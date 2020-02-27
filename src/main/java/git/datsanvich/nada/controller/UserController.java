package git.datsanvich.nada.controller;

import git.datsanvich.nada.model.MagicValue;
import git.datsanvich.nada.model.Result;
import git.datsanvich.nada.model.STATUS;
import git.datsanvich.nada.model.UserInformation;
import git.datsanvich.nada.service.UserService;
import git.datsanvich.nada.util.AESUtil;
import git.datsanvich.nada.util.ConversionUtil;
import git.datsanvich.nada.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Author GitDatSanvich
 * @Date 2020/1/20 11:02
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private TokenUtil tokenUtil;

    /**
     * 用户手动登陆
     * 返回UserId
     *
     * @param map userName
     *            passWord
     * @return return
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<String> login(@RequestBody Map<String, String> map) {
        Result<String> result = new Result<>();
        if (!map.containsKey(MagicValue.USER_NAME) || !map.containsKey(MagicValue.PASS_WORD)) {
            result.setCode(STATUS.PARAM_NULL.getValue());
            result.setMsg(STATUS.PARAM_NULL.getName());
            return result;
        }
        logger.info("用户登陆" + ConversionUtil.mapToJsonString(map));
        String userName;
        try {
            userName = userService.login(map.get(MagicValue.USER_NAME), map.get(MagicValue.PASS_WORD));
            if (userName == null) {
                result.setMsg(STATUS.ERROR_RESULT.getName());
                result.setCode(STATUS.ERROR_RESULT.getValue());
                result.setProcess("用户名或者密码有误");
                return result;
            }
            if (userName.equals(MagicValue.STATUE_LOCK)) {
                result.setMsg(STATUS.ERROR_RESULT.getName());
                result.setCode(STATUS.ERROR_RESULT.getValue());
                result.setProcess("用户未验证,请检查邮件");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(STATUS.ERROR.getValue());
            result.setMsg(STATUS.ERROR.getName());
            result.setProcess(e.getMessage());
            return result;
        }
        result.setMsg(STATUS.SUCCESS.getName());
        result.setCode(STATUS.SUCCESS.getValue());
        return result;
    }

    @PostMapping("/loginCheck")
    @ResponseBody
    public Result<Boolean> loginCheck(@RequestBody String userId) {
        Result<Boolean> result = new Result<>();
        if (userId == null || "".equals(userId)) {
            result.setCode(STATUS.PARAM_NULL.getValue());
            result.setMsg(STATUS.PARAM_NULL.getName());
            return result;
        }
        boolean flag = false;
        try {
            flag = userService.loginCheck(userId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(STATUS.ERROR.getValue());
            result.setMsg(STATUS.ERROR.getName());
            result.setProcess(e.getMessage());
        }
        result.setMsg(STATUS.SUCCESS.getName());
        result.setCode(STATUS.SUCCESS.getValue());
        result.setData(flag);
        return result;
    }


    @PostMapping("/signIn")
    @ResponseBody
    public Result signIn(@RequestBody UserInformation userInformation) {
        Result result = new Result<>();
        boolean flag = userInformation.checkNull();
        if (!flag) {
            result.setMsg(STATUS.PARAM_NULL.getName());
            result.setCode(STATUS.PARAM_NULL.getValue());
            return result;
        }
        try {
            flag = userService.signIn(userInformation);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(STATUS.ERROR.getValue());
            result.setMsg(STATUS.ERROR.getName());
            result.setProcess(e.getMessage());
            return result;
        }
        if (flag) {
            result.setMsg(STATUS.SUCCESS.getName());
            result.setCode(STATUS.SUCCESS.getValue());
        } else {
            result.setMsg(STATUS.ERROR.getName());
            result.setCode(STATUS.ERROR.getValue());
        }
        return result;
    }

    @GetMapping("/userActive")
    @ResponseBody
    public String userActive(@RequestParam(value = "userName") String userName, @RequestParam("key") String key) {
        Result result = new Result<>();
        if (userName == null || "".equals(userName) || key == null || "".equals(key)) {
            result.setCode(STATUS.PARAM_NULL.getValue());
            result.setMsg(STATUS.PARAM_NULL.getName());
            return "缺参数呢亲";
        }
        return userService.userActive(userName, key);
    }

    @GetMapping("/getCookie")
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
        try {
            String userName = request.getParameter("userName");
            String cookieValue = tokenUtil.userLogin(userName);
            String encrypt = AESUtil.encrypt(cookieValue, MagicValue.COOKIE_KEY);
            Cookie cookie = new Cookie(MagicValue.SANVICH_TOKEN, encrypt);
            int expiry = 604800;
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "nope";
        }
    }

    @GetMapping("/checkLogin")
    public String checkLogin(@CookieValue(MagicValue.SANVICH_TOKEN) String encrypt) {
        try {
            String cookieValue = AESUtil.decrypt(encrypt, MagicValue.COOKIE_KEY);
            if (cookieValue == null) {
                logger.info("用户自动登录失败 cookie为空");
                return "nope";
            }
            boolean b = tokenUtil.checkLogin(cookieValue);
            if (b) {
                logger.info(cookieValue.substring(2, cookieValue.indexOf(":") - 1) + "自动登录");
                return "ok";
            } else {
                logger.info(cookieValue.substring(2, cookieValue.indexOf(":") - 1) + "自动登录失败");
                return "nope";
            }
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
