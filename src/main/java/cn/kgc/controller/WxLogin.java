package cn.kgc.controller;

import cn.kgc.utils.UrlUtils;
import cn.kgc.vo.AccessTokenBean;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2019/4/15.
 */
@RestController
public class WxLogin {

    private String appid="wx9168f76f000a0d4c";
    private String secret = "8ba69d5639242c3bd3a69dffe84336c1";
    private String redirect_uri ="http%3a%2f%2flocalhost%3a8888%2fcallback";


    //登录 第一步：获得code ,code会携带在回调的URI中
    @RequestMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        StringBuilder codeUrl=new StringBuilder("https://open.weixin.qq.com/connect/qrconnect?");
        codeUrl.append("appid="+appid);
        codeUrl.append("&redirect_uri="+redirect_uri);
        codeUrl.append("&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect");

        response.sendRedirect(codeUrl.toString());
    }

    //回调  第二步：通过code获取access_token
    @RequestMapping("/callback")
    public void callback(@RequestParam String code) throws Exception {
        StringBuilder str=new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?");
        str.append("appid=" + appid);
        str.append("&secret="+secret);
        str.append("&code=" + code);
        str.append("&grant_type=authorization_code");

        //请求地址获取access_token，与openid
        String result= UrlUtils.loadURL(str.toString());

        AccessTokenBean accessTokenBean= JSON.parseObject(result,AccessTokenBean.class);

        StringBuilder userInfo=new StringBuilder("https://api.weixin.qq.com/sns/userinfo");
        userInfo.append("?access_token=" + accessTokenBean.getAccess_token());
        userInfo.append("&openid=" + accessTokenBean.getOpenid());

        String userinfoJSON = UrlUtils.loadURL(userInfo.toString());
        Map<String, Object> userinfo = JSON.parseObject(userinfoJSON, Map.class);

        //获取用户基本信息
        String nickname = userinfo.get("nickname").toString();
        String city = userinfo.get("city").toString();
        String sex= userinfo.get("sex").toString();
        String img=userinfo.get("headimgurl").toString();

        System.out.println("昵称：" + nickname + ",城市" + city+",性别"+sex+"，头像"+img);

    }
}
