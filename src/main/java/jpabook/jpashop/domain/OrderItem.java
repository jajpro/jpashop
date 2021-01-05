package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; //주문수량

    //==생성 매서드==//
    //protected -> 쓰지 말아라. createOrderItem 으로만 생성하고 OrderItem orderItem = new OrderItem(); 막을 수 있음
    //롬복 @NoArgsConstructor(access = AccessLevel.PROTECTED) 선언하면 같은 기능
    protected OrderItem() {
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     * */
    public void cancel() {
        getItem().addStock(count); //재고수량 원복

    }

    //==조회 로직==//
    /**
     * 주문상품 전체가격 조회
     * */
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
