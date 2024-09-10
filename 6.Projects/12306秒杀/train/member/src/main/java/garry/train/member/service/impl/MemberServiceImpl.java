package garry.train.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import garry.train.common.consts.CommonConst;
import garry.train.common.consts.RedisConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.RedisUtil;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.form.MemberSendCodeForm;
import garry.train.member.mapper.MemberMapper;
import garry.train.member.pojo.Member;
import garry.train.member.pojo.MemberExample;
import garry.train.member.service.MemberService;
import garry.train.member.service.SmsService;
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
    public long register(MemberRegisterForm form) {
        // 检验mobile是否重复
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria() // 创建条件
                .andMobileEqualTo(form.getMobile()); // and意为“且”，即并且mobile="mobile"
        List<Member> members = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(members)) {
            throw new BusinessException(ResponseEnum.MEMBER_REGISTER_EXIST);
        }

        Member member = new Member();
        member.setId(CommonUtil.getSnowflakeNextId()); // 使用雪花算法获取自增不重复的Id
        member.setMobile(form.getMobile());
        memberMapper.insert(member);
        return member.getId();
    }

    @Override
    public void sendCode(MemberSendCodeForm form) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria()
                .andMobileEqualTo(form.getMobile());
        List<Member> members = memberMapper.selectByExample(memberExample);

        if (CollUtil.isEmpty(members)) {
            log.info("手机号 {} 未注册，插入一条新的用户", form.getMobile());
            Member member = new Member();
            member.setId(CommonUtil.getSnowflakeNextId()); // 使用雪花算法获取自增不重复的Id
            member.setMobile(form.getMobile());
            memberMapper.insert(member);
        } else {
            log.info("手机号 {} 已注册", form.getMobile());
        }

        String code = RandomUtil.randomString(CommonConst.CODE_LENGTH);
        log.info("手机号 {} 的验证码为: {}", form.getMobile(), code);

        log.info("将手机号 {} 的验证码 {} 存储到 Redis 中", form.getMobile(), code);
        String redisKey = RedisUtil.getRedisKey4Code(form.getMobile());
        redisTemplate.opsForValue().set(redisKey, code, RedisConst.CODE_EXPIRE_SECOND, TimeUnit.SECONDS);

        log.info("调用阿里云运营商的接口，发短信给手机 {}", form.getMobile());
        HashMap<String, String> templateCode = new HashMap<>(); // templateCode 必须是 JSON 字符串
        templateCode.put("code", code);
        smsService.sendSms(form.getMobile(), JSONObject.toJSONString(templateCode));
    }
}
