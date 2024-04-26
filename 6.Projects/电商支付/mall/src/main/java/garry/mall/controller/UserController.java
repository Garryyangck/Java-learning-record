package garry.mall.controller;

import garry.mall.consts.MallConst;
import garry.mall.form.UserLoginForm;
import garry.mall.form.UserRegisterForm;
import garry.mall.pojo.User;
import garry.mall.service.impl.UserServiceImpl;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Garry
 * ---------2024/3/6 20:47
 **/
@SuppressWarnings({"all"})
@Slf4j
@RestController
//@JsonInclude(value = JsonInclude.Include.NON_NULL)//删除Vo中值为null的属性
public class UserController {
    @Resource
    private UserServiceImpl userService;

    /**
     * 注册，检验接收的参数是否非空，对象间属性传递，写入数据库
     *
     * @param userRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid/*表单验证*/ @RequestBody UserRegisterForm userRegisterForm/*json可以以对象形式获取和返回数据，前提是加上@RequestBody注解*/) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);//对象间拷贝，自带方法类
        return userService.register(user);
    }

    /**
     * 登录，检验接收的参数是否非空，将登录对象写入session
     *
     * @param userLoginForm
     * @param bindingResult
     * @param session
     * @return
     */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session) {

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //Data写入session，表示当前处于登录状态
        session.setAttribute(MallConst.CURRENT_USER, userResponseVo.getData());
        log.info("/login sessionId={}", session.getId());

        return userResponseVo;
    }

    /**
     * 检验login中是否成功向session中传入数据（需要登录）
     * 被拦截器拦截，如果当前没有登录用户就会返回NEDD_LOGIN
     *
     * @param session
     * @return
     */
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {
        log.info("/user sessionId={}", session.getId());

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }

    /**
     * 登出，被拦截器拦截，如果当前没有登录用户就会返回NEDD_LOGIN（需要登录）
     * 移除session
     *
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseVo<User> logout(HttpSession session) {
        log.info("/logout sessionId={}", session.getId());//sessionId相同

        session.removeAttribute(MallConst.CURRENT_USER);//移除session
        return ResponseVo.success();
    }
}
