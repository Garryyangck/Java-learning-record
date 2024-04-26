package garry.community.controller;

import com.google.code.kaptcha.Producer;
import com.google.gson.Gson;
import garry.community.consts.CommunityConst;
import garry.community.enums.ActivationEnum;
import garry.community.enums.MistakeEnum;
import garry.community.enums.MsgEnum;
import garry.community.enums.UserStatusEnum;
import garry.community.form.ForgetPasswordForm;
import garry.community.form.UserLoginForm;
import garry.community.form.UserRegisterForm;
import garry.community.pojo.LoginTicket;
import garry.community.pojo.User;
import garry.community.service.UserService;
import garry.community.utils.CommunityUtil;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * ---------2024/3/16 20:53
 **/
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Controller
public class LoginController {
    @Resource
    private UserService userService;

    @Resource
    private Producer kaptchaProducer;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;//上下文地址

    private final Gson gson = new Gson();

    /**
     * 显示注册页面
     *
     * @return register.html
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * 显示登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    /**
     * 显示忘记密码页面
     *
     * @return
     */
    @RequestMapping(value = "/forget", method = RequestMethod.GET)
    public String getForgetPage() {
        return "/site/forget";
    }

    /**
     * 接收用户传入的注册信息，并判断是否有效
     *
     * @param model
     * @param userRegisterForm
     * @return (1)注册成功: operate-result.html
     * (2)注册失败: register.html(重新注册)
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, UserRegisterForm userRegisterForm) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        Map<String, Object> responseMap = userService.register(user);
        //注册成功，用户需要激活
        if (responseMap == null || responseMap.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已向您发送激活邮件，请尽快激活");
            model.addAttribute("redirectionTime", CommunityConst.AUTO_REDIRECTION_TIME);
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }

        //注册失败，全发给前端(重新注册)，不是null才会处理
        model.addAttribute(MsgEnum.USERNAME_MSG.getMsg(), responseMap.get(MsgEnum.USERNAME_MSG.getMsg()));
        model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), responseMap.get(MsgEnum.PASSWORD_MSG.getMsg()));
        model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), responseMap.get(MsgEnum.EMAIL_MSG.getMsg()));
        return "/site/register";
    }

    /**
     * 接收登录表单的信息，并做出业务判断
     *
     * @param userLoginForm
     * @param model
     * @param owner
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(UserLoginForm userLoginForm,
                        Model model,
                        @CookieValue(value = CommunityConst.KAPTCHA_OWNER_COOKIE_NAME) String owner,
                        HttpServletResponse response) {
        /*
            1.检验验证码
         */
        //验证码过期
        if (StringUtils.isBlank(owner)) {
            model.addAttribute(MsgEnum.VERIFY_CODE_MSG.getMsg(), MistakeEnum.VERIFY_CODE_EXPIRED.getDesc());
            return "/site/login";
        }
        //获取redis中的验证码
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(owner);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String verifyCode = (String) opsForValue.get(kaptchaKey);
        //验证码错误，返回登陆页面
        if (!verifyCode.equals(userLoginForm.getVerifyCode())) {
            model.addAttribute(MsgEnum.VERIFY_CODE_MSG.getMsg(), MistakeEnum.WRONG_VERIFY_CODE.getDesc());
            return "/site/login";
        }

        /*
            2.验证username和password
         */
        Long expiredSeconds = userLoginForm.getRemember() ? CommunityConst.REMEMBER_EXPIRED_SECONDS : CommunityConst.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> responseMap = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword(), expiredSeconds);
        if (responseMap == null) {
            throw new RuntimeException("【登录responseMap创建失败】");
        }

        /*
            3.登录成功
         */
        if (responseMap.containsKey(MsgEnum.LOGIN_TICKET_MSG.getMsg())) {
            //给浏览器发送Cookie
            LoginTicket loginTicket = (LoginTicket) responseMap.get(MsgEnum.LOGIN_TICKET_MSG.getMsg());
            Cookie cookie = new Cookie(CommunityConst.LOGIN_TICKET_COOKIE_NAME, loginTicket.getTicket());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds.equals(CommunityConst.DEFAULT_EXPIRED_SECONDS) ?
                    -1 : expiredSeconds.intValue());
            response.addCookie(cookie);

            //以ticket为key，将登录用户的信息存入redis
            String loginTicketKey = RedisKeyUtil.getLoginTicketKey(loginTicket.getTicket());
            opsForValue = redisTemplate.opsForValue();
            opsForValue.set(loginTicketKey, gson.toJson(loginTicket));//凭证永久存储在redis中

            return "redirect:/index";//重定向到getIndexPages方法，而不是跳转到index.html
        }

        /*
            4.登录失败，全发给前端(重新登录)，不是null才会处理
         */
        model.addAttribute(MsgEnum.USERNAME_MSG.getMsg(), responseMap.get(MsgEnum.USERNAME_MSG.getMsg()));
        model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), responseMap.get(MsgEnum.PASSWORD_MSG.getMsg()));
        return "/site/login";
    }

    /**
     * 验证表单信息，从redis中获取当前的验证码和验证码创建时间，
     * 验证成功后将密码更新，并返回登录页面
     *
     * @param model
     * @param forgetPasswordForm
     * @param owner
     * @return
     */
    @RequestMapping(value = "/forget", method = RequestMethod.POST)
    public String forget(Model model, ForgetPasswordForm forgetPasswordForm,
                         @CookieValue(value = CommunityConst.FORGET_OWNER_COOKIE_NAME) String owner) {
        /*
            1.判空
         */
        if (forgetPasswordForm == null) {
            throw new IllegalArgumentException("【forgetPasswordForm为空】");
        }

        if (StringUtils.isBlank(forgetPasswordForm.getEmail())
                || StringUtils.isBlank(forgetPasswordForm.getVerifyCode())
                || StringUtils.isBlank(forgetPasswordForm.getPassword())) {
            if (StringUtils.isBlank(forgetPasswordForm.getEmail())) {
                model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), MistakeEnum.EMAIL_BLANK.getDesc());
            }
            if (StringUtils.isBlank(forgetPasswordForm.getVerifyCode())) {
                model.addAttribute(MsgEnum.VERIFY_CODE_MSG.getMsg(), MistakeEnum.VERIFY_CODE_BLANK.getDesc());
            }
            if (StringUtils.isBlank(forgetPasswordForm.getPassword())) {
                model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), MistakeEnum.PASSWORD_BLANK.getDesc());
            }
            return "/site/forget";
        }

        /*
            2.检查输入是否正确
         */
        //检查邮箱
        User user = userService.findByEmail(forgetPasswordForm.getEmail());
        if (user == null) {
            model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), MistakeEnum.WRONG_EMAIL.getDesc());
            return "/site/forget";
        }

        //检查验证码
        //验证码过期
        if (StringUtils.isBlank(owner)) {
            model.addAttribute(MsgEnum.VERIFY_CODE_MSG.getMsg(), MistakeEnum.VERIFY_CODE_EXPIRED.getDesc());
            return "/site/forget";
        }
        //获取redis中的验证码
        String forgetKey = RedisKeyUtil.getForgetKey(owner);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String verifyCode = (String) opsForValue.get(forgetKey);
        //验证码错误
        if (!verifyCode.equals(forgetPasswordForm.getVerifyCode())) {
            model.addAttribute(MsgEnum.VERIFY_CODE_MSG.getMsg(), MistakeEnum.WRONG_VERIFY_CODE.getDesc());
            return "/site/forget";
        }

        //检验新密码长度
        if (forgetPasswordForm.getPassword().length() < CommunityConst.MIN_PASSWORD_LENGTH) {
            model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), MistakeEnum.PASSWORD_TOO_SHORT.getDesc());
            return "/site/forget";
        }

        /*
            3.通过检验，修改密码
         */
        String newPassword = CommunityUtil.md5Encryption(forgetPasswordForm.getPassword() + user.getSalt());
        int rows = userService.updatePassword(user.getId(), newPassword);
        if (rows <= 0) {
            throw new RuntimeException("【用户更新失败】user = " + gson.toJson(user));
        }

        model.addAttribute("msg", "密码修改成功，请记住您的新密码");
        model.addAttribute("redirectionTime", CommunityConst.AUTO_REDIRECTION_TIME);
        model.addAttribute("target", "/login");
        return "/site/operate-result";
    }

    /**
     * 获取名为ticket的cookie，然后修改对应登录凭证为无效
     *
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue(value = CommunityConst.LOGIN_TICKET_COOKIE_NAME) String ticket) {
        userService.logout(ticket);
        //清理SecurityContextHolder
        SecurityContextHolder.clearContext();
        return "redirect:/login";//重定向到/login
    }

    /**
     * 根据路径上的userId和activationCode进行账号激活
     *
     * @param model
     * @param userId
     * @param activationCode
     * @return
     */
    @RequestMapping(value = "/activation/{userId}/{activationCode}",
            method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable(value = "userId") Integer userId,
                             @PathVariable(value = "activationCode") String activationCode) {
        ActivationEnum activation = userService.activation(userId, activationCode);
        if (activation.getCode().equals(ActivationEnum.ACTIVATION_SUCCESS.getCode())) {
            model.addAttribute("msg", ActivationEnum.ACTIVATION_SUCCESS.getDesc());
            model.addAttribute("target", "/login");
        } else if (activation.getCode().equals(ActivationEnum.ACTIVATION_REPEAT.getCode())) {
            model.addAttribute("msg", ActivationEnum.ACTIVATION_REPEAT.getDesc());
            model.addAttribute("target", "/index");
        } else if (activation.getCode().equals(ActivationEnum.ACTIVATION_FAIL.getCode())) {
            model.addAttribute("msg", ActivationEnum.ACTIVATION_FAIL.getDesc());
            model.addAttribute("target", "/index");
        }
        model.addAttribute("redirectionTime", CommunityConst.AUTO_REDIRECTION_TIME);
        return "/site/operate-result";
    }

    /**
     * 根据路径上的email向指定邮箱发送验证码，并将验证码写入redis
     *
     * @param model
     * @param response
     * @param email
     * @return
     */
    @RequestMapping(value = "/verifyCode/{email}", method = RequestMethod.GET)
    public String getVerifyCode(Model model, HttpServletResponse response,
                                @PathVariable(value = "email") String email) {
        //用户未输入邮箱
        if (email == null || email.equals("null")) {
            model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), MistakeEnum.EMAIL_BLANK.getDesc());
            model.addAttribute("email", "");
            return "/site/forget";
        }

        User user = userService.findByEmail(email);
        //邮箱错误
        if (user == null) {
            model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), MistakeEnum.WRONG_EMAIL.getDesc());
            return "/site/forget";
        }

        //用户先激活，再发验证码
        if (!user.getStatus().equals(UserStatusEnum.ACTIVATED.getCode())) {
            model.addAttribute(MsgEnum.EMAIL_MSG.getMsg(), "用户还未激活，请先激活用户");
            return "/site/forget";
        }

        //用户已激活，说明用户存在，邮箱正常可以发邮件
        String verifyCode = userService.sendVerifyCode(email);

        //生成验证码凭证owner，存入cookie
        String owner = CommunityUtil.generateUUID().substring(0, CommunityConst.OWNER_LENGTH);
        Cookie cookie = new Cookie(CommunityConst.FORGET_OWNER_COOKIE_NAME, owner);
        cookie.setMaxAge(CommunityConst.FORGET_EFFECTIVE_TIME * 60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        //将验证码写入redis，便于forget方法中验证，并设置redis中的存活时间
        String forgetKey = RedisKeyUtil.getForgetKey(owner);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(forgetKey, verifyCode, CommunityConst.FORGET_EFFECTIVE_TIME, TimeUnit.MINUTES);

        return "/site/forget";
    }

    /**
     * 将验证码图片返回给浏览器，向Cookie写入验证码的凭证owner，并将答案存入redis
     *
     * @param response
     */
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response) {
        //生成验证码
        String text = kaptchaProducer.createText();//验证码的内容
        BufferedImage image = kaptchaProducer.createImage(text);//根据内容生成图片

        //生成验证码凭证owner，存入cookie
        String owner = CommunityUtil.generateUUID().substring(0, CommunityConst.OWNER_LENGTH);
        Cookie cookie = new Cookie(CommunityConst.KAPTCHA_OWNER_COOKIE_NAME, owner);
        cookie.setMaxAge(CommunityConst.KAPTCHA_EFFECTIVE_TIME);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        //将验证码存入redis，并设置有效时间为60秒
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(owner);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(kaptchaKey, text, CommunityConst.KAPTCHA_EFFECTIVE_TIME, TimeUnit.SECONDS);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            //获取传给浏览器的输出流
            ServletOutputStream os = response.getOutputStream();
            //图片传输api
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("【向浏览器输出验证码图片失败】");
        }
    }
}
