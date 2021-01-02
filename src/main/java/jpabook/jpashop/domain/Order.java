package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")   //관례적으로 orders
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne  //Many order by one member. 연관관계의 주인
    @JoinColumn(name = "member_id") //FK 가 member_id 가 되어 맵핑
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //ORDINAL 은 숫자 방식인데 쓰면안됨. 중간에 다른 상태가 들어가면 XXX 로 되버림
    private OrderStatus status; //주문상태 [Order, Cancel]

}
