package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity //JPA 객체로 등록
//@Table(name = "item") 객체명 = 테이블 명이 같으면 생략 가능
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment
    private Long id;

    @Column(name = "item_name", length = 10) //스네이크 -> 카멜로 자동 변환되기 때문에 없어도 무방
    private String itemName;
    //컬럼명이 db = 객체 동일하면 @Column 설정 불필요
    private Integer price;
    private Integer quantity;

    //JPA public or protected 기본 생성자 필수 -> JPA 조건임
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
