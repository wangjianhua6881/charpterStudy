package com.example.charpter13.controller;


import com.example.charpter13.model.User;
import com.example.charpter13.utils.DatabaseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Log4j
@RestController
@Api(value = "/v1",description = "用户管理系统")
@RequestMapping(value = "/v1")
public class UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private SqlSession sqlSession;

    {
        try {
            sqlSession = DatabaseUtil.getSqlSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录接口，登录之后获取cookies
     * @param response
     * @param user
     * @return boolean
     */
    @ApiOperation(value = "登录接口",httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public boolean login(HttpServletResponse response, @RequestBody User user) throws IOException{
        System.out.println("传进来的用户名为：" + user.getUserName());
        System.out.println("传进来的密码为：" + user.getPassword());

        int result = sqlSession.selectOne("loginUser",user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        logger.info("查询到到结果是:" + result);
        if (result == 1) {
            logger.info("查询到的用户名为：" + user.getUserName());
            logger.info("查询到的密码为:" + user.getPassword());
            logger.info("恭喜您，登录成功！");
            return true;
        }
        return false;
    }

    /**
     * 添加用户接口，验证cookie信息是否正确
     * @param request
     * @param user
     * @return boolean
     */
    @ApiOperation(value = "添加用户接口",httpMethod = "POST")
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public boolean insert(HttpServletRequest request,@RequestBody User user){
        Boolean result = verifyCookie(request);
        int x = 0;
        if (result){
            logger.info("cookies验证成功！");
            x = sqlSession.insert("insertUser",user);
            sqlSession.commit();
            if (x > 0){
                logger.info("添加用户成功！添加数量为：" + x);
                return true;
            }
            logger.info("添加用户失败，或添加数量为0");
            return false;
        }
        logger.info("cookies验证失败！");
        return false;
    }

    @ApiOperation(value = "获取用户列表的接口",httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public List<User> getUserList(HttpServletRequest request, @RequestBody User user){
        boolean result = verifyCookie(request);
        if (result){
            List<User> userList = sqlSession.selectList("getUserInfo",user);
            logger.info("获取到到用户数量为：" + userList.size());
            return userList;
        }
        logger.info("cookies验证失败，请重试！");
        return null;
    }

    @ApiOperation(value = "更新用户接口",httpMethod = "POST")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request,@RequestBody User user){
        boolean result = verifyCookie(request);
        int a = 0;
        if (result){
            a = sqlSession.update("updateUser",user);
            sqlSession.commit();
            if (a > 0){
                logger.info("修改用户成功，修改的数量为：" + a);
                return a;
            }
            logger.info("修改失败，或修改成功的信息数为0");
            return a;
        }
        logger.info("cookies验证失败！");
        return a;
    }

    @ApiOperation(value = "删除用户接口",httpMethod = "POST")
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    public boolean deleteUser(HttpServletRequest request,@RequestBody User user){
        boolean result = verifyCookie(request);
        if (result){
            int a = sqlSession.delete("deleteUser",user);
            sqlSession.commit();
            if (a > 0){
                logger.info("删除用户成功！删除的数量为：" + a);
                return true;
            }
            logger.info("没有用户被删除，或删除失败");
            return false;
        }
        logger.info("cookies验证失败！");
        return false;
    }

    /**
     * cookie验证方法
     * @param request
     * @return boolean
     */
    private boolean verifyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        //判断cookies列表是否为空
        if (Objects.isNull(cookies)){
            logger.info("cookie为空～");
            return false;
        }
        //循环比较key&value如果匹配则放行
        for (Cookie cookie:cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                logger.info("cookie验证成功！");
                return true;
            }
        }
        logger.info("cookies验证失败");
        return false;
    }
}
