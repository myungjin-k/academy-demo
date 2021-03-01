## ACADEMY-DEMO : KH Academy Final Project - Reboot!

💡 기존 Final Project 와 달라진 점
 * 프레임워크 : Spring 3.1.1 -> Spring Boot 2.3.6
 * Java 버전 : JDK 1.8 -> JDK 11
 * DB 연동방식 : JDBC + MyBatis -> JPA
 * DB : Oracle -> H2(로컬서버) / MariaDB(실서버) 
 * Junit 5 단위 테스트 작성
 * 템플릿 엔진 : JSP -> mustache
 * 개발/실서버 profile 설정 및 aws 배포
 * Git으로 소스 관리 
   * ~~지난 과제 당시에는 카톡으로 코드를 합쳤다...~~ 


### 2020.11.20
💡 의존성 추가
 * JPA, Web, Undertow, Test, H2 database
 * spring-boot-configuration-processor
   * IntelliJ가 application.yml의 내용을 가이드할 수 있게 한다.
 * Lombok
💡 Resource Handler 설정
💡 프론트 리소스 라이브러리 추가
 * Bootstrap, JQuery
💡 application.yml 작성
 * 포트 설정

### 2020.11.21
💡 Data Source 적용
 * H2 Driver 설정, enable console 
 * JPA 설정 : ddl, open-in-view, show-sql
 * schema.sql, data.sql 작성

💡 공통코드 도메인 작성
 * CodeGroup, CommonCode Entity + Repository
 * 메뉴 카테고리 등의 공통코드를 정의하는 엔티티
 * 부모 코드를 CodeGroup에, 자식 코드를 CommonCode에 저장하여 매핑한다.

💡 Test Data Source와 Logging 설정
 * HikariCP
 * Logback.xml

💡 샘플용 서비스와 테스트 작성
 * CodeGroup CRUD 구현
💡 스키마 변경
 * common_code table foreign key : on delete restrict -> cascade  
💡 샘플용 컨트롤러 작성
💡 프론트 레이아웃 작성
 * mustache 의존성 추가
 * header / footer 
💡 Resource Location 변경
 * classpath:/static/ -> classpath:/templates/
 * mustache, thymeleaf 등 템플릿들은 /templates 에 둬야 인식하는 듯 하다. (?)

### 2020.11.22
💡 Id Class 작성
 * 엔티티들의 Id가 모두 UUID String으로 작성되어 구분하기 위해!
💡 CommonCode 서비스 구현 및 테스트
💡 CommonCode 컨트롤러 및 화면 구현

### 2020.11.23
💡 Response, Error Class 작성
 * 컨트롤러 응답
💡 General ExceptionHandler 작성
 * 특정 Exception에 대해 HTTP Response 정의

💡 CodeGroup 삭제 시 cascade 안 되는 오류 수정
 * CodeGroup codes 필드 @OneToMany 에 cascade 설정 추가

### 2020.11.24
💡 Member 도메인 및 서비스 작성
 * 가입된 회원정보 엔티티
 * 가입 기능 구현
 
💡 Spring Security 의존성 추가 및 기본 설정
 * in memory authentication으로 설정(임시)
 * ignoring uri path 설정
 * authoriaztion : any request authenticated 로 설정(임시)
 * httpBasic() 설정(임시) 
   * cookie, session, login page 등이 없음
   * credentials가 암호화되지 않음

💡 Role Enum 작성
 * 회원/관리자 구분

💡 회원 DB 저장 시 패스워드 암호화 : PasswordEncoder

### 2020.11.25
💡 Member Entity Validator 추가
 * @Size, @Pattern
💡 Member Service Validation 추가
 * @Valid

💡 Authentication
 * Custom Authentication Provider 작성
 * api/auth uri에서 AuthenticationRequest 로 principal(아이디)과 credential(비밀번호) 받음 -> MyAuthenticationToken으로 변환 (principal, credential, role)-> MyAuthenticationProvider에서 MemberService에 접근하여 해당 auth 정보를 검증하고,  set authenticated detail
 * auth request uri path 설정
 * session management 설정
 * disable csrf, header : 개발용

### 2020.11.26
💡 공통코드 화면 탭 이벤트 수정
 * 탭 클릭 시 현재 탭만 활성화 : bootstrap 자체 클래스 이용

💡 기본 로그인 Role 을 어드민으로 설정(임시)
💡 authorization request uri 설정 변경
 * 어드민단 uri는 어드민만, auth 페이지는 누구나, 회원관련 uri는 인증된 요청만 접근가능 
💡 로그인 Interceptor 작성
 * 인증된 세션 정보가 없을 경우 로그인 페이지로 redirect 

💡 가입 컨트롤러 및 화면단 구현 

### 2020.11.27
💡 어드민/일반사용자 로그인 구분
 * Admin 도메인 및 가입 서비스 구현
 * AuthenticationProvider에서 Role로 분기하여 각 서비스에서 데이터 검증, 완료된 데이터를 User class로 받아 auth detail로 set

💡 javax.servlet.ServletException: Circular view path [auth] 오류 해결
 * 로그인 인증 시 발생
 * controller에서 return 값을 view name으로 인식
 * @RestController 추가로 해결

### 2020.11.28
💡 Member ID/PW 찾기 서비스 구현 및 테스트
 * PW 확인 위해 Member 엔티티에 email 필드 추가
 * PW 찾기(변경) : 변경 페이지 링크를 메일로 발송
 * 메일 설정
   * 메일 의존성 추가
   * Mail 클래스로 제목, 발신이메일 주소, 내용을 받아 MimeMessage로 변환, javaMailSender의 sendMail 메소드로 발송 
   * spring.mail application property 설정
     * smtp host, port, username, password
     * properties.mail.smtp.starttls.enable: true
     * properties.mail.smtp.auth: true
   * @ConfigurationProperties 로 property 불러오려 했으나 실패.. 지역변수 선언하여 해결  


### 2020.11.29
💡 Member ID/PW 찾기 화면 구현
 * template, script

### 2020.11.30
💡 Member ID/PW 찾기 화면 구현
 * css, 메일 발송 후 spinner 추가

💡 상품 카테고리 가져오기 구현
 * 캐싱 적용 시도... 

### 2020.12.01
💡 Item 도메인 작성
 * ItemMaster : 상품 마스터 / ItemOption : 각 상품의 옵션(색상, 사이즈) / ItemDisplay : 상품 마스터별 상세페이지 전시 정보

💡 엔티티 필드 updateAt -> nullable로 변경
 * 수정일시
 * null 로 insert 하고 데이터가 업데이트되면 시각 입력하도록 

### 2020.12.02
💡 Admin, Member : password를 toString에서 제외
💡 Member 회원정보 조회/수정 서비스 및 테스트 구현
💡 카테고리별 상품마스터 조회 서비스 및 테스트 구현
💡 코드그룹 검색 서비스 및 테스트 구현
 * 조회 파라미터를 유동적으로 넘기기 위해 다이나믹 쿼리 필요
 * QueryDsl 의존성 추가

💡 Service Exception 생성   
 * ServiceRuntimeException, NotFoundException, UnauthorizedException
 * 예외메세지 출력 위한 MessageSource 설정 
 * GeneralExceptionHandler에 해당 예외 처리로직 추가

💡 로그인 인증 시 Exception 처리 추가

### 2020.12.03
💡 아이템 마스터 등록 서비스, 컨트롤러, 화면 작성
 * 상품 이미지 등록 위한 AWS S3 설정 
   * AWS 의존성 추가
   * accessKey, url, bucketName, region 정보를 property로 설정하여 AwsConfigure class로 받음
   * 업로드, 삭제를 위한 S3Client class 작성, ServiceConfigure에 빈으로 등록
 * MultipartFile 처리 위한 AttachedFile class 작성

### 2020.12.04
💡 어드민/일반사용자 포트 분리 (?)

### 2020.12.06
💡 공통코드 리스트 페이징
💡 공통코드 리스트 반환 방식 변경
 * 코드그룹 자체를 반환하여 프론트에서 공통코드 리스트 get -> 공통코드 리스트만 반환

💡 로그인한 상태에서 /login 호출하면? 
 * 애초에 로그인한 상태에서 로그인 화면이 보이면 안 되지만...
 * 만약 그렇다면 홈으로 redirect 하도록

### 2020.12.07
💡 기존 상품 컨트롤러 -> 쇼핑몰 상품전시와 어드민 상품관리 컨트롤러로 분리
💡 아이템 마스터, 공통코드 @OneToMany, @ManyToOne 관계 설정 중 StackOverflow 발생
 * @OneToMany 필드에 @JsonIgnore 추가로 해결

### 2020.12.08
💡 상품 마스터 수정, 삭제 서비스 구현 및 테스트
 * 디폴트 옵션이 무조건 등록되게끔 했는데(추후에 수정 가능하도록) 마스터 등록 시에 옵션까지 같이 등록하는 게 맞는 지 고민 필요..

### 2020.12.09
💡 상품 전시 서비스 구현 및 테스트

### 2020.12.10
💡 상품 마스터 등록 화면 구현
💡 프론트 pagination 이벤트 및 css 수정
💡 주소찾기 api 추가

💡 상품 마스터 검색 서비스 구현 및 테스트
 * repository가 QuerydslPredicateExecutor 상속, Predicate 클래스 생성하여 다이나믹 쿼리 구현

💡 상품 마스터 수정, 삭제, 검색 컨트롤러 작성
💡 상품 옵션 CRUD 컨트롤러 작성
💡 상품 전시 CRUD 컨트롤러 작성


### 2020.12.11
💡 Cart 테이블 스키마 작성

### 2020.12.14
💡 Swagger 2 적용
 * swagger 의존성 추가
 * Swagger2Configure class 생성
 * 인증 403오류 ? WebSecurityConfigure 에서 ignore할 uri에 swagger 추가
💡 Swagger 2 관련 Annotation 추가 및 value 작성
💡 /api uri 구분 : rest api uri
💡 아이템 카테고리 검색 구현(모달)
💡 쇼핑몰/어드민 분리 (?)

### 2020.12.15
💡 Swagger 2 테스트
 * 상품마스터 검색 파라미터 중 날짜 형식(등록일)이 있음
    * String -> LocalDate 변환 위한 Formatter 추가
    * swagger model substitute 설정
    * ItemMaster Predicate에서 LocalDate로 받은 데이터를 LocalDateTime으로 변환

💡 uri 정리 : 어드민 관련 (/admin), 쇼핑몰 관련(/mall)
💡 전시상품 검색 서비스 구현 및 테스트

### 2020.12.16
💡 전시상품 옵션을 전시상품 클래스와 함께 작성
 * 전시상태는 ItemStatus Enum으로 관리
  💡 Lazy Initialization Exception !
 * @JsonIgnore 로 해결

### 2020.12.17
💡 Cart 도메인, CRUD 서비스, 컨트롤러 작성 및 테스트
 * 장바구니
💡 Swagger 에서 로그인 인증도 할 수 있도록 apiKey 및 securitySchemes 설정

### 2020.12.22
💡 Order, OrderItem, Delivery 도메인 작성
  * 주문 마스터, 주문 상품, 배송 마스터
  * 배송상태는 DeliveryStatus Enum으로 관리, 

💡 주문 생성 서비스, 컨트롤러 구현 및 테스트

### 2020.12.23
💡 주문 조회, 수정 서비스, 컨트롤러 구현 및 테스트
💡 배송상태 Exception 작성

### 2020.12.24
💡 Review 도메인 작성
  * 주문상품 리뷰

### 2020.12.25 ~ 26
💡 DeliveryItem 도메인 및 서비스 작성, 테스트
 * 배송정보 생성, 수정 / 배송아이템 추가, 삭제, 수량변경 / 배송상태 변경
### 2020.12.27
💡 리뷰 서비스 구현 및 테스트

### 2020.12.28 ~ 29
💡 배송 컨트롤러 작성
💡 리뷰 컨트롤러 작성

### 2020.12.30
💡 주문, 배송 서비스관련 수정
 * CartItem - item ManyToOne으로
 * Delivery - item Eager fetch, json ignore 해제
   * item을 항상 같이 조회 필요
 * DeliveryItem - 상품정보 같이 받아오도록(JsonBackRefrence 해제)
 * DeliveryController, DeliveryService - orderItems 조회 서비스 추가
 * DeliveryRepo - 주문별 배송정보 조회 시 배송취소건 제외하도록
 * DeliveryRequest - orderItem 엔티티 아닌 Id로 받도록
 * Order - OrderItem 필드 제외
 * OrderItem -> Order 단방향으로
 * Orderitem을 항상 같이 조회할 필요가 없다
   * OrderItem을 조회할 시에는 Order을 같이 받아오도록 (JsonBackRefrence 해제)
   * deliveries는 Lazy fetch로

💡 주문, 배송 서비스관련 재수정
 * DeliveryController
   * 배송상품을 따로 조회할 일이 없음. 항상 Delivery에 붙어서 가져오기 때문에
 * DeliveryRepository
   * 불필요한 메소드 제거
 * Order
   * deliveries 필드를 다시 json ignore 해제...
   * 주문에서 배송상태를 보여줘야 함
 * OrderService
   * 주문상품 조회 시 Order 가져오면서 deliveries lazy init 예외 해결


### 2020.01.01
💡 주문 포인트 적립/사용 구현
 * 포인트 사용시 주문 적립 안 됨
 * 회원별 총주문금액 및 회원등급 업데이트

### 2020.01.04
💡 ItemDisplay - Option
 *  양방향 순환 참조 해결 : @JsonManagedReference
 * Response DTO 생성
💡 ItemDisplay - Option

### 2020.01.05
💡 회원등급 정보, 주문상세 조회 구현 및 테스트
💡 property 설정
 * jpa 쿼리 출력 시 formatting
 * 메세지 설정
 * 로깅 파라미터 출력
* mail, cloud(aws) profile 분리 및 적용
💡 IdGenerator 생성 : ID(UUID String) 매번 set하지 않고 insert 전에 자동 생성

### 2020.01.06
💡 aws ec2 인스턴스, aws rds (mariaDB) 생성
💡 real profile 설정

### 2020.01.07
💡 travis 연동
💡 AWS CodeDeploy 연동
💡 배포 연습!

### 2020.01.08
💡 Dependency Cycle 오류
 * 순환 참조 ?
 * Order/Member Controller에서 @AuthenticationPrincipal 로 인증정보에 접근하고 있고, 
   그 인증정보를 생성하기 위해서는 MemberService를 주입받아야 한다. (MyAuthenticationProvider)
   그런데 Order/Member Controller 에서 또다시 MemberService를 생성자로 주입받고 있어서 Cycle이 발생한 듯..?
 * [Circular Dependencies in Spring](https://www.baeldung.com/circular-dependencies-in-spring)
 * @Lazy를 넣어서 해결했다... (임시방편 ?)

💡 관리자 페이지 접근 제어방법 변경
 * 접속 서버 포트 -> 접속 IP
 * 포트 나누는 것에 의문이 생겼다... 한 서버에서 두 포트를 사용하는 게 맞나? 포트를 나누려면 서버 인스턴스를 하나 더 생성해야 하는 것이 아닌가?

💡 AuthenticationInterceptor 분기 변경
 * 관리자페이지 && 지정 IP 아닐 경우 접근 불가, 쇼핑몰 홈으로
 * 로그인 페이지 요청이면 그대로 리턴
 * 관리자페이지 && 로그인정보 없으면 관리자 로그인 페이지로
 * 쇼핑몰페이지 && 로그인정보 없으면 쇼핑몰 로그인 페이지로

💡 uri path 및 접근권한 변경
 * 회원가입/아이디,비번찾기/상품조회 는 누구나 접근 가능
 * 인터셉터에서는 뷰만 확인하도록
 * 비번찾기 메일 링크 변경


### 2020.01.12
💡 어드민 상품 마스터 & 옵션 관리 화면 구현

### 2020.01.14 - 15
💡 어드민 상품 옵션 멀티등록 기능 구현

### 2020.01.15
💡 어드민 상품 전시 화면 구현

### 2020.01.16
💡 favicon 설정

### 2020.01.17
💡 클릭 이벤트 중복 ?
 * unbind().bind()
 * off().on()

💡 상품마스터 foreign key null 오류
 * 상품마스터 등록 시 카테고리 id가 null로 삽입되는 현상
 * 로컬에서는 정상적인데, aws에서만 오류
 * sql이나 db 오류인 듯 해서 rds db를 싹 밀고 테이블과 데이터를 다시 만드니 해결되었다..

### 2020.01.18
💡 전시상품 옵션 서비스 구현 및 테스트, 화면구현
💡 코드그룹 검색, 상품마스터 검색 화면 구현

### 2020.01.20
💡 s3 디렉토리 server profile 별로 구분
💡 쇼핑몰 전시상품 목록 화면 구현
💡 쇼핑몰 전시상품 상세 화면 레이아웃 구성
💡 쇼핑몰 페이지에서는 model attribute에 일반사용자의 로그인유저 정보만 저장

### 2020.01.21
💡 쇼핑몰 전시상품 상세 화면 구현

### 2020.01.24
💡 로그인 후 redirect ?

### 2020.01.25 ~ 26
💡 장바구니 화면 구현

### 2020.01.26 ~ 27
💡 주문 생성 및 조회 화면 구현


### 2020.01.28
💡 Mail Service 수정
 * SocketException : connection reset
 * Session을 생성하여 Transport로 메일 전송


### 2020.01.29 ~ 30
💡 주문 조회목록, 상세 화면 구현

### 2020.01.31 ~ 02.01
💡 어드민 주문관리 화면 구현
 * 주문 조회
 * 배송정보 조회
 * 배송정보 추가, 취소, 배송상태 변경 및 송장번호 입력

### 2020.02.02 ~ 04
💡 어드민 주문관리 화면 구현
 * 배송상품 추가, 삭제, 수량변경

### 2020.02.04
💡 쇼핑몰 주문 상세에서 배송정보 변경 기능 구현


### 2020.02.06 ~ 07
💡 리뷰 작성 및 조회 화면 구현

### 2020.02.08 ~ 10
💡 화면 버그 수정
💡 상품 상세 - 리뷰목록 화면 구현

### 2020.02.11
💡 리뷰 코멘트 도메인, 서비스 생성 및 테스트

### 2020.02.12
💡 어드민 리뷰 관리 목록 화면 구현
💡 어드민 주문 수기등록 화면 구현
💡 어드민 주문 복사 화면 구현
### 2020.02.13
💡 어드민 리뷰 관리 - 리뷰 상세 화면 구현
 * 리뷰 적립금 지급
 * 리뷰 코멘트 작성, 삭제
### 2020.02.15
💡 결제 라이브러리 추가
 * I'amport

### 2020.02.16
💡 주문완료 메일 발송 설정
 * JavaMailSender와 MimeMessage로 회귀...
 * thymeleaf 사용, 메일 템플릿 작성은 보류...

💡 결제api 테스트
💡 주문취소 서비스 구현

### 2020.02.17
💡 주문완료 메일 발송 서비스 적용
💡 파일업로더 라이브러리만 추가.. ?
 * 리뷰 이미지 멀티 업로드를 위해 추가했으나.. 다시 생각해보니 굳이? 이걸 추가함으로써 DB 스키마 갈아엎는 비용이 더 클 것 같다..
💡 주문생성, 주문취소 결제정보 로드 테스트 및 버그수정

### 2020.02.18
💡 배송비 부과 / 적립금 적립&사용&복구 로직 추가

### 2020.02.19 ~ 20
💡 배송상태 변경 배치 업데이트 구현 및 테스트
 * Spring Boot Batch 의존성 추가
 * DeliveryStatusJobConfigure class 추가
   * job, step (ItemReader, ItemProcssor, ItemWriter)
 * JobScheduler class 생성
   * @Scheduled

### 2020.02.21 ~ 22
💡 Top Seller 배치 구현 및 테스트
 * TopSellerJobConfigure class 생성
💡 Job Parameter 생성을 위한 CreateJobParameter 생성 및 JobConfigure에 주입
 * 조회할 배송정보의 생성일시
### 2020.02.23
💡 두 개 이상의 CreateJobParameter bean 생성 시 NoQualified Bean(spring-expected-single-matching-bean-but-found-2) 오류 발생
 * @Qualifier 추가로 해결

### 2020.02.23 ~ 24
💡 JobParameter NullPointerException
 * JobScheduler에서 JobParameter를 넘기기 전에 (서버 startup 시) CreateJobParameter가 호출되어서 parameter가 null임... Nullable로 받고 처리했는데 적절하지 않은 듯
 * Job Flow 설정
   * parameter null일 경우 job exit
 * 깔끔한 해결책 : application property에서 spring.batch.job.enabled=false 설정하면 startup 시 Job 실행하지 않는다!
💡 Test DB pool size 변경
 * 배치 작업 때문에 1로 했더니 오류가 난다..


### 2020.02.25
💡 배송상태 Job Parameter 조정
 * JobInstance already exists and is not restartable 메세지가 뜨며 실행이 안 됐다
 * 똑같은 Job Parameter를 계속 보내고 있어서 동일한 JobInstance로 인식한 것

💡 적립금 히스토리 도메인 , 서비스 생성 및 테스트

### 2020.02.26
💡 어드민 리뷰관리 - 검색 서비스 및 화면 구현
💡 리뷰, 전시상품 기능을 어드민 서비스로 분리
💡 어드민 회원관리 서비스 구현

### 2020.02.27
💡 어드민 회원관리 화면 구현
💡 적립금 히스토리 관련 마이페이지 조회 버그 수정
