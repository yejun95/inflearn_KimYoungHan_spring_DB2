# 김영한님의 spring DB 2편 강의 정리
<br>
<hr>
<br>

## ✔️ itemservice-db directory
### 학습 범위 : 7-1-1 - 7-1-7
- 프로젝트 구조 설명

- h2 테이블 생성
<br>
<br>

### 학습 범위 : 7-2-1 - 7-2-10
- JdbcTemplate 적용

- 동적 쿼리

- 이름 지정 파라미터 : NamedParameterJdbcTemplate
  - SqlParameterSource
  - BeanPropertySqlParameterSource
  - MapSqlParameterSource
  - Map
<br>

- SimpleJdbcInsert : insert를 쉽게 가능
<br>
<br>

### 학습 범위 : 7-3-1 - 7-3-7
- 테스트 시 트랜잭션을 활용한 데이터 롤백
  - PlatformTransactionManager
  - @Transactional
  - 원하는 테스트에 @Commit을 붙이면 트랜잭션이 적용되나 롤백이 되지 않음 -> db 실 확인 필요할 때
<br>

- 임베디드 모드 DB
  - H2는 JVM 메모리에 포함되어 실행 가능 -> 자바 메모리를 함께 사용하는 라이브러리처럼 동작
  - 애플리케이션 종료 시 데이터 자동으로 날아감
  - test 시 만약 db 설정 정보가 없다면 자동으로 임베디드 모드 실행 -> 단, test/resources/schema.sql에 테이블 create 쿼리 필요
<br>
<br>

### 학습 범위 : 7-4-1 - 7-4-8 
- MyBatis
  - interface로 생성 -> @Mapper annotation 필요

- xml 특수문자
  - '<' : `&lt;`
  - '>' : `&gt;`
  - '&' : `&amp;`

- 동적 쿼리
  - if
  - choose (when, otherwise)
  - trim (where, set)
  - foreach
 
- Mapper에서 annotaion으로 SQL 작성 가능
```java
@Select("select * from item where id =#{id})
Optional<Item> findById(Long id);
```

- 쿼리에 `${}` 사용 시 문자열 직접 넣기 가능 -> SQL Injection 공격 가능

- `<sql></sql> 코드 사용시 `<include></include` 태그로 재사용 가능

- `<resultMap></resultMap>` db 컬럼과 객체 프로퍼티 명이 상이해서 `as` 쓰는 대신 사용 가능
<br>
<br>

### 학습 범위 : 7-5-1 - 7-5-8
- JPA (Java-Persistence Api)

- ORM (Object-Relational Mapping)

- SQL 중심적인 개발에서 객체 중심으로 개발

- Annotation
  - `@Entity` : 객체를 JPA로 등록
  - `@Table(name = "item")` 객체명 = 테이블 명이 같으면 생략 가능
  - `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` = auto-increment
  - @Column(name = "item_name", length = 10) : db 컬럼명과 자바 객체명이 상이할 시 사용 -> 스네이크 -> 카멜로 자동 변환되기 때문에 없어도 무방
  
- EntityManager : 의존성 주입 필수

- JPQL : 엔티티 객체를 대상으로 SQL 실행 -> 동적 쿼리 문제가 JdbcTemplate 처럼 발생 -> Querydsl 기술 활용 필요

- 예외 변환 : `@Repository`가 붙으면 예외 변환 AOP 적용 대상이 됨
  - 스프링 부트가 `PersistenceExceptionTranslationPostProcessor`를 자동 등록해줘서 AOP Proxy가 예외 변환을 해주는 것
<br>

**예외 변환 전**

![image](https://github.com/user-attachments/assets/56a649c3-2aed-4e89-a553-f32db17a0cc8)
> JPA Exception에 종속되어 버림
<br>

**예외 변환 후**

![image](https://github.com/user-attachments/assets/f7a1bc27-9a5d-40ae-bbfe-57abc343a7b6)
> Proxy AOP에 의해 예외 변환
<br>
<br>

### 학습 범위 : 7-6-1 - 7-6-6
- 스프링 데이터 JPA : JPA을 좀 더 편리하게 사용하도록 함
  - CRUD + 쿼리
  - 페이징
  - 동일한 인터페이스
  - 메서드 이름으로 쿼리생성 : 로직이 복잡해지면 이름이 길어지는 단점 발생

- Spring Data는 비즈니스 로직에 더 집중할 수 있게 데이터베이스 사용 기능을 클래스 레벨에서 추상화

- Spring Data라는 추상기술은 JPA, Redis, Mysql 등등 모든 기술을 구현해서 사용 할 수 있음 -> 현 학습에서는 JPA로 진행하는 것

- JpaRepository : 해당 인터페이스를 상속받으면 됨

- `@Query` : JPQL 사용을 통한 SQL 직접 작성
<br>

**의존 관계**
- ItemRepository 인터페이스를 SpringDataJpaItemRepository를 만들어서 구현해야함 -> OCP를 지키기 위해
![image](https://github.com/user-attachments/assets/76822547-b9fc-4045-b623-cb017e4b80eb)
> 만약 ItemService에 바로 JpaRepository 인터페이스를 구현한다면 쓸 수는 있겠지만<br>
OCP를 지킬 수 없다.
<br>
<br>

### 학습 범위 : 7-7-1 - 7-7-5
- Querydsl : 쿼리를 java 코드로 개발할 수 있게 지원하는 프레임워크
  - dsl이란 도메인 특화 언어라는 뜻
  - JPQL을 만들어주는 빌더라고 보면됨
  - 💡 type-check가 가능하기 때문에 컴파일 시점에 쿼리에 에러가 있는지 발견이 가능하다.

- Querydsl > JPQL > SQL 로 변환 과정을 거침

- JPQL로 해결하기 어려운 복잡한 쿼리는 네이티브 SQL을 사용 (JdbcTemplate, Mybatis)

- JPAQueryFactory

- JPA, Spring Data JPA, Querydsl 의존성을 전부 주입받아 그때 그때 알맞은 것을 사용해도 된다.
  - 팀이 사용하는 통일 양식을 따르면 될 것

```java
private final EntityManager em;
private final JPAQueryFactory query;
private final SpringDataJpaItemRepository repository; //JpaRepository의 구현체

public JpaItemRepositoryV4(EntityManager em, SpringDataJpaItemRepository repository) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
    this.repository = repository;
}
```
<br>
<br>

### 학습 범위 : 7-8-1 - 7-8-4
- DI, OCP를 도입하면 구현체 변경에는 자유로우나 복잡해진다.

- DI, OCP를 도입하지 않으면 구현체 변경은 힘드나 코드가 직관적이며, 빠른 개발이 가능하다.

- 즉, 트레이드 오프 관계이기 때문에 프로젝트 성격에 따라 알맞게 적용을 해야한다.

- 어설픈 추상화는 오히려 독이 될 수 있기 때문이다.
<br>

![image](https://github.com/user-attachments/assets/dcd0f4eb-2484-44bf-8eae-dd36e84b58b6)
> 추상화 도입 시 구현체 변경이 자유롭지만, 구조가 복잡해진다.
<br>

![image](https://github.com/user-attachments/assets/181717dc-e08b-4ff0-aeb7-bd68f7586d96)
> 추상화를 하지 않으면 단순해지므로 빠른 개발이 가능해지지만, 구현체 변경은 할 수 없다.
<br>
<hr>
<br>

## ✔️ springtx directory
###  학습 범위 : 7-9-1 - 7-9-11
- @Transactional 동작 확인 및 부가 기능
  - 위치에 따른 실행 순서
 
- 1. 트랜잭션 AOP 주의 사항
  - 내부에서 메서드 직접 호출에 따른 AOP Proxy 적용이 되지 않음
  - 새로운 class를 만들어서 호출하는 방식으로 변경
  - `public` 메서드에만 트랜잭션 적용이 됨
 
![image](https://github.com/user-attachments/assets/25623fc9-a095-48f9-8b8b-20e715665b57)
> external 메서드 안에서 트랜잭션이 적용된 internal 함수 호출 시 this.internal로 인스턴스 참조가 되기 때문에<br>
자기 자신을 호출하게 되므로 트랜잭션 AOP를 거치지 않고 인스턴스의 메서드를 바로 호출한다.
<br>

- 만약 `exnernal` 함수 호출 뒤에 바로 `internal` 함수를 호출하고 싶으면, 아래와 같이 별도의 클래스로 분리 후 호출한다.

![image](https://github.com/user-attachments/assets/c61becd1-3894-42f1-82dd-657bfd38ae6f)
> 이전처럼 this.internal로 호출하는 것이 아니기 때문에 AOP Proxy가 호출된다.
<br>

- 2. 트랜잭션 AOP 주의 사항
  - 초기화 코드 `@PostContruct`는 스프링 컨테이너가 완전히 호출되기 전 먼저 실행되므로 트랜잭션 AOP가 적용이 안됨
  - 때문에 초기화 코드가 필요하다면 `@EventListener(ApplicationReadyEvent.class)`를 대신 사용 -> 스프링 컨테이너 완전 뜬 후 실행됨
<br>

- 트랜잭션 옵션 설명

- 예외 처리에 대한 커밋, 롤백
  - 체크 예외 : 커밋 -> 비즈니스 의미가 있는 예외 (스프링에서 체크 예외는 의미가 있다고 판단하기 때문)
  - 언체크 예외 : 롤백 -> 복구 불가능한 예외 (스프링에서 언체크 예외는 시스템 에러로써 복구 불가능하다고 판단하기 때문)

예시
```
1. 정상: 주문시 결제를 성공하면 주문 데이터를 저장하고 결제 상태를 완료 로 처리한다.
2. 시스템 예외: 주문시 내부에 복구 불가능한 예외가 발생하면 전체 데이터를 롤백한다.
3. 비즈니스 예외: 주문시 결제 잔고가 부족하면 주문 데이터를 저장하고, 결제 상태를 대기 로 처리한다.
이 경우 고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내한다.
```
> 체크 예외인 경우에도 롤백을 하고 싶으면 @Transactional(rollbackFor = ***Exception.class) 사용
<br>
<br>

### 학습 범위 : 7-10-1 - 7-10-9
**➡️ 트랜잭션 전파 설명**
 
- 물리 트랜잭션과 논리 트랜잭션
  - 모든 논리 트랜잭션이 커밋되어야 물리 트랜잭션이 커밋된다.
  - 하나의 논리 트랜잭션이라도 롤백되면 물리 트랜잭션은 롤백된다.

![image](https://github.com/user-attachments/assets/131ea775-2de1-42da-93be-4bfcfc1613dc)
![image](https://github.com/user-attachments/assets/ae86ccbf-6838-45a6-a15c-ef76c4f265f8)
<br>
<br>

**➡️ 트랜잭션 전파**

![image](https://github.com/user-attachments/assets/831fe9e4-48dd-4bc7-b6fa-5f3aad4a00da)
> 외부 트랜잭션이 커밋되어야 물리 트랜잭션이 커밋된다.
내부 트랜잭션은 중간에 참여한 트랜잭션이기 때문에 커밋을 찍어도 아무런 변화가 없다.
<br>
<br>

**➡️ 트랜잭션 롤백**
- 논리 트랜잭션 중 1개라도 롤백이 되면 물리 트랜잭션 자체가 롤백이 되야 한다.

- 외부 트랜잭션 or 내부 트랜잭션 둘 중 1개라도 롤백이면 롤백인 것
<br>

![image](https://github.com/user-attachments/assets/a655a017-17f5-4140-9c9c-e0987f13abf7)
> 외부 트랜잭션이 롤백되어 물리 트랜잭션 또한 롤백이 되었다.
<br>

![image](https://github.com/user-attachments/assets/4f77fa43-e906-4bd5-8e2b-0776f6d9078c)
> 내부 트랜잭션이 롤백되어 물리 트랜잭션 또한 롤백이 되었다.
<br>

- 내부 트랜잭션이 롤백이 되면 트랜잭션 매니저에게 `rollbackOnly=true` 옵션을 전달한다.
  - 내부 트랜잭션은 신규 트랜잭션이 아니기 때문에 실제로 커밋, 롤백을 호출 할 수가 없다.
  - 이러한 문제점을 해결하기 위해 내부 트랜잭션에서 롤백이 발생하면 트랜잭션 매니저에게 플래그를 주는 것이다.

![image](https://github.com/user-attachments/assets/d7ae76eb-ca58-4f07-bd06-a1fb86f13f9e)
<br>
<br>

**➡️ REQUIRES_NEW**
- 외부 트랜잭션에서 새로운 트랜잭션을 생성 시 별도의 트랜잭션으로 만들어준다.

- 기존에는 외부 트랜잭션에서 내부 트랜잭션을 선언하면 "전부 커밋" or "전부 롤백" 이라는 상황이 생겼다.

- 그러나 `REQUIRES_NEW`를 사용하면 내부 트랜잭션이 별도의 커넥션을 획득하여 분리된 트랜잭션을 실행하는 것이다.

![image](https://github.com/user-attachments/assets/24dcfbc8-0c90-4363-a968-fdcb2c69f882)
> 내부 트랜잭션이여도 REQUIRES_NEW를 통해 롤백하면 외부 트랜잭션에 영향을 주지 않는다.
<br>

![image](https://github.com/user-attachments/assets/40c55d9d-d82a-4c41-9370-c7ca51b8ac18)
> 각각 다른 커넥션을 통해 트랜잭션을 실행하는 로직
<br>

- 💡 별도의 커넥션을 가지더라도 외부 트랜잭션이 먼저 끝나면, 내부 트랜잭션은 실행을 마칠 수가 없다. -> Exception 발생
<br>
<br>

**➡️ 전파 옵션**
- REQUIRED (@Transactional의 기본 설정이 이것임)
  - 가장 많이 사용하는 기본 설정이다. 기존 트랜잭션이 없으면 생성하고, 있으면 참여한다.
  - 트랜잭션이 필수라는 의미로 이해하면 된다. (필수이기 때문에 없으면 만들고, 있으면 참여한다.)
  - 기존 트랜잭션 없음: 새로운 트랜잭션을 생성한다.
  - 기존 트랜잭션 있음: 기존 트랜잭션에 참여한다.
<br>

- REQUIRES_NEW
  - 항상 새로운 트랜잭션을 생성한다.
  - 기존 트랜잭션 없음: 새로운 트랜잭션을 생성한다.
  - 기존 트랜잭션 있음: 새로운 트랜잭션을 생성한다.
<br>

- SUPPORT
  - 트랜잭션을 지원한다는 뜻이다. 기존 트랜잭션이 없으면, 없는대로 진행하고, 있으면 참여한다.
  - 기존 트랜잭션 없음: 트랜잭션 없이 진행한다.
  - 기존 트랜잭션 있음: 기존 트랜잭션에 참여한다.
<br>

- NOT_SUPPORT
  - 트랜잭션을 지원하지 않는다는 의미이다.
  - 기존 트랜잭션 없음: 트랜잭션 없이 진행한다.
  - 기존 트랜잭션 있음: 트랜잭션 없이 진행한다. (기존 트랜잭션은 보류한다)
<br>

- MANDATORY
  - 의무사항이다. 트랜잭션이 반드시 있어야 한다. 기존 트랜잭션이 없으면 예외가 발생한다.
  - 기존 트랜잭션 없음: IllegalTransactionStateException 예외 발생
  - 기존 트랜잭션 있음: 기존 트랜잭션에 참여한다.
<br>

- NEVER
  - 트랜잭션을 사용하지 않는다는 의미이다. 기존 트랜잭션이 있으면 예외가 발생한다. 기존 트랜잭션도
  - 허용하지 않는 강한 부정의 의미로 이해하면 된다.
  - 기존 트랜잭션 없음: 트랜잭션 없이 진행한다.
  - 기존 트랜잭션 있음: IllegalTransactionStateException 예외 발생
<br>

- NESTED
  - 기존 트랜잭션 없음: 새로운 트랜잭션을 생성한다.
  - 기존 트랜잭션 있음: 중첩 트랜잭션을 만든다.
  - 중첩 트랜잭션은 외부 트랜잭션의 영향을 받지만, 중첩 트랜잭션은 외부에 영향을 주지 않는다.
  - 중첩 트랜잭션이 롤백 되어도 외부 트랜잭션은 커밋할 수 있다.
  - 외부 트랜잭션이 롤백 되면 중첩 트랜잭션도 함께 롤백된다.
<br>

- 참고
  - JDBC savepoint 기능을 사용한다. DB 드라이버에서 해당 기능을 지원하는지 확인이 필요하다.
  - 중첩 트랜잭션은 JPA에서는 사용할 수 없다.
<br>
<br>

### 학습 범위 : 7-11-1 - 7-11-8
- 트랜잭션 전파 활용 예시

- 예외가 service로 올라온 경우 잡아서 복구시키기 (REQUIRES_NEW)

![image](https://github.com/user-attachments/assets/40e9e436-2734-49bc-905e-6bc83e25d66c)
> REQUIRES_NEW 덕분에 Log는 별개의 커넥션을 가짐
<br>

- 그러나 REQUIRES_NEW를 통해 커넥션을 분리하게 되면 HTTP 1개 요청에 2개의 커넥션을 사용하는 것으로
DB의 부하를 줄 수 있다.

- 반드시 필요한 곳에만 써야하며, 반드시 같이 실행해야하는 함수라면 아래 사진 처럼 애초에 분리시켜서 하는 방법이 있다.

![image](https://github.com/user-attachments/assets/6ca73fc4-23d3-4e71-8783-55ee5a73ef14)
> MemberService에서 member저장과 log 저장을 같이 하지 않고, 아예 분리 시킴
