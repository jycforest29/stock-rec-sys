package com.bi.stockrecsys.service;

import com.bi.stockrecsys.entity.transaction.Day5Entity;
import com.bi.stockrecsys.exception.ArraySizeNotEqualException;
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

    public MainService(StockRepository stockRepository,
                       QuarterRepository quarterRepository,
                       MonthRepository monthRepository,
                       Day5Repository day5Repository,
                       RecordRepository recordRepository){
        this.stockRepository = stockRepository;
        this.quarterRepository = quarterRepository;
        this.monthRepository = monthRepository;
        this.day5Repository = day5Repository;
        this.recordRepository = recordRepository;
    }

    public List<ResponseDTO> recommend(RequestDTO requestDTO){
        ArrayList<ResponseDTO> responseDTOs = new ArrayList<>();
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
                    responseDTOs.add(resT);
                }
            }
        }
        responseDTOs = sortByGetBetter(responseDTOs);
        responseDTOs = getTopFive(responseDTOs);

        return responseDTOs;
    }

    public ArrayList<ResponseDTO> sortByGetBetter(ArrayList<ResponseDTO> responseDTOs){
        responseDTOs.sort(Comparator.comparing(ResponseDTO::getBetter));
        Collections.reverse(responseDTOs);

        return responseDTOs;
    }

    public ArrayList<ResponseDTO> getTopFive(ArrayList<ResponseDTO> responseDTOs){
        if(responseDTOs.size() >= 5){
            ArrayList<ResponseDTO> getTopFiveResult = new ArrayList<>(responseDTOs.subList(0, 5));
            return getTopFiveResult;
        }
        return responseDTOs;
    }

    public double getCosineSimilarity(double[] vectorA, double[] vectorB) {
        isArraySizeEqual(vectorA, vectorB);

        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        for(int i = 0; i < vectorA.length ; i++){
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        // [ToDo - getCosineSimilarity()를 호출하는 로직에서 ArithmeticException()이 throw 되었을 때 처리하는 로직 필요함.]
        if (normA == 0 | normB == 0){
            throw new ArithmeticException(); // division by zero 예외처리
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public void isArraySizeEqual(double[] vectorA, double[] vectorB){
        if (vectorA.length != vectorB.length){
            throw new ArraySizeNotEqualException();
        }
    }

    public String toDate(DateVO dateVO){
        // [ToDo - 앞의 로직에서 year의 예외처리가 선행되어야 함.]
        int month = Integer.parseInt(dateVO.getMonth());
        int date = Integer.parseInt(dateVO.getDate());

        return dateVO.getYear()+"-"+String.format("%02d", month)+"-"+String.format("%02d", date);
    }

    public ResponseDTO getUpperProfit(StockEntity stockEntity, RateVO rateVO, DateVO start, DateVO end, double toCompare){
        // 알고리즘 개선 - stockEntity
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
