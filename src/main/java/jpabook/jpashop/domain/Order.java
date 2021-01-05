package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")   //관례적으로 orders
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    //==생성 메서드==// * 로직이 복잡하므로 별도 생성 메서드 작성, 생성되고 연관 설정됨. 밖에서 set set set 하지 않고 한번에.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); //재고 원복 시켜줌
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice).sum(); //alt+Enter : replace sum() -> ctrl+alt+N
        /*int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;*/
    }

}

