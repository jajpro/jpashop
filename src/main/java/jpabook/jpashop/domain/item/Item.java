package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//JOINED : 정규화된 스타일/ SINGLE_TABLE : 한 테이블에 다 넣음(성능 이점)/ TABLE_PER_CLASS : Book, Album, Movie 3개의 테이블 만듬
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") //SINGLE_TABLE 이므로 구분할 방법이 필요
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    //N:N 은 실무에서는 안씀. 중간 Table 에 다른 값을 넣을 수 없음. 따라서 설계 시 새로 1:N, N:1 로 풀어서 처리
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /* 도메인 주도 설계. 엔티티 자체적으로 해결할 수 있는 기능들은 앤티티를 가진 곳에서 비즈니스 로직이 나가는게 응집도 있고 객체지향적 장점
    * setter 쓸 필요 없이 밖에서 하지 말고 그냥 여기서 비즈니스 로직으로 처리하는 것이 좋음
    * */
    //==비즈니스 로직==//
    /**
     * stock 증가
     * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    /**
     * stock 감소
     * */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("Need more stock");
        }
        this.stockQuantity = restStock;
    }

}
