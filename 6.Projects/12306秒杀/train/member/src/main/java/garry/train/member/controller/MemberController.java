package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.form.MemberSendCodeForm;
import garry.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // RestController = Controller + ResponseBody
@RequestMapping(value = "/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseVo<Integer> count() {
        int count = memberService.count();
        return ResponseVo.success(count);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseVo register(@Valid MemberRegisterForm form) {
        long registerId = memberService.register(form);
        return ResponseVo.success(registerId);
    }

    @RequestMapping(value = "/send-code", method = RequestMethod.POST)
    public ResponseVo sendCode(@Valid MemberSendCodeForm form) {
        memberService.sendCode(form);
        return ResponseVo.success();
    }
}
