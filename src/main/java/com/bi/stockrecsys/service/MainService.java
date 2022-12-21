package com.bi.stockrecsys.service;

import com.bi.stockrecsys.entity.transaction.Day5Entity;
import com.bi.stockrecsys.repository.transaction.Day5Repository;
import com.bi.stockrecsys.vo.RateVO;
import com.bi.stockrecsys.dto.RequestDTO;
import com.bi.stockrecsys.vo.DateVO;
import com.bi.stockrecsys.dto.ResponseDTO;
import com.bi.stockrecsys.entity.*;
import com.bi.stockrecsys.entity.transaction.MonthEntity;
import com.bi.stockrecsys.entity.transaction.QuarterEntity;
import com.bi.stockrecsys.repository.*;
import com.bi.stockrecsys.repository.transaction.MonthRepository;
import com.bi.stockrecsys.repository.transaction.QuarterRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {

    private final StockRepository stockRepository;
    private final QuarterRepository quarterRepository;
    private final MonthRepository monthRepository;
    private final Day5Repository day5Repository;
    private final RecordRepository recordRepository;

    private StockEntity stock;
    private List<StockEntity> candidates;
    private HashMap<StockEntity, RateVO> upperSimilarity = new HashMap<>();
    private double toCompare = 0;

    public MainService(StockRepository stockRepository, QuarterRepository quarterRepository,MonthRepository monthRepository, Day5Repository day5Repository, RecordRepository recordRepository){
        this.stockRepository = stockRepository;
        this.quarterRepository = quarterRepository;
        this.monthRepository = monthRepository;
        this.day5Repository = day5Repository;
        this.recordRepository = recordRepository;
    }

    // [To - Refactor] 모듈화 제대로 되어있지 않음.
    public List<ResponseDTO> recommend(RequestDTO requestDTO){
        ArrayList<ResponseDTO> res = new ArrayList<>();
        stock = stockRepository.findByCode(requestDTO.getStockCode());
        candidates = stockRepository.findBySectorAndMarket(stock.getSector(), stock.getMarket());
        candidates.remove(stock);

        QuarterEntity qStock = quarterRepository.findByStockCode(stock.getCode());
        MonthEntity mStock = monthRepository.findByStockCode(stock.getCode());
        Day5Entity dStock = day5Repository.findByStockCode(stock.getCode());
        toCompare = recordRepository.findByPk(new Pk(stock, toDate(requestDTO.getEnd()))).getPrice()/100*requestDTO.getProfit();

        // 각 price 유사도, volume 유사도, upAndDown 유사도 구함 -> 해당 종목과 유사도가 0.5 이상 높은 주식 필터링
        for(StockEntity candidate : candidates){
            QuarterEntity q = quarterRepository.findByStockCode(candidate.getCode());
            MonthEntity m = monthRepository.findByStockCode(candidate.getCode());
            Day5Entity f = day5Repository.findByStockCode(candidate.getCode());

            Double priceRate = getCosineSimilarity(new double[]{q.getAvgPrice(), m.getAvgPrice(), f.getAvgPrice()}, new double[]{qStock.getAvgPrice(), mStock.getAvgPrice(), dStock.getAvgPrice()});
            Double volumeRate = getCosineSimilarity(new double[]{q.getAvgVolume(), m.getAvgVolume(), f.getAvgVolume()}, new double[]{qStock.getAvgVolume(), mStock.getAvgVolume(), dStock.getAvgVolume()});
            Double sdRate = getCosineSimilarity(new double[]{q.getAvgPriceSd(), m.getAvgPriceSd(), f.getAvgPriceSd()}, new double[]{qStock.getAvgPriceSd(), mStock.getAvgPriceSd(), dStock.getAvgPriceSd()});

            double rate = (priceRate+volumeRate+sdRate) / 3;
            RateVO rateVO = new RateVO(rate, priceRate, volumeRate, sdRate);
            if (priceRate >= 0.5 & volumeRate >= 0.5 & sdRate >= 0.5){
                ResponseDTO resT = getUpperProfit(candidate,rateVO, requestDTO.getStart(), requestDTO.getEnd(), toCompare);
                if (resT != null){
                    res.add(resT);
                }
            }
        }

        // sort
        res.sort(Comparator.comparing(ResponseDTO::getBetter));
        Collections.reverse(res);

        if(res.size() >= 5){
            ArrayList<ResponseDTO> subListedRes = new ArrayList<>(res.subList(0, 5));
            return subListedRes;
        }
        return res;
    }


    public double getCosineSimilarity(double[] vectorA, double[] vectorB){
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        if (vectorA.length != vectorB.length){
            throw new RuntimeException("두 벡터의 길이가 달라 코사인 유사도 계산 불가");
        }
        for(int i = 0; i < vectorA.length ; i++){
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public String toDate(DateVO dateVO){
//        [To - feat] year에 대해 예외처리 안함(좀 애매함)
        int month = Integer.parseInt(dateVO.getMonth());
        int date = Integer.parseInt(dateVO.getDate());

        return dateVO.getYear()+"-"+String.format("%02d", month)+"-"+String.format("%02d", date);
    }

    public ResponseDTO getUpperProfit(StockEntity stockEntity, RateVO rateVO, DateVO start, DateVO end, double toCompare){
        RecordEntity b = recordRepository.findByPk(new Pk((StockEntity) stockEntity, toDate(start)));
        RecordEntity e = recordRepository.findByPk(new Pk((StockEntity) stockEntity, toDate(end)));
        if ((e.getPrice()-b.getPrice()) >= toCompare){
            try{
                return new ResponseDTO(start, end, ((StockEntity) stockEntity).getName(), ((StockEntity) stockEntity).getCode(), toCompare, (e.getPrice()-b.getPrice()), rateVO);
            }
            catch (Exception error){
                error.printStackTrace();
            }
        }
        return null;
    }
}
