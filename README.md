# stock-rec-sys
[![Java CI with Gradle](https://github.com/jycforest29/stock-rec-sys/actions/workflows/gradle.yml/badge.svg)](https://github.com/jycforest29/stock-rec-sys/actions/workflows/gradle.yml)
- 프로젝트 이름

    아 이거 살걸

- 프로젝트 소개
    
    사용자의 주식 거래 내역 기반으로, 대신 매수했으면 더 좋았을 주식을 추천해주는 서비스로 중앙대학교 비즈니스 인텔리전스 수업에서 개인과제로 진행했습니다.

- 추천시스템 구현 방법
    
    섹터와 마켓 기준으로 필터링 후 가격, 거래량, 가격 그래프의 표준편차에 관해 각각 코사인 유사도 계산함.
    
    이후 각 유사도 값이 모두 0.5 이상이고, 해당 기간동안의 수익이 사용자가 실제 얻은 수익보다 높을 때 추천하도록 함.
    
    - 0.5라는 값은 경험적으로 산출된 값
    
- 설계
    - 전체 아키텍처 설계
        
        ![image](https://user-images.githubusercontent.com/103106183/210195967-b114e326-1b3d-4631-ae56-7916f2e56d04.png)
        
        * Dart Open api는 dart_fss 사용
        
        우선 DB 수집부터 추천 시스템 서버 개발까지 나 혼자 해야했기에 어떻게 구현할지 설계하는 것은 온전히 내가 정할 수 있었다.
        
        따라서 나는 우선 크게 data pipeline 과 server단으로 분리하였다.
        
        Data pipeline에선 데이터 처리에 유리한 주피터노트북에서 dart open api와 yfinance의 외부 api를 사용하여 데이터를 수집하고, 이를 mysql에 적재하였다. 나중에 종목의 삭제/추가까지 확장되어야 하므로 주피터노트북에서 DB에 접근할 때는 read, write 둘 다 가능하도록 설계하였다.
        
        Server에선 클라이언트단에서 JSON 형식으로 전달한 데이터를 바탕으로 DB에서 해당 데이터와 연관된 내용을 읽어와야 했으므로, read만 가능하도록 설정하여 불필요한 transactional 관련 이슈를 막도록 설계하였다.
        
    - UML 설계
        
        ![image](https://user-images.githubusercontent.com/103106183/208821069-248c33a0-b461-4a0b-aa41-dd3bdd07747d.png)
        
        종목 1개에 대해 
        
        - 한 달간의 종목 추이 정보를 담는 MonthEntity
        - 5일간의 종목 추이 정보를 담는 Day5Entity
        - 한 분기간의 종목 추이 정보를 담는 QuarterEntity
        
        가 존재한다. 컬럼에 들어가는 내용은 다 다르지만 세 테이블 모두 사용하는 컬럼은 동일하다.
        
        따라서 @MappedSuperclass 를 사용해 DB에 반영은 안되지만 추상 클래스로 상속을 구현할 수 있게 설계하였다.
        
- 어려웠던 점과 해결
    - 추천 알고리즘 정하기
        
        나는 다른 학생들과 달리 Kaggle에서 데이터를 가져오지 않았다. 정제된 캐글 데이터셋을 사용하면 더 깔끔하게 진행할 수 있겠지만, 좀 더 실무에 가까운 프로젝트를 해보고 싶었기 때문이다. 따라서, DB를 설계한 후 원하는 데이터들을 직접 외부 API를 통해 수집하였다.
        
        하지만 주식 데이터의 경우 트레이딩 내역에 대한 데이터셋을 수집하기가 힘들었고, 이를 가상으로 만드는 것도 생각해봤지만 프로젝트의 정확도 자체가 떨어질 것 같아 적합하지 않아보였다.
        
        유저의 트레이딩 데이터가 없으면 협업 필터링등의 user-matrix 기반 방식을 사용할 수 없으므로 컨텐츠 기반 추천으로 방향을 바꿨다. 실무에선 여러개의 추천 시스템 방식을 복합적으로 사용하고, 컨텐츠 기반 추천은 정확도가 많이 떨어지기에 잘 안 쓰인다는 것을 알지만 이 프로젝트의 목적은 100% 추천 알고리즘 구현보다는 혼자 프로젝트의 전반적인 진행을 이끌어보는 것이었기 때문에 해당 방향으로 구현하였다.
        
        컨텐츠 기반 추천시스템을 구현할 때 사용되는 데이터의 타입이 주로 numerical 이라는 점에서 벡터에 기반해 계산하는 코사인 유사도가 적합할 것이라고 판단했다. 

- 더 추가할 기능  

    - 멀티스레딩 적용해 성능 최적화
    
    - cached memory 적용해 성능 최적화
    
    - SOLID 원칙 지키도록 리팩토링
    
    - 싱글톤 패턴 적용해 리팩토링
    
    - 추천 알고리즘 개선(단순히 사용자가 입력한 기간 그대로 반영해 추천하지 말고, 수익률이 낮거나 유사도가 낮을 경우 기간 변경해가며 추천하도록)
        
- 결과
    
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
        }]
    }
    ```
    
    ```json
    [
        [
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "동아에스티",
                "stockCode": "170900",
                "toCompare": 1051.2,
                "better": 4600.0,
                "rateVO": {
                    "rate": 0.9448264838851159,
                    "priceRate": 0.9987742506553949,
                    "volumeRate": 0.861172365563372,
                    "priceSdRate": 0.9745328354365808
                }
            },
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "유한양행",
                "stockCode": "000100",
                "toCompare": 1051.2,
                "better": 2500.0,
                "rateVO": {
                    "rate": 0.967711668804231,
                    "priceRate": 0.996757448007138,
                    "volumeRate": 0.9890141476404511,
                    "priceSdRate": 0.9173634107651037
                }
            },
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "대원제약",
                "stockCode": "003220",
                "toCompare": 1051.2,
                "better": 1400.0,
                "rateVO": {
                    "rate": 0.9703329773585566,
                    "priceRate": 0.9981673051197735,
                    "volumeRate": 0.9912162721933732,
                    "priceSdRate": 0.9216153547625229
                }
            }
        ],
        [
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "현대두산인프라코어",
                "stockCode": "042670",
                "toCompare": 1285.2,
                "better": 1910.0,
                "rateVO": {
                    "rate": 0.9045451808279236,
                    "priceRate": 0.9966501962862117,
                    "volumeRate": 0.8074568294062188,
                    "priceSdRate": 0.9095285167913405
                }
            }
        ],
        [
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
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
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "삼아알미늄",
                "stockCode": "006110",
                "toCompare": 50.4,
                "better": 8250.0,
                "rateVO": {
                    "rate": 0.9728171018864047,
                    "priceRate": 0.9984338382599585,
                    "volumeRate": 0.9271996371226875,
                    "priceSdRate": 0.9928178302765679
                }
            },
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "풍산",
                "stockCode": "103140",
                "toCompare": 50.4,
                "better": 2050.0,
                "rateVO": {
                    "rate": 0.9429183466451888,
                    "priceRate": 0.999500952538884,
                    "volumeRate": 0.834443117555538,
                    "priceSdRate": 0.9948109698411446
                }
            },
            {
                "start": {
                    "year": "2022",
                    "month": "9",
                    "date": "14"
                },
                "end": {
                    "year": "2022",
                    "month": "11",
                    "date": "11"
                },
                "stockName": "이구산업",
                "stockCode": "025820",
                "toCompare": 50.4,
                "better": 60.0,
                "rateVO": {
                    "rate": 0.9936876663349444,
                    "priceRate": 0.9990990233941247,
                    "volumeRate": 0.9992672747862202,
                    "priceSdRate": 0.9826967008244883
                }
            }
        ]
    ]
    ```
    - toCompare는 실제 수익
    - better는 개선된 수익
