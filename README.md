# stock-rec-sys
[![Java CI with Gradle](https://github.com/jycforest29/stock-rec-sys/actions/workflows/gradle.yml/badge.svg)](https://github.com/jycforest29/stock-rec-sys/actions/workflows/gradle.yml)

branch protection 확인
- 프로젝트 소개
    
    사용자의 주식 거래 내역 기반으로 대신 매수했으면 더 좋았을 주식을 추천해주는 서비스로, 중앙대학교 비즈니스 인텔리전스 수업에서 개인과제로 진행함.
    
- 설계
    - 전체 아키텍처 설계
        
        ![image](https://user-images.githubusercontent.com/103106183/210195967-b114e326-1b3d-4631-ae56-7916f2e56d04.png)

        프로젝트를 Data pipeline 과 Server로 분리함.
        
        Data pipeline 에서는 Dart open api(dart_fss)와 yfinance의 외부 Api를 사용하여 데이터를 수집하고, Jupyter notebook을 통해 이를 Mysql에 적재함.
        
        Server 에서는 클라이언트단에서 JSON 형식으로 전달한 데이터를 바탕으로 DB에서 해당 데이터와 연관된 내용을 읽어와야 했으므로, read만 가능하도록 설정하여 불필요한 transactional 관련 이슈를 막도록 설계하였음.

    - UML 설계
        
        ![image](https://user-images.githubusercontent.com/103106183/208821069-248c33a0-b461-4a0b-aa41-dd3bdd07747d.png)
        
        StockEntity 1개에 대해 
        
        - 한 달간의 종목 추이 정보를 담는 MonthEntity
        - 5일간의 종목 추이 정보를 담는 Day5Entity
        - 한 분기간의 종목 추이 정보를 담는 QuarterEntity
        
        가 존재함. 
        
        컬럼에 들어가는 내용은 다 다르지만 세 테이블 모두 사용하는 컬럼은 동일하므로 @MappedSuperclass 를 사용해 DB에 반영은 안되지만 추상 클래스로 상속을 구현할 수 있게 설계함.
        
    - 추천 로직 설계
    
        수집한 데이터의 특성을 고려해 콘텐츠 기반 필터링 방식 사용함.
    
        섹터와 마켓 기준으로 필터링 후 가격, 거래량, 가격 그래프의 표준편차에 관해 각각 코사인 유사도를 계산함.

        이후 각 유사도 값이 모두 0.9 이상이고, 백테스팅 함수를 통해 해당 기간 또는 근접한 기간 동안의 수익이 사용자가 실제 얻은 수익보다 높을 때 추천하도록 함.

        - 0.9 라는 값은 경험적으로 산출된 값
     
     - 백테스팅 로직 설계
     
        단순히 입력으로 받은 기간에 대해 백테스팅을 수행할 경우 응답값이 null인 빈도가 약 30%의 확률로 존재하였음.(12번 수행)
        
        따라서 시작 날짜와 종료 날짜를 각각 +1, -1씩 연산하며 사용자가 입력한 기간과 근접한 기간까지 확장하여 백테스팅을 수행해 추천 시스템의 정확도를 높임.
        
        시간 최적화 또한 고려해야 했기 때문에 각 종목에 대해 최대 5번씩 위의 연산을 수행하도록 설계함.
     
- 테스트

    Junit, Mock 테스트 라이브러리를 통해 유닛 테스트 작성한 뒤 Github Actions를 통해 테스트 자동화

- 결과

    rate는 요청한 종목과 응답한 종목의 유사도
    
    toCompare는 실제 수익
    
    better는 개선된 수익
    

    - 요청
    
        ```json
        {
            "requestDTOs":[
                {
                    "start":{
                        "year":"2022", "month":"9", "date":"14"
                    },
                    "end":{
                        "year":"2022", "month":"11", "date":"11"
                    },
                    "stockName":"일동제약",
                    "stockCode":"249420",
                    "volume":36,
                    "profit":3.6
                },
                {
                    "start":{
                        "year":"2022", "month":"9", "date":"14"
                    },
                    "end":{
                        "year":"2022", "month":"11", "date":"11"
                    },
                    "stockName":"두산밥캣",
                    "stockCode":"241560",
                    "volume":36,
                    "profit":3.6
                },
                {
                    "start":{
                        "year":"2022", "month":"9", "date":"14"
                    },
                    "end":{
                        "year":"2022", "month":"11", "date":"11"
                    },
                    "stockName":"DSR",
                    "stockCode":"069460",
                    "volume":36,
                    "profit":3.6
                }
            ]
        }
 
    - 응답(,,,는 이하 생략)
        ```json
        [
            [
                {
                    "start": {
                        "year": "2022",
                        "month": "SEPTEMBER",
                        "date": "17"
                    },
                    "end": {
                        "year": "2022",
                        "month": "NOVEMBER",
                        "date": "11"
                    },
                    "stockName": "대웅제약",
                    "stockCode": "069620",
                    "toCompare": 1051.2,
                    "better": 154000.0,
                    "rateVO": {
                        "rate": 0.9857678936147812,
                        "priceRate": 0.9938612288154084,
                        "volumeRate": 0.986975523062969,
                        "priceSdRate": 0.9764669289659661
                    }
                },
                ,,,
            ],
            [
                {
                    "start": {
                        "year": "2022",
                        "month": "SEPTEMBER",
                        "date": "17"
                    },
                    "end": {
                        "year": "2022",
                        "month": "NOVEMBER",
                        "date": "11"
                    },
                    "stockName": "한국주강",
                    "stockCode": "025890",
                    "toCompare": 1285.2,
                    "better": 2445.0,
                    "rateVO": {
                        "rate": 0.9713194918088685,
                        "priceRate": 0.9961715527942873,
                        "volumeRate": 0.9979996760238415,
                        "priceSdRate": 0.9197872466084765
                    }
                }
            ],
            [
                {
                    "start": {
                        "year": "2022",
                        "month": "SEPTEMBER",
                        "date": "14"
                    },
                    "end": {
                        "year": "2022",
                        "month": "NOVEMBER",
                        "date": "11"
                    },
                    "stockName": "영풍",
                    "stockCode": "000670",
                    "toCompare": 50.4,
                    "better": 61000.0,
                    "rateVO": {
                        "rate": 0.9677148970716353,
                        "priceRate": 0.9994408915100508,
                        "volumeRate": 0.9462498527339025,
                        "priceSdRate": 0.9574539469709525
                    }
                },
                ,,,
            ]
        ]
