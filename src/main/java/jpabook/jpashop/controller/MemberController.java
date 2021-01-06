package jpabook.jpashop.controller;

import com.sun.istack.NotNull;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //domain 과 controller 가 원하는 형태가 다를 수 있기 때문에 Member 로 받지 않고 MemberForm 으로 받음. id, orders 등등 불필요. NotEmpty 등도 지저분. 엔티티는 화면 종속성 없이 설계가 중요
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) //오류가 생겼을 때 튕기지 않고 BindingResult 에 담겨서 계속 실행. import org.springframework.validation.BindingResult;
    {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        //원래는 Member 를 넘기면 안됨. Member 에 passwd 가 있거나 다른 필드 추가할 경우 보안문제, APT 스펙이 변할 수 있음.
        //그러나 현재는 템플릿 엔진이 서버사이드에서 처리 해주니까 문제 될 것은 없음. 그래도 List<Member> 보다는 화면에 맞는 DTO 새로 만드는 것 권장
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
