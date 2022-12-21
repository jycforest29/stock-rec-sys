# stock-rec-sys

- 프로젝트 소개
    
    사용자의 주식 거래 내역 기반으로, 대신 매수했으면 더 좋았을 주식을 추천해주는 서비스
    
- 프로젝트 구조 

  ![image](https://user-images.githubusercontent.com/103106183/208821195-93a13b3c-7c6d-414c-97c1-164d260fcdf8.png)

- 추천시스템 구현 방법
    
    가격, 거래량, 가격 그래프의 표준편차에 관해 각각 코사인 유사도 계산함.
    
    이후 각 유사도 값이 모두 0.5 이상일 때 추천하도록 함.
    
- 클래스 다이어그램
    
    ![image](https://user-images.githubusercontent.com/103106183/208821069-248c33a0-b461-4a0b-aa41-dd3bdd07747d.png)
