package com.hyp.controller.shoes;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.mapper.ShoesUserMapper;
import com.hyp.pojo.shoes.dataobject.ShoesSystem;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.pojo.shoes.dto.ShoesCookieDTO;
import com.hyp.service.shoes.ShoesOrderService;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import com.hyp.utils.shoes.ShoesCookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 12:15
 * @Description: TODO
 */
@Controller
@RequestMapping(value = "home")
@Slf4j
public class Shoes {

    @Autowired
    private com.hyp.service.shoes.ShoesService shoesService;
    @Autowired
    private ShoesOrderService shoesOrderService;
    @Autowired
    private ShoesUserMapper shoesUserMapper;


    /**
     * 查询用户信息界面
     *
     * @return
     */
    @RequestMapping("/userInfoByPage")
    public String userInfoByPage(@RequestParam(required = false) String phoneNum,
                                 @RequestParam(value = "currentPage",required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
        PageHelper.startPage(page, size);
        List<ShoesUser> userInfoByPage;
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        if (phoneNum != null && !phoneNum.equals("")) {
            map.addAttribute("phoneNum", phoneNum);
            criteria.andEqualTo("phoneNum", phoneNum);
            userInfoByPage = shoesUserMapper.selectByExample(example);
        } else {
            example.orderBy("createTime").desc();
            userInfoByPage = shoesUserMapper.selectAll();
        }
        PageInfo pageInfo = new PageInfo(userInfoByPage);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        /*分页*/
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        return "shoes/userInfo";
    }


    /**
     * 访问主页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest httpServletRequest, ModelMap map) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            // 数据上的查询
            int userId = ShoesCookie.getUserId(httpServletRequest);
            log.info("用户ID{}登录", userId);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
            map.addAttribute("shoesSystem", shoesSystem);
            Map<String, String> todayShoesOrder = shoesOrderService.getTodayShoesOrder("2019-12-31 0:0:0", "2019-12-31 23:51:53");
            map.addAttribute("todayData", todayShoesOrder);
            return "shoes/index";
        } else {
            return "forward:/home/loginPage";
        }
    }

    /**
     * 退出系统
     *
     * @return
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelMap modelMap) {
        log.info("用户退出登录");
        ShoesCookie.delCookie(httpServletRequest, httpServletResponse);
        modelMap.addAttribute("errorCode", "GOOD BYE!");
        modelMap.addAttribute("errorDesc", "下次见");
        return "communal/error/error";
    }


    /**
     * 访问登录界面
     *
     * @return
     */
    @RequestMapping("/loginPage")
    public String loginPage() {
        return "shoes/login";
    }


    /**
     * 访问主页面
     *
     * @return
     */
    @RequestMapping("/sysLogin")
    public String sysLogin(@RequestParam String realName, @RequestParam String passWord, HttpServletResponse httpServletResponse, ModelMap modelMap) {
        log.info("{}和{}，登录信息", realName, passWord);
        if (realName == null) {
            modelMap.addAttribute("errorCode", "NOT FOUND");
            modelMap.addAttribute("errorDesc", "账号为必填项");
        }
        if (realName == null) {
            modelMap.addAttribute("errorCode", "NOT FOUND");
            modelMap.addAttribute("errorDesc", "密码为必填项");
        }

        ShoesSystem shoesSystem = shoesService.shoesSystemUserLogin(realName, passWord);
        if (shoesSystem != null) {
            ShoesCookieDTO shoesCookieDTO = new ShoesCookieDTO();
            shoesCookieDTO.setUserId(shoesSystem.getUserId());
            shoesCookieDTO.setTimeStamp(String.valueOf(System.currentTimeMillis()));
            ShoesCookie.writeCookie(httpServletResponse, shoesCookieDTO);
            return "redirect:/home/index";
        } else {
            modelMap.addAttribute("errorCode", "NOT FOUND PERSON");
            modelMap.addAttribute("errorDesc", "未发现用户信息");
        }
        return "communal/error/error";
    }


}
