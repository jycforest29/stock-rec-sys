package com.bi.stockrecsys.service;

import com.bi.stockrecsys.repository.RecordRepository;
import com.bi.stockrecsys.repository.StockRepository;
import com.bi.stockrecsys.repository.transaction.Day5Repository;
import com.bi.stockrecsys.repository.transaction.MonthRepository;
import com.bi.stockrecsys.repository.transaction.QuarterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainServiceTest {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private QuarterRepository quarterRepository;
    @Autowired
    private MonthRepository monthRepository;
    @Autowired
    private Day5Repository day5Repository;
    @Autowired
    private RecordRepository recordRepository;

}