package garry.train.member.service;

import garry.train.member.form.MemberRegisterForm;

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
    long register(MemberRegisterForm form);
}
