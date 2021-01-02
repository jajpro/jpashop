package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long Id;

    private String name;

    @Embedded //내장타입
    private Address address;

    //One Member 가 Many Order. mappedBy : 읽기 전용. 나는 맵핑된 거울일 뿐이다. 여기에 값을 넣는다고 FK가 변경되지 않음
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
