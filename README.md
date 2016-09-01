# NewsingIT_Project

* Push ex) [NewsingIT Project 2016/08/23 11:20]

-----------------------------------------
< Android Using Library list (updating...) >

* com.github.iwgang:familiarrecyclerview:1.3.0
* com.yqritc:recyclerview-flexibledivider:1.4.0
* compile 'jp.wasabeef:picasso-transformations:2.1.0'
* compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.4.1'
* compile 'com.github.bumptech.glide:glide:3.7.0'
* compile 'com.android.support:cardview-v7:24.2.0'
* compile 'com.android.support:design:24.2.0'
* compile 'com.android.support:support-v4:24.2.0'
* compile 'com.android.support:appcompat-v7:22.2.1' //TextInputLayout 관련 라이브러리//
* compile 'com.squareup.okhttp3:okhttp:3.4.1'
* compile 'com.github.franmontiel:PersistentCookieJar:v1.0.0'
* compile 'com.facebook.android:facebook-android-sdk:4.8.2'
* compile 'com.android.support:multidex:1.0.1'
* (새로운 라이브러리 추가 시 계속 업데이트)


< Update Content >

* 플로팅 버튼 제거 - 임지수 (16/08/23 11:20)
* 패키지 폴더 추가 - 서창욱 (16/08/23 11:47)
* 패키지 더미파일 추가 - 서창욱 (16/08/23 11:53)
* MainActivity / BottomMenu 추가 - 서창욱 (16/08/23 16:10)
* SplashActivity / LoginActivity / KeywordListActivity 추가 - 임지수 (16/08/23 16:27)
* DrawerActivity / EditMyInfoActivity /AlarmListActivity 추가 - 임지수 (16/0824 10:26)
* DrawerActivity의 ExpandableListView Group Indicator customizing - 임지수 (16/08/24 11:14)
* FollowerActivity / FollowingActivity 추가 - 임지수(16/08/24 15:45)
* NoticeActivity 추가 - 임지수 (16/08/24 17:33)
* SelectNewsDetailActivity / EditScrapContentActivity / UserSelectScrapContentActivity 추가 - 임지수 (16/08/24 19:44)
* UI시나리오 테스트를 위한 전반적인 구성 - 서창욱 (16/08/25 00:43)
* CreateScrapContentActivity / MainNews - RecyclerView 추가 / 기타 화면 Navigation 구성 - 임지수 (16/08/25 13:06)
* 검색 fragment에 recyclerview 추가 - 16/08/25 15:57
* PopupWindow 화면 구성 / ScrapFolderListDialog 구성 / 전반적인 UI Navigation 정리 - 서창욱 (16/08/26 02:18)
* SearchTabActivity 검색 EditText 추가 - 임지수(16/08/26 17:53)
* MainNewsList 화면 구성 - RecyclerView / MultiListAdapter (작업 중 - Recyclerview안에 fragment구성) - 서창욱 (16/08/27 23:20)
* MainNewsList 화면구성 사항 - 서창욱 (16/08/29 00:52)
1. 리스트가 아래로 스크롤 시 상단으로 이동하기 버튼 구현 - 서창욱 (16/08/29 00:52)
2. RecyclerView 내부에 fragment issue -> '<include>'방식으로 해결 -> 터치 시 중복발생 issue 존재 - 서창욱 (16/08/29 00:52)
3. 상단으로 이동하기 버튼(FloatingActionButton) 클릭 시 리스트 상단으로 이동 - 서창욱 (16/08/29 00:52)
4. 리스트 내용 선택 시 해당 뉴스의 id값과 title을 가져오는 기능 구현 - 서창욱 (16/08/29 00:52)
5. 사용자가 키워드리스트에서 키워드 선택 시 메인뉴스 리스트에서 해당 키워드가 존재하는 위치로 이동하는 기능 구현 - 서창욱 (16/08/29 00:52)
* MainNewsList에서 RecyclerView 내부에 FrameLayout터치 중복 issue -> Adapter에 onCreateViewHolder에서 view를 inflate하고 해당 자원을 가지고 이벤트 작업 방식으로 issue 해결 - 서창욱 (16/08/29 10:13)
* ListView에 트위터 가져오기 테스트 완료- 임지수 (16/08/29 15:47)
* SwipeLayout을 적용해서 트위터 내용 갱신 기능 완료 - 서창욱 (16/08/29 17:55)
* OkHttp 라이브러리를 적용하여 KeywordList데이터 불러오기 - 서창욱 (16/08/29 23:02)
* 상대방 스크랩에서만 좋아요 활성화/ 검색 edittext color값 수정/ scrap 생성,수정 edittext 글자수 표기/ 기타 view들 custom - 임지수 (16/08/30 14:51)
* MainNewsList Okhttp를 이용한 getData()부분 기능 구현 완료 - 서창욱 (16/08/30 15:08)
* facebook login/ EditFolderActivity 추가/ 폴더 long click시 폴더 수정 화면으로 이동/ 자물쇠 이미지(버튼) 클릭 시 사라짐 - 임지수 (16/08/30 19:51)
* ScrapCreate / ScrapActivity UI fix - 서창욱 (16/08/30 20:28)
* ScrapContentEditDialog의 ExpandableListView 수정, 기타 전반적인 UI구조 수정 - 서창욱 (16/08/31 01:26)
* NewsDetailActivity / UserInfoActivity / SearchUser 부분 DummyData test 완료 - 서창욱 (16/08/31 15:30)
* SearchTab/scrap목록 네트워크 DummyData test - 임지수 (16/08/31 15:33)
* ScrapFolderList ExpandableList DummyData test 완료 - 서창욱 (16/08/31 18:25)
* Follower/Following 목록, scrap 상세 화면 DummyData test - 임지수 (16/08/31 18:30)
* MyInfoFragment / AlarmListActivity 등 DummyData test 완료 및 현 프로젝트 GET방식 점검 - 서창욱 (16/09/01 00:25)
* dexOutOfMemory방지 라이브러리 추가로 컴파일 issue 해결 - 서창욱 (16/09/01 00:25)
* 로그아웃 dummy test, 스크랩 수정 dialog, follower/following id값 intent로 넘김 - 임지수 (16/09/01 10:46)
* folder 잠금일 때 자물쇠 이미지 보이기 - 임지수 (16/09/01 11:17)

