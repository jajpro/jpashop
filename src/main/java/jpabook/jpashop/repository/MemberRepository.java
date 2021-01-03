package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //@SpringBootApplication 이 있는 해당 패키지 하위에 있는 애들이 Component Scan 대상이 되어 Bean 으로 등록됨
public class MemberRepository {

    //원래는 @Autowired 가 안됬는데 Spring boot 에서는 지원해줌
    @PersistenceContext //spring 이 EntityManager 를 만들어서 여기에 알아서 주입함. 팩토리 만들 필요 없이
    private EntityManager em;

    /* 위에꺼가 있으므로 별로 쓸일 없음
    @PersistenceUnit
    private EntityManagerFactory emf;
    */

    public void save(Member member) {
        em.persist(member);
        //insert 쿼리가 바로 안나감. 트랜잭션이 커밋 될 때 flush 하면서 insert 나감
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    //위 JPQL 은 객체를 대상으로 쿼리
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
