# NewsingIT_Project

-----------------------------------------
< Project status (Waterfall model) >

* 전체 진행률 : 70%
* 프로젝트 기간 : 08/23 ~ 09/22 
<p>

</p>
* 요구 / 분석 : (완료)
* 설계 / 구현 : (부분완료)
* 테스팅 : (미흡)
* 유지보수 : (시행되지 않음)

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
* compile('com.twitter.sdk.android:twitter:1.14.1@aar') { transitive = true; }
* compile 'com.android.support:multidex:1.0.1'
* compile 'com.jakewharton.picasso:picasso2-okhttp3-downloader:1.0.2'
* compile 'com.google.android.gms:play-services-gcm:9.4.0'
* (새로운 라이브러리 추가 시 계속 업데이트)

< Algorithm image >

* AutoLogin

<p align="left">
  <img src="https://github.com/seochangwook/NewsingIT_Project/blob/master/autologin_1.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/NewsingIT_Project/blob/master/autologin_2.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/NewsingIT_Project/blob/master/autologin_3.png" width="650" height="350">
</p>

* FCM

<p align="left">
  <img src="https://github.com/seochangwook/NewsingIT_Project/blob/master/cloudmessage.png" width="650" height="350">
</p>

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
* Favorite count 증가 관련 POST test 완료 - 서창욱 (16/09/01 13:16)
* 팔로워,팔로잉,유저 검색 목록에서의 팔로우 생성/해제 test - 임지수 (16/09/01 15:35)
* CreateFolder 부분 MultipartBody적용하여 File전송 POST test 완료 - 서창욱 (16/09/01 15:54)
* 스크랩 삭제 dummyData test - 임지수 (16/09/01 17:23)
* 스크랩 수정 dummy test / 메인 화면, 마이페이지 ui 수정 - 임지수 (16/09/01 20:23)
* CreateScrap부분 MultipartBody를 적용하여 POST test 완료 - 서창욱 (16/09/01 20:50)
* 각 RecyclerView에 EmptyView설정 완료 - 서창욱 (16/09/02 02:05)
* 아이콘 적용 - 임지수 (16/09/02 13:01)
* 검색에 SearchView 적용 - 임지수 (16/09/02 17:47)
* EditScarp부분 Dummy Data test 완료, PUT test 완료(사용자 입력에 대한 완벽한 예외처리 생략) - 서창욱 (16/09/02 20:50)
* Search부분에서 Tag검색 2단계 GET요청 DummyData test 완료 - 서창욱 (16/09/03 00:49)
* Server Domain 주소 변경에 따른 수정작업 진행 (strings파일에 적용) - 서창욱 (16/09/04 00:48)
* 각 Alarm(새 스크랩/내 스크랩/마이 페이지)기능 PUT test완료 - 서창욱 (16/09/04 03:07)
* ScrapList출력 시 사용자와 나를 구분하고, locked로 유효한 데이터만 저장 test완료 - 서창욱 (16/09/04 12:24)
* 현 앱에서 필요한 icon, font등을 수정 (작업 중) - 서창욱, 임지수 (16/09/04 23:55)
* 페이스북 로그아웃/ 폴더 dialog의 폴더 리스트가 안 뜨는 문제 해결 - 임지수 (16/09/05 16:03)
* AutoLogin구현(Https), PropertyManager(SharedPreference) - 서창욱 (16/09/05 19:02) 
* autologin 관련 이중 세션 적용 issue 해결 -> 이중 세션이 일어나면 로그인 버튼이 동작을 하지 않게되므로 사용자가 로그인을 할 필요가 있을 경우 이전에 로그인 되어 있던 상태를 logout을 하고 저장된 SharedPreference의 정보도 초기화한다. 이로서 기존 세션은 제거되고, 새롭게 로그인을 하여서 HTTP 401에러를 방지할 수 있다. - 서창욱 (16/09/06 00:28)
* 검색 기능 더미 데이터로 test 완료 -> view pager의 갱신을 위해서는 반드시 FragmentPagerAdapter의 getItemPosition 메소드를 override 해주어야 한다. - 임지수 (16/09/06 11:43)
* 검색 기능 refresh 추가 - 임지수 (16/09/06 15:35)
* 알람 목록, 스크랩 목록, 뉴스 검색 목록에 디자인 가이드 적용 - 임지수 (16/09/06 18:17)
* MyInfopage image download Picasso issue solution -> https://github.com/JakeWharton/picasso2-okhttp3-downloader
* CreateFolder / EditFolder / EditMyInfo real server test complete - 서창욱 (16/09/06 18:23)
* drawer 메뉴 디자인 가이드 적용 / recycler view 구분선 적용 - 임지수 (16/09/06 20:33)
* autologin <-> FCM id 간 Merge, Receiver로 fcm id를 먼저 획득 후 로그인 과정을 진행 - 서창욱 (16/09/07 09:51)
* 태그검색,유저검색,팔로잉/팔로워 목록,스크랩 상세화면 디자인 가이드 적용 - 임지수 (16/09/07 18:03)
* 마이페이지 수정화면,폴더 수정화면 디자인 가이드 적용 - 임지수 (16/09/07 19:44)
* 구분선을 line 이미지로 적용 - 임지수 (16/09/07 20:51)
* CreateScrap / EditScrap real server test complete (RequestBody를 FormBody형식으로 다중 바디구조로 적용) - 서창욱 (16/09/08 09:24)
* SearchUser간 int 범위를 초과한 값을 가지고 GSON Parsing issue -> gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING); 방법으로 String으로 캐스팅 해주는 Builder를 만들어 해결 - 서창욱 (16/09/09 01:37)
* User 검색 후 Info정보 / 폴더 리스트 / 스크랩 리스트 real server test complete - 서창욱 (16/09/09 01:37)
* Scrap Edit / Create시 Tag 중복 등록방지 적용 - 서창욱 (16/09/09 08:51)


