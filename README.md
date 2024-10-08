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
