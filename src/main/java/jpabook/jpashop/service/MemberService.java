package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //public method 들 모두에 적용
/* @RequiredArgsConstructor final member 대상으로 생성자 만들어줌. 현재 처럼 생성자 인젝션 또는 @Autowired 대신
private final MemberRepository memberRepository; 한줄로 가능 */
public class MemberService {

    /* 필드 주입 방법 - 임의적인 주입이 까다로움
    @Autowired
    private MemberRepository memberRepository;

    setter 주입은 사실 런타임에 새로 바꿀일이 없으므로 안좋음. 따라서 생성자 인젝션 권장
    */

    private final MemberRepository memberRepository; //변경할 일 없으므로 final. 컴파일 시점에 확인 가능

    @Autowired  //생성자 인젝션. 한번 생성된 이후 안바뀜. 생성자가 하나밖에 없으면 Autowired 생략 가능. 알아서 주입
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    @Transactional(readOnly = false)    //전역 보다 우선함. 디폴트가 false 라 괄호 생략 가능
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //중복회원검증
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        //실무에서는 같은 이름으로 동시에 가입 save() 할 수 있으므로, DB 에도 UNIQUE 제약조건 걸어야 됨
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
