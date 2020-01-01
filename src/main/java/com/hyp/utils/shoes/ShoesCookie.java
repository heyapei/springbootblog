package com.hyp.utils.shoes;

import com.hyp.pojo.shoes.dto.ShoesCookieDTO;
import com.hyp.utils.EnDecryption;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 12:40
 * @Description: TODO
 */
@Slf4j
public class ShoesCookie {
    private final static String COOKIE_NAME = "shoesSysCookie";
    private final static String COOKIE_SECRET = "shoes";
    static EnDecryption enDecryption = new EnDecryption();

    public static int getUserId(HttpServletRequest request) {
        int userId = -1;
        ShoesCookieDTO shoesCookieDTO = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String value = cookie.getValue();
                    byte[] bytes = enDecryption.deCryptBase64(value);
                    try {
                        String s = new String(bytes, "utf-8");
                        String substring = s.substring(0, s.indexOf(COOKIE_SECRET));
                        userId = Integer.parseInt(substring);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return userId;
    }


    /**
     * 判断是否登录
     *
     * @param request
     * @return
     */
    public static boolean isLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("存在的cookie名称{}", cookie.getName());
                if (cookie.getName().equals(COOKIE_NAME)) {
                    log.info("存在cookie数据{}", cookie.getName());
                    return true;
                }
            }
        }
        log.info("不存在cookie数据");
        return false;
    }

    /**
     * 写入cookie
     *
     * @param response
     * @param shoesCookieDTO
     */
    public static void writeCookie(HttpServletResponse response, ShoesCookieDTO shoesCookieDTO) {
        String cookieValue = shoesCookieDTO.getUserId() + COOKIE_SECRET + shoesCookieDTO.getTimeStamp();
        String s = enDecryption.enCryptBase64(cookieValue).replaceAll("\r|\n", "");
        log.info("cookie数据：{}" + s);
        Cookie cookie = new Cookie(COOKIE_NAME, s);
        cookie.setPath("/");
        // 设置为10个小时
        cookie.setMaxAge(60 * 60 * 10);
        response.addCookie(cookie);
    }


    /**
     * 读取所有cookie
     * 注意二、从客户端读取Cookie时，包括maxAge在内的其他属性都是不可读的，也不会被提交。浏览器提交Cookie时只会提交name与value属性。maxAge属性只被浏览器用来判断Cookie是否过期
     *
     * @param request
     * @param response
     */
    public void showCookies(HttpServletRequest request, HttpServletResponse response) {
        //这样便可以获取一个cookie数组
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            System.out.println("没有cookie=========");
        } else {
            for (Cookie cookie : cookies) {
                System.out.println("name:" + cookie.getName() + ",value:" + cookie.getValue());
            }
        }

    }


    /**
     * 修改cookie
     *
     * @param request
     * @param response
     * @param name
     * @param value    注意一、修改、删除Cookie时，新建的Cookie除value、maxAge之外的所有属性，例如name、path、domain等，都要与原Cookie完全一样。否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
     */
    public static void editCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            System.out.println("没有cookie==============");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    System.out.println("原值为:" + cookie.getValue());
                    cookie.setValue(value);
                    cookie.setPath("/");
                    cookie.setMaxAge(30 * 60);// 设置为30min
                    System.out.println("被修改的cookie名字为:" + cookie.getName() + ",新值为:" + cookie.getValue());
                    response.addCookie(cookie);
                    break;
                }
            }
        }

    }

    /**
     * 删除cookie
     *
     * @param request
     * @param response
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    cookie.setValue(null);
                    // 立即销毁cookie
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

}
