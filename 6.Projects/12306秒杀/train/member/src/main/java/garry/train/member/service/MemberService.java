package garry.train.member.service;

import garry.train.member.form.MemberLoginForm;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.form.MemberSendCodeForm;
import garry.train.member.vo.MemberLoginVo;

/**
 * @author Garry
 * 2024-09-05 16:39
 */
public interface MemberService {
    /**
     * 获得用户总数
     */
    int count();

    /**
     * 获取 mobile，注册会员
     */
    @Deprecated
    long register(MemberRegisterForm form);

    /**
     * 将验证码存储到 redis设置过期时间，发送验证码给手机，无需返回值，因为验证码的判断逻辑是在后端而不是前端
     */
    void sendCode(MemberSendCodeForm form);

    /**
     * 验证用户登录
     */
    MemberLoginVo login(MemberLoginForm form);
}
