package garry.community.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.consts.CommunityConst;
import garry.community.dao.UserMapper;
import garry.community.enums.*;
import garry.community.pojo.LoginTicket;
import garry.community.pojo.User;
import garry.community.service.UserService;
import garry.community.utils.CommunityUtil;
import garry.community.utils.MailClient;
import garry.community.utils.MistakeUtil;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Garry
 * ---------2024/3/25 19:45
 **/

/**
 * 该实现类没有使用redis缓存user
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Primary
@Service
public class UserServiceMysqlImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;//域名

    @Value("${server.servlet.context-path}")
    private String contextPath;//上下文地址

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public User findByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public int updateHeader(int userId, String headerUrl) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setHeaderUrl(headerUrl);
        int rows = userMapper.updateByPrimaryKeySelective(user);
        if (rows <= 0) {
            throw new RuntimeException("【用户头像更新失败】userId = " + userId);
        }
        return rows;
    }

    @Override
    public int updatePassword(int userId, String password) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(password);
        int rows = userMapper.updateByPrimaryKeySelective(user);
        if (rows <= 0) {
            throw new RuntimeException("【用户密码更新失败】userId = " + userId);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> register(User user) {
        /*
            1.参数为空，服务器对象创建失败
         */
        if (user == null) {
            throw new IllegalArgumentException("【用户参数不能为空】");
        }

        //创建responseMap
        Map<String, Object> responseMap = new HashMap<>();

        /*
            2.账号，密码，邮箱为空
         */
        if (StringUtils.isBlank(user.getUsername())) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.USERNAME_BLANK);
        }
        if (StringUtils.isBlank(user.getPassword())) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.PASSWORD_BLANK);
        }
        if (StringUtils.isBlank(user.getEmail())) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.EMAIL_BLANK);
        }
        if (!responseMap.isEmpty()) return responseMap;

        /*
            3.账号，邮箱重复，密码过短
         */
        User byUsername = userMapper.selectByUsername(user.getUsername());
        if (byUsername != null) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.USERNAME_EXIST);
        }
        User byEmail = userMapper.selectByEmail(user.getEmail());
        if (byEmail != null) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.EMAIL_EXIST);
        }
        if (user.getPassword().length() < CommunityConst.MIN_PASSWORD_LENGTH) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.PASSWORD_TOO_SHORT);
        }
        if (!responseMap.isEmpty()) return responseMap;

        /*
            4.注册
         */
        user.setSalt(CommunityUtil.generateUUID()
                .substring(0, CommunityConst.SALT_LENGTH));//设置盐值
        user.setPassword(CommunityUtil.md5Encryption(//设置加密密码
                user.getPassword() + user.getSalt()));//原密码+盐值

        user.setType(UserTypeEnum.USER.getCode());//普通用户
        user.setStatus(UserStatusEnum.UNACTIVATED.getCode());//未激活

        user.setActivationCode(CommunityUtil.generateUUID()//激活密码
                .substring(0, CommunityConst.ACTIVATION_CODE_LENGTH));//密码长度

        int headerNum = new Random().nextInt(CommunityConst.USER_HEADER_URL_BOUND + 1);
        String headerUrl = String.format(CommunityConst.USER_HEADER_URL_TEMPLATE, headerNum);
        user.setHeaderUrl(headerUrl);//设置用户头像的绝对地址
        user.setCreateTime(new Date());//设置时间

        int insertRows = userMapper.insert(user);
        if (insertRows <= 0) {
            throw new RuntimeException("【用户插入失败】user = " + gson.toJson(user));
        }

        /*
            5.发送验证邮件
         */
        //传入用户邮箱地址
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", user.getEmail());

        //获取激活码的URL，使用字符拼接，Controller获取参数方式为路径参数
        String url = domain + contextPath + "/activation/" +
                user.getId() + "/" + user.getActivationCode();
        variables.put("url", url);

        //模板引擎获取渲染后的html内容
        context.setVariables(variables);
        String content = templateEngine.process("/mail/activation", context);

        //尝试发送邮件，若邮箱地址有误，则会在responseMap中添加WRONG_EMAIL
        //用户邮箱有误，则必须在try-catch中手动回滚事务
        try {
            mailClient.sendMail(user.getEmail(), "牛客网-激活账号", content);
        } catch (Exception e) {
            log.error("【用户邮箱地址有误】user = {}", gson.toJson(user));
            MistakeUtil.addMistake(responseMap, MistakeEnum.WRONG_EMAIL);
            /*try-catch异常时，手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return responseMap;
    }

    @Override
    public ActivationEnum activation(Integer userId, String activationCode) {
        User user = userMapper.selectByPrimaryKey(userId);
        //用户为空，或激活码不正确，激活失败
        if (user == null || !user.getActivationCode().equals(activationCode)) {
            return ActivationEnum.ACTIVATION_FAIL;
        }

        //用户已激活，重复激活
        if (user.getStatus().equals(UserStatusEnum.ACTIVATED.getCode())) {
            return ActivationEnum.ACTIVATION_REPEAT;
        }

        //激活成功
        user.setStatus(UserStatusEnum.ACTIVATED.getCode());
        int updateRows = userMapper.updateByPrimaryKeySelective(user);
        if (updateRows <= 0) {
            throw new RuntimeException("【用户更新失败】user = " + gson.toJson(user));
        }
        return ActivationEnum.ACTIVATION_SUCCESS;
    }

    @Override
    public Map<String, Object> login(String username, String password, Long expiredSeconds) {
        Map<String, Object> responseMap = new HashMap<>();

        /*
            1.username，password不能为空
         */
        if (StringUtils.isBlank(username)) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.USERNAME_BLANK);
        }
        if (StringUtils.isBlank(password)) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.PASSWORD_BLANK);
        }
        if (!responseMap.isEmpty()) return responseMap;

        /*
            2.用户不存在
         */
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.WRONG_USERNAME);
            return responseMap;
        }

        /*
            3.账号未激活
         */
        if (user.getStatus().equals(UserStatusEnum.UNACTIVATED.getCode())) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.USER_UNACTIVATED);
            return responseMap;
        }

        /*
            4.密码错误
         */
        if (!user.getPassword().equals(
                CommunityUtil.md5Encryption(password + user.getSalt()))) {
            MistakeUtil.addMistake(responseMap, MistakeEnum.WRONG_PASSWORD);
            return responseMap;
        }

        /*
            5.登录成功，生成登录凭证
         */
        LoginTicket loginTicket = new LoginTicket();
        //userId
        loginTicket.setUserId(user.getId());
        //ticket的长度为LOGIN_TICKET_LENGTH
        loginTicket.setTicket(CommunityUtil.generateUUID().substring(0, CommunityConst.LOGIN_TICKET_LENGTH));
        //凭证的状态为有效
        loginTicket.setStatus(LoginTicketStatusEnum.EFFECTIVE.getCode());
        //有效时间为expiredSeconds
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        //responseMap中传入loginTicket，便于服务器发送cookie
        responseMap.put(MsgEnum.LOGIN_TICKET_MSG.getMsg(), loginTicket);
        return responseMap;
    }

    @Override
    public void logout(String ticket) {
        String loginTicketKey = RedisKeyUtil.getLoginTicketKey(ticket);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String loginTicketJson = (String) opsForValue.get(loginTicketKey);
        LoginTicket loginTicket = gson.fromJson(loginTicketJson, LoginTicket.class);
        loginTicket.setStatus(LoginTicketStatusEnum.INEFFECTIVE.getCode());
        opsForValue.set(loginTicketKey, gson.toJson(loginTicket));
    }

    @Override
    public String sendVerifyCode(String email) {
        //生成验证码
        String verifyCode = CommunityUtil.generateUUID().substring(0, CommunityConst.VERIFY_CODE_LENGTH);

        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", email);
        variables.put("verifyCode", verifyCode);
        variables.put("effectiveMin", CommunityConst.FORGET_EFFECTIVE_TIME);
        //模板引擎获取渲染后的html内容
        context.setVariables(variables);
        String content = templateEngine.process("/mail/forget", context);

        try {
            mailClient.sendMail(email, "牛客网-忘记密码", content);
        } catch (Exception e) {
            log.error("【用户邮箱地址有误】email = {}", gson.toJson(email));
            e.printStackTrace();
        }

        //返回验证码
        return verifyCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Integer userId) {
        User user = this.findByUserId(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return UserTypeEnum.ADMIN.getDesc();
                    case 2:
                        return UserTypeEnum.MODERATOR.getDesc();
                    default:
                        return UserTypeEnum.USER.getDesc();
                }
            }
        });

        return list;
    }
}
