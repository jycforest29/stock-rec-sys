# stock-rec-sys

- 프로젝트 이름

    아 이거 살걸

- 프로젝트 소개
    
    사용자의 주식 거래 내역 기반으로, 대신 매수했으면 더 좋았을 주식을 추천해주는 서비스
    
- 프로젝트 구조 

  ![image](https://user-images.githubusercontent.com/103106183/208821195-93a13b3c-7c6d-414c-97c1-164d260fcdf8.png)
  
  * Dart Open api는 dart_fss 사용

- 추천시스템 구현 방법
    
    가격, 거래량, 가격 그래프의 표준편차에 관해 각각 코사인 유사도 계산함.
    
    이후 각 유사도 값이 모두 0.5 이상일 때 추천하도록 함.
    
- 클래스 다이어그램
    
    ![image](https://user-images.githubusercontent.com/103106183/208821069-248c33a0-b461-4a0b-aa41-dd3bdd07747d.png)

- 더 추가할 기능  

    - 멀티스레딩 적용해 성능 최적화
    
    - SOLID 원칙 지키도록 리팩토링
    
    - 추천 알고리즘 개선(단순히 사용자가 입력한 기간 그대로 반영해 추천하지 말고, 수익률이 낮거나 유사도가 낮을 경우 기간 변경해가며 추천하도록)

- 프로젝트 소개
    
    사용자의 주식 거래 내역 기반으로 대신 매수했으면 더 좋았을 주식을 추천해주는 프로젝트 입니다.
    
    
- 추천시스템 구현 방법
    
    priceRate, volumeRate, sdRate에 관해 각각 코사인 유사도 계산함.
    
    이후 각 유사도 값이 모두 0.5 이상일 때 추천하도록 함.

    
- 클래스 다이어그램
    
    ![image](https://user-images.githubusercontent.com/103106183/208874432-d0261f07-e3a5-4cec-98a4-35145c0edf4c.png)
    
- 데이터베이스


    ![image](https://user-images.githubusercontent.com/103106183/208874474-b50a41d1-2d67-4a54-a7c6-9a53a98ef127.png)

        
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
