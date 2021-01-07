package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional  //전역보다 우선권 가지고 default 가 false 임.
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional  //변경 감지 방식
    public void updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());

        //itemRepository.save(findItem); findItem 이 영속상태라 할 필요 없음. Transactional 이 커밋 알아서 JPA 가 변경감지해서 바꿈. update 쿼리 만들어서 flush cf) cancel
    }

    //readOnly = true 가 먹음
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
