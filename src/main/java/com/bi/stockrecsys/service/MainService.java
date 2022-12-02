package com.bi.stockrecsys.service;

import com.bi.stockrecsys.entity.transaction.Day5Entity;
import com.bi.stockrecsys.entity.transaction.Transaction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MainService {

    private final StockRepository stockRepository;
    private final QuarterRepository quarterRepository;
    private final MonthRepository monthRepository;
    private final Day5Repository day5Repository;
    private final RecordRepository recordRepository;

    private StockEntity stock;
    private List<StockEntity> candidates;
    private HashMap<StockEntity, RateVO> upperSimilarity;
    private HashMap<StockEntity, RateVO> upperProfit;
    private ArrayList<ResponseDTO> res;

    public MainService(StockRepository stockRepository, QuarterRepository quarterRepository,MonthRepository monthRepository, Day5Repository day5Repository, RecordRepository recordRepository){
        this.stockRepository = stockRepository;
        this.quarterRepository = quarterRepository;
        this.monthRepository = monthRepository;
        this.day5Repository = day5Repository;
        this.recordRepository = recordRepository;
    }

    public List<ResponseDTO> recommend(RequestDTO requestDTO){

        stock = stockRepository.findByCode(requestDTO.getStockCode());

        candidates = stockRepository.findBySectorAndMarket(stock.getSector(), stock.getMarket());

        QuarterEntity qStock = quarterRepository.findByStockCode(stock.getCode());
        MonthEntity mStock = monthRepository.findByStockCode(stock.getCode());
        Day5Entity dStock = day5Repository.findByStockCode(stock.getCode());

        // 각 price 유사도, volume 유사도, upAndDown 유사도 구함 -> 해당 종목과 유사도가 0.5 이상 높은 주식 필터링
        for(StockEntity candidate : candidates){
            QuarterEntity q = quarterRepository.findByStockCode(candidate.getCode());
            MonthEntity m = monthRepository.findByStockCode(candidate.getCode());
            Day5Entity f = day5Repository.findByStockCode(candidate.getCode());

            // price 유사도
            Double priceRate = 0.0;
            priceRate += getCosineSimilarity(q.getAvgPrice(), qStock.getAvgPrice());
            priceRate += getCosineSimilarity(m.getAvgPrice(), mStock.getAvgPrice());
            priceRate += getCosineSimilarity(f.getAvgPrice(), dStock.getAvgPrice());

            // volume 유사도
            Double volumeRate = 0.0;
            volumeRate += getCosineSimilarity(q.getAvgVolume(), qStock.getAvgVolume());
            volumeRate += getCosineSimilarity(m.getAvgVolume(), mStock.getAvgVolume());
            volumeRate += getCosineSimilarity(f.getAvgVolume(), dStock.getAvgVolume());

            // sd 유사도
            Double sdRate = 0.0;
            sdRate += getCosineSimilarity(q.getAvgSd(), qStock.getAvgSd());
            sdRate += getCosineSimilarity(m.getAvgSd(), mStock.getAvgSd());
            sdRate += getCosineSimilarity(f.getAvgSd(), dStock.getAvgSd());

            double rate = (priceRate+volumeRate+sdRate) / 3;
            RateVO rateDTO = new RateVO(rate, priceRate, volumeRate, sdRate);
            if (rate >= 0.5){
                upperSimilarity.put(candidate, rateDTO);
            }
        }
        res = getUpperProfit(upperSimilarity, requestDTO.getStart(), requestDTO.getEnd(), requestDTO.getProfit());
        return res;
    }

    public double getCosineSimilarity(double vectorA, double vectorB){
        double dotProduct = vectorA * vectorB;
        double normA = Math.pow(vectorA, 2);
        double normB = Math.pow(vectorB, 2);
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public ArrayList<ResponseDTO> getUpperProfit(HashMap h, DateVO start, DateVO end, double profit){
        ArrayList<ResponseDTO> resT = new ArrayList<>();
        for (Object stockEntity : h.keySet()){
            RecordEntity b = recordRepository.findByStockAndDate((StockEntity) stockEntity, start.toString());
            RecordEntity e = recordRepository.findByStockAndDate((StockEntity) stockEntity, start.toString());
            if ((e.getPrice()-b.getPrice()) >= profit){
                resT.add(new ResponseDTO(start, end, ((StockEntity) stockEntity).getCode(), ((StockEntity) stockEntity).getName(), profit, (e.getPrice()-b.getPrice()), (RateVO) h.get(stockEntity)));
            }
        }
        return resT;
    }
}
