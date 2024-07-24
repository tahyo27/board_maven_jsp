# 만들면서 생각

Request 받는 객체를 insert update 전부 나눠놨는데 중복되는 부분이 많고 다른것들이 추가될때마다
쓸데없는 코드가 많아진다고 생각 Request를 하나로 묶고 convert 할때 나눠보는걸로 한번 해보자

- 예외처리
-> @NotBank 같은것 넣을려고 할때 불편함 생김 -> 유효체크 클래스 만듦
-> 리플렉션을 활용해 모든 필드 서치하는 식으로 구현했더니 해당 로직에 필요없는 값까지 넣어줘야하는 문제 생김 (Request 객체를를 통합해서 발생)
-> 가변인자로 검증할 필드값 넣어주고 처리하기로 결정

- 이미지처리
무료 클라우드라 저장공간이 작아서 다른 이미지 저장소 필요 -> 구글 결정
이미지 이름 DB에 저장시 UUID와 원본이름 어떤식으로 할지 생각
-> DB 이미지 이름으로 검색하는 경우가 없을거라 생각해 중간에 _를 넣어 같이 저장후 사용시 스플릿해서 일단 사용해보기로
-> 한 게시판에 이미지 여러개 넣는 경우 생각해서 이미지 테이블 따로 만들어서 관리하기로 변경
-이미지 삽입시 배치 현재 foreach사용 성능 개선 생각해봐야할듯
-toast ui 사용해서 이미지 url 받아야하는데 서버쪽에 임시 저장 디렉토리 만들어서 관리 후 글 작성하면 스토리지에 올리고 임시 저장소 관리 고민
->임시로 저장할 로컬 temp 만들고 에디터에 이미지 url줘서 이미지 나오게 함
->content의 텍스트 넘어올떄 이미지 파싱해서 바꿔야 하는데 어떻게 할지 생각중
->찾아보니 html 파싱해주는 라이브러리 존재해서 쓰면 될듯
->이미지 저장시 저장유형 설정하기
->속도 올리는방법 없나 고민하기