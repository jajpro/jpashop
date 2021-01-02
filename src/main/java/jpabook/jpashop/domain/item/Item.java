package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
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
}
