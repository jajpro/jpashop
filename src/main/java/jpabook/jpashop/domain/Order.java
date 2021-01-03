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

    @ManyToOne(fetch = FetchType.LAZY)  //Many order by one member. 연관관계의 주인
    @JoinColumn(name = "member_id") //FK 가 member_id 가 되어 맵핑
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    /*
    원래는
    persist(orderItemA);
    persist(orderItemB);
    persist(orderItemC);
    persist(order);
    cascade ALL 하면 그냥
    persist(order); 만 하면됨
    */

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //ORDINAL 은 숫자 방식인데 쓰면안됨. 중간에 다른 상태가 들어가면 XXX 로 되버림
    private OrderStatus status; //주문상태 [Order, Cancel]

    //==연관관계 매서드 : 양쪽 세팅을 원자적으로 한 곳에서 해버릴 수 있도록 함==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
        /*
        public static void main(String args[]) {
            Member member = new Member();
            Order order = new Order();
            //하나로 줄일 수 있음
            //member.getOrders().add(order);
            order.setMember(member);
        }*/
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
