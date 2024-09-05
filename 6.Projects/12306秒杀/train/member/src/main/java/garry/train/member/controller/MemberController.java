package garry.train.member.controller;

import garry.train.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public String count() {
        return String.valueOf(memberService.getMemberCount());
    }
}
