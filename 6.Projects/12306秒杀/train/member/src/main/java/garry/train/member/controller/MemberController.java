package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    @ResponseBody
    public ResponseVo register(MemberRegisterForm form) {
        long registerId = 0;
        try {
            registerId = memberService.register(form);
        } catch (RuntimeException e) {
            return ResponseVo.error(e.getMessage());
        }
        return ResponseVo.success(registerId);
    }
}
