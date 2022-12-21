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
