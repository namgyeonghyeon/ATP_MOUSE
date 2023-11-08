프로젝트명: ATP_MOUSE

프로젝트 설명: 다한증이나 수족냉증을 가진 컴퓨터이용자들은 컴퓨터 마우스를 조작 할 때, 땀이나 체온으로 인해 불쾌한 이용환경을 가지게된다. 이와 같은 이용자들을 위해 냉풍·열풍기능을 추가해 쾌적환 이용환경을 조성하기 위한 제품이다.

사용 언어 : java

사용한 데이터베이스 : FireBase


개발 방법 
· Web server를 이용해 Internet 통신으로 Raseberry PI로 명령어 전송 및 데이터 수신, 명령어 처리, 동기화
· Arduino를 이용해 HW제어, Raspberry PI로 데이테 송신및 명령어 처리
· Android application을 이용해 Web server와 Internet 통신
· Firebase, Realtime Firebase를 이용하여 로그인, 회원가입, 기기등록
· Android 를 이용해 어플리케이션 제작


주기능
- 온도 센서 : 사용자의 손의 온도를 측정하여 데이터를 전송한다
- 펠티어 소자 : 제벡 효과에 의해 한쪽이 차가워지면 다른쪽은 뜨거워진다. 냉·온기능을 한 소자로 구현가능하다
- 팬(Fan) : 사용자 손에 펠티어 소자의 온도를 보내는용도와 펠티어 소자를 공랭하기 위한 용도로 사용한다
- 마우스 모듈 : 기본적인 마우스 모듈로써 마우스 조작을 컴퓨터로 전송한다
  
부기능
- 심박수 센서 : 사용자의 심박수를 측정하여 이상여부를 판단, 사용자에게 이를 알린다.


- ![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/8e8e0168-e98f-488b-9bbe-1efdb04077a2)

기대효과
- 수족냉증이나 다한증으로 고민이 많았던 사용자들에게 온도 조절 기능으로 자동으로 온도를 측정하고 적절한 온도를 계산하여 냉 온열을 발생시켜 쾌적한 마우스 사용을 할 수 있다.  
- 심박수 측정 기능으로 사용자들의 건강관리와 비정상 심박시 알림을 알 수 있어 건강상태를 체크할 수 있다.


활용 분야
- 건강관리 : 다양한 방법으로 건강관리를 할 수 있지만 심박수 체크 기능으로 부정맥 등 평균 심박수에 벗어날 시 서버와 클라이언트를 통해 PADDISPLAY와 APP으로 알림을 받을 수 있다.

- 사용자 니즈 파악: 다한증 사용자들은 땀이 나면 옆에 손수건을 두어 자주 땀을 닦아야하고 수족냉증 환자들은 옆에 핫팩을 두어 자주 마우스에서 손을 떼어야 하지만 ATP MOUSE 사용시 온도를 측정하여 자동으로 적정한 온도를 작동시킨다.


작품판넬
![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/7d7e4ff6-2031-4732-b18c-e99ef37dc098)


[안드로이드 화면]

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/e754567d-e712-440c-af2d-dcffab1a9944)

어플 시작화면

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/3d1d78b1-5afe-4bc9-a0ca-eaa6fbf36a99)

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/c569ccdc-c533-4d78-97cf-0336dcbccfba)

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/adcf9636-2192-4bce-b621-d411d9bdce80)

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/3c8fd397-0190-4a04-b237-ab52db1503ad)

비밀번호 찾기를하면 메일로 비밀번호를 바꿀수있는 링크를 보내줌 

<img width="1265" alt="image" src="https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/4aba4be1-0792-4882-9b4c-a9cedc60b5e6">

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/6d90026f-ebb0-48b1-8c85-c64a3dcfd912)

센서값 리스트

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/8718589d-c7db-47a6-a68d-ff29b783edd6)

![image](https://github.com/namgyeonghyeon/ATP_MOUSE/assets/129054045/f3ce1587-f4f1-4ba8-a516-77d3c92468cd)

센서값 리스트와 각각 Bpm값과 Temp값이 제품을 통해 데이터를 전달받게 되면 실시간으로 파이썬에서 firebase로 데이터가 넘어간후 어플로 다시 전달받게된다


