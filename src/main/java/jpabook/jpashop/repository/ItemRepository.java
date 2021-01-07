package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        //item 은 JPA 에 저장하기 전까지는 id 값이 없음. 즉 새로 생성한 객체.
        if (item.getId() == null) {
            em.persist(item);
        } else {
            //이미 DB 에 등록된 것으로 update 로 간주
            em.merge(item); //item 은 그냥 파라미터로 넘어온 것이고 뭘 더 하려면 Item merge = em.merge(item); 에서 영속성인 merge 로 해야함.
            //merge 시 모든 속성이 변경되는데 값이 없으면 null 로 업데이트할 위험. 따라서 실무에서는 위험. 수정할 시에 빼먹은 필드 있으면 null.
            //merge 쓰지말고 변경감지를 쓰자.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
