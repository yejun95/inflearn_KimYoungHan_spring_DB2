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
private final SpringDataJpaItemRepository repository;

public JpaItemRepositoryV4(EntityManager em, SpringDataJpaItemRepository repository) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
    this.repository = repository;
}
```
<br>
<hr>
<br>

## ✔️ springtx directory
###  학습 범위 : 7-8-1 - 
- @Transactional 확인 및 기능
