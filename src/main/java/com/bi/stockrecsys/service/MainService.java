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
import com.bi.stockrecsys.vo.StockRecommendVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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
    private StockRecommendVO stockRecommendVO;

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
        stockRecommendVO = getStockRecommendVO(stock, requestDTO);
        candidates = getCandidates(stock);

        for(StockEntity candidate : candidates){
            HashMap<String, double[]> avgRateWrapper = getAvgRateWrapper(candidate.getCode());
            RateVO rateVO = getRateVO(avgRateWrapper, stockRecommendVO);

            if (rateVO != null){
                ResponseDTO responseDTO = getUpperProfit(
                        candidate, rateVO, requestDTO.getStart(), requestDTO.getEnd(), stockRecommendVO.getToCompare()
                );
                if(responseDTO != null){
                    responseDTOs.add(responseDTO);
                }
            }
        }
        responseDTOs = sortByGetBetter(responseDTOs);
        responseDTOs = getTopFive(responseDTOs);

        return responseDTOs;
    }

    public HashMap<String, double[]> getAvgRateWrapper(String stockCode){
        HashMap<String, double[]> avgRateWrapper = new HashMap<>();

        QuarterEntity quarterEntity = quarterRepository.findByStockCode(stockCode);
        MonthEntity monthEntity = monthRepository.findByStockCode(stockCode);
        Day5Entity day5Entity = day5Repository.findByStockCode(stockCode);

        // [ToDo - ????????? ?????? ????????????.]
        avgRateWrapper.put("priceRateWrapper",
                new double[]{quarterEntity.getAvgPrice(), monthEntity.getAvgPrice(), day5Entity.getAvgPrice()}
        );
        avgRateWrapper.put("volumeRateWrapper",
                new double[]{quarterEntity.getAvgVolume(), monthEntity.getAvgVolume(), day5Entity.getAvgVolume()}
        );
        avgRateWrapper.put("sdRateWrapper",
                new double[]{quarterEntity.getAvgPriceSd(), monthEntity.getAvgPriceSd(), day5Entity.getAvgPriceSd()}
        );

        return avgRateWrapper;
    }

    public RateVO getRateVO(HashMap<String, double[]> avgRateWrapper, StockRecommendVO stockRecommendVO){
        double priceRate = getCosineSimilarity(
                avgRateWrapper.get("priceRateWrapper"), stockRecommendVO.getAvgPriceWrapper()
        );
        double volumeRate = getCosineSimilarity(
                avgRateWrapper.get("volumeRateWrapper"), stockRecommendVO.getAvgVolumeWrapper()
        );
        double sdRate = getCosineSimilarity(
                avgRateWrapper.get("sdRateWrapper"), stockRecommendVO.getAvgSdWrapper()
        );
        double rate = (priceRate+volumeRate+sdRate) / 3;

        if (priceRate > 0.9 & volumeRate > 0.9 & sdRate > 0.9){
            RateVO rateVO = new RateVO(rate, priceRate, volumeRate, sdRate);
            return rateVO;
        }
        return null;
    }

    public StockRecommendVO getStockRecommendVO(StockEntity stockEntity, RequestDTO requestDTO){
        RecordEntity inputRecordEntity = recordRepository.findByPk(new Pk(stockEntity, toDate(requestDTO.getEnd())));
        double inputStockPrice = inputRecordEntity.getPrice()/100*(requestDTO.getProfit());
        HashMap<String, double[]> avgRateWrapper = getAvgRateWrapper(stockEntity.getCode());

        return StockRecommendVO.builder()
                .avgPriceWrapper(avgRateWrapper.get("priceRateWrapper"))
                .avgVolumeWrapper(avgRateWrapper.get("volumeRateWrapper"))
                .avgSdWrapper(avgRateWrapper.get("sdRateWrapper"))
                .toCompare(inputStockPrice)
                .build();
    }

    public List<StockEntity> getCandidates(StockEntity stockEntity){
        List<StockEntity> candidates = stockRepository.findBySectorAndMarket(
                stockEntity.getSector(), stockEntity.getMarket());
        candidates.remove(stockEntity);
        return candidates;
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

        // [ToDo - getCosineSimilarity()??? ???????????? ???????????? ArithmeticException()??? throw ????????? ??? ???????????? ?????? ?????????.]
        if (normA == 0 | normB == 0){
            throw new ArithmeticException(); // division by zero ????????????
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public void isArraySizeEqual(double[] vectorA, double[] vectorB){
        if (vectorA.length != vectorB.length){
            throw new ArraySizeNotEqualException();
        }
    }

    public String toDate(DateVO dateVO){
        // [ToDo - ?????? ???????????? year??? ??????????????? ??????????????? ???.]
        int month = Integer.parseInt(dateVO.getMonth());
        int date = Integer.parseInt(dateVO.getDate());

        return dateVO.getYear()+"-"+String.format("%02d", month)+"-"+String.format("%02d", date);
    }

    public ResponseDTO getUpperProfit(StockEntity stockEntity, RateVO rateVO, DateVO start, DateVO end, double toCompare){
        double startPlusADayPrice;
        double endMinusADayPrice;
        LocalDate startLocalDate = LocalDate.parse(toDate(start));
        LocalDate endLocalDate = LocalDate.parse(toDate(end));
        double startPrice = getPrice(startLocalDate, stockEntity);
        double endPrice = getPrice(endLocalDate, stockEntity);
        int count = 0;

        while (endPrice - startPrice <= toCompare){
            if (count >= 5){
                return null;
            }
            // ?????? ????????? ?????? plus
            startPlusADayPrice = getPrice(startLocalDate.plus(Period.ofDays(1)), stockEntity);

            // ?????? ????????? ?????? minus
            endMinusADayPrice = getPrice(endLocalDate.minus(Period.ofDays(1)), stockEntity);

            if (endMinusADayPrice - startPrice > endPrice - startPlusADayPrice){
                endPrice = endMinusADayPrice;
                endLocalDate = endLocalDate.minus(Period.ofDays(1));
            } else if (endPrice - startPlusADayPrice > endMinusADayPrice - startPrice) {
                startPrice = startPlusADayPrice;
                startLocalDate = startLocalDate.plus(Period.ofDays(1));
            }
            count += 1;
        }

        String s = String.valueOf(startLocalDate.getMonth());
        return ResponseDTO.builder()
                .start(DateVO.builder()
                        .year(String.valueOf(startLocalDate.getYear()))
                        .month(String.valueOf(startLocalDate.getMonthValue()))
                        .date(String.valueOf(startLocalDate.getDayOfMonth()))
                        .build()
                )
                .end(DateVO.builder()
                        .year(String.valueOf(endLocalDate.getYear()))
                        .month(String.valueOf(endLocalDate.getMonthValue()))
                        .date(String.valueOf(endLocalDate.getDayOfMonth()))
                        .build()
                )
                .stockName(stockEntity.getName())
                .stockCode(stockEntity.getCode())
                .toCompare(toCompare)
                .better(endPrice - startPrice) // ??????
                .rateVO(rateVO)
                .build();
    }

    public double getPrice(LocalDate localDate, StockEntity stockEntity){
        try{
            RecordEntity record = recordRepository.findByPk(new Pk(stockEntity, localDate.toString()));
            return record.getPrice();
        }catch (Exception e){
            return 0; //  ???????????? ????????? ?????? ????????? ???????????? 0 ??????
        }
    }

}
