package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import garry.train.member.form.MemberLoginForm;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.form.MemberSendCodeForm;
import garry.train.member.service.MemberService;
import garry.train.member.vo.MemberLoginVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Deprecated
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseVo<Long> register(@Valid @RequestBody/*必须使用@RequestBody，才能接收(application/json)格式的请求*/ MemberRegisterForm form) {
        long registerId = memberService.register(form);
        return ResponseVo.success(registerId);
    }

    @RequestMapping(value = "/send-code", method = RequestMethod.POST)
    public ResponseVo sendCode(@Valid @RequestBody MemberSendCodeForm form) {
        memberService.sendCode(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseVo<MemberLoginVo> login(@Valid @RequestBody MemberLoginForm form) {
        MemberLoginVo vo = memberService.login(form);
        return ResponseVo.success(vo);
    }
}
