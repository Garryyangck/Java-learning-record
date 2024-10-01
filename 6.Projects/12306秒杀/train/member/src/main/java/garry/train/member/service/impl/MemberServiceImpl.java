package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import garry.train.common.consts.CommonConst;
import garry.train.common.consts.RedisConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.JWTUtil;
import garry.train.common.util.RedisUtil;
import garry.train.member.form.MemberLoginForm;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.form.MemberSendCodeForm;
import garry.train.member.mapper.MemberMapper;
import garry.train.member.pojo.Member;
import garry.train.member.pojo.MemberExample;
import garry.train.member.service.MemberService;
import garry.train.member.service.SmsService;
import garry.train.member.vo.MemberLoginVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * 2024-09-05 16:41
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SmsService smsService;

    @Override
    public int count() {
        return (int) memberMapper.countByExample(null);
    }

    @Override
    @Deprecated
    public long register(MemberRegisterForm form) {
        // 检验mobile是否重复
        String mobile = form.getMobile();
        List<Member> members = selectMembersByMobile(mobile);

        if (CollUtil.isNotEmpty(members)) {
            throw new BusinessException(ResponseEnum.MEMBER_MOBILE_REGISTER_EXIST);
        }

        Member member = new Member();
        member.setId(CommonUtil.getSnowflakeNextId()); // 使用雪花算法获取自增不重复的Id
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    @Override
    public void sendCode(MemberSendCodeForm form) {
        String mobile = form.getMobile();
        List<Member> members = selectMembersByMobile(mobile);

        if (CollUtil.isEmpty(members)) {
            log.info("手机号 {} 未注册，插入一条新的用户", mobile);
            Member member = new Member();
            member.setId(CommonUtil.getSnowflakeNextId()); // 使用雪花算法获取自增不重复的Id
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            log.info("手机号 {} 已注册", mobile);
        }

        String code = RandomUtil.randomString(CommonConst.CODE_LENGTH);
        log.info("手机号 {} 的验证码为: {}", mobile, code);

        log.info("将手机号 {} 的验证码 {} 存储到 Redis 中", mobile, code);
        redisTemplate.opsForValue().set(RedisUtil.getRedisKey4Code(mobile), code, RedisConst.CODE_EXPIRE_SECOND, TimeUnit.SECONDS);

        log.info("调用阿里云运营商的接口，发短信给手机 {}", mobile);
        HashMap<String, String> templateCode = new HashMap<>(); // templateCode 必须是 JSON 字符串
        templateCode.put("code", code);
        smsService.sendSms(mobile, JSONObject.toJSONString(templateCode));
    }

    @Override
    public MemberLoginVo login(MemberLoginForm form) {
        String mobile = form.getMobile();
        String code = form.getCode();
        List<Member> members = selectMembersByMobile(mobile);

        if (CollUtil.isEmpty(members)) {
            log.info("手机号 {} 不存在", mobile);
            throw new BusinessException(ResponseEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        String rightCode = (String) redisTemplate.opsForValue().get(RedisUtil.getRedisKey4Code(mobile));
        if (StrUtil.isBlank(rightCode)) {
            log.info("手机号 {} 未获取验证码或验证码已过期", mobile);
            throw new BusinessException(ResponseEnum.MEMBER_CODE_NOT_EXIST);
        }

        if (!rightCode.equals(code)) {
            log.info("手机号 {} 的验证码 {} 与正确验证码 {} 不匹配", mobile, code, rightCode);
            throw new BusinessException(ResponseEnum.MEMBER_WRONG_CODE);
        }

        log.info("手机号 {} 的用户身份验证成功，使用 JWT 设置 token", mobile);
        Member member = members.get(0);
        MemberLoginVo vo = BeanUtil.copyProperties(member, MemberLoginVo.class);
        vo.setToken(JWTUtil.createToken(member.getId(), member.getMobile()));
        return vo;
    }

    private List<Member> selectMembersByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria() // 创建条件
                .andMobileEqualTo(mobile); // and意为“且”，即并且mobile="mobile"
        List<Member> members = memberMapper.selectByExample(memberExample);
        return members;
    }
}
