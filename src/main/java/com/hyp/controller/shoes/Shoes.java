package com.hyp.controller.shoes;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.mapper.ShoesUserMapper;
import com.hyp.pojo.datatransferobject.NewDTO;
import com.hyp.pojo.datatransferobject.WeatherDTO;
import com.hyp.pojo.shoes.dataobject.*;
import com.hyp.pojo.shoes.dto.ShoesCookieDTO;
import com.hyp.pojo.shoes.utils.DatesUtil;
import com.hyp.pojo.shoes.utils.PrintTest;
import com.hyp.pojo.shoes.vo.OrderItemVO;
import com.hyp.pojo.shoes.vo.RealOrderVO;
import com.hyp.pojo.shoes.vo.ShoesItemAndProduct;
import com.hyp.pojo.shoes.vo.ShoesTicketVO;
import com.hyp.service.shoes.*;
import com.hyp.utils.HttpClientUtil;
import com.hyp.utils.IpUtils;
import com.hyp.utils.JsonUtils;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import com.hyp.utils.shoes.ShoesCookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";

    @Autowired
    private com.hyp.service.shoes.ShoesService shoesService;
    @Autowired
    private ShoesOrderService shoesOrderService;
    @Autowired
    private ShoesUserMapper shoesUserMapper;
    @Autowired
    private ShoesUserService shoesUserService;
    @Autowired
    private ShoesProductService shoesProductService;
    @Autowired
    private ShoesOrderItemService shoesOrderItemService;
    @Autowired
    private ShoesReductionService shoesReductionService;


    /**
     * 添加积分消费情况
     *
     * @return
     */
    @RequestMapping("/addShoesReduction")
    @ResponseBody
    public String addShoesReduction(@RequestParam String phoneNum,
                                    @RequestParam String event,
                                    @RequestParam Integer reduction) {
        ShoesReduction shoesReduction = new ShoesReduction();
        shoesReduction.setEvent(event);
        shoesReduction.setPhoneNum(phoneNum);
        shoesReduction.setReduction(reduction);
        shoesReduction.setCreateDate(new Date());
        boolean b = shoesReductionService.addShoesReduction(shoesReduction);
        if (b) {
            return "积分消费记录成功";
        } else {
            return "积分消费记录失败！请重试";
        }
    }

    /**
     * 添加积分消费情况页面
     *
     * @return
     */
    @RequestMapping("/addShoesReductionPage")
    public String addShoesReductionPage(HttpServletRequest httpServletRequest, ModelMap map, @RequestParam String phoneNum) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }


        ShoesReduction shoesReduction = new ShoesReduction();
        shoesReduction.setPhoneNum(phoneNum);
        List<ShoesReduction> shoesReduction1 = shoesReductionService.getShoesReduction(shoesReduction);
        if (shoesReduction1 != null && shoesReduction1.size() > 0) {
            map.addAttribute("errorCode", "NOT FOUND");
            map.addAttribute("errorDesc", "没有找到用户");
            return "communal/error/error";
        }

        map.addAttribute("phoneNum", phoneNum);
        return "shoes/addShoesReduction";
    }


    /**
     * 积分消费列表页面
     *
     * @return
     */
    @RequestMapping("/getShoesReduction")
    public String getShoesReduction(HttpServletRequest httpServletRequest, ModelMap map,
                                    @RequestParam(required = false) String phoneNum,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        ShoesReduction shoesReduction = new ShoesReduction();
        if (StringUtils.isNotBlank(phoneNum)) {
            shoesReduction.setPhoneNum(phoneNum);
        }
        PageHelper.startPage(page, size);
        List<ShoesReduction> shoesReductionList = shoesReductionService.getShoesReduction(shoesReduction);
        PageInfo pageInfo = new PageInfo(shoesReductionList);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("phoneNum", phoneNum);
        return "shoes/shoesReduction";
    }


    /**
     * 删除积分消费页面
     *
     * @return
     */
    @RequestMapping("/deleteShoesReduction")
    @ResponseBody
    public boolean deleteShoesReduction(@RequestParam Integer id) {
        boolean b = shoesReductionService.deleteShoesReduction(id);
        return b;
    }


    /**
     * 用户购买行为总结
     *
     * @return
     */
    @RequestMapping("/showUserBuy")
    @ResponseBody
    public Map<String, String[]> showUserBuy(@RequestParam Integer userId) {
        SimpleDateFormat formatter = new SimpleDateFormat(shortDateFormat);
        Date pastDate = DatesUtil.getPastDate(365);

        Map<String, String[]> showUserBuy = new HashMap<>(5);
        ShoesOrder shoesOrder = new ShoesOrder();
        shoesOrder.setUserId(userId);
        shoesOrder.setCreateDate(pastDate);
        shoesOrder.setEndDate(new Date());
        List<ShoesOrder> shoesOrderByPhoneAndTime = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
        BigDecimal realMoney = new BigDecimal(0);
        int orderNum = 0;
        int shoesNum = 0;
        double orderAverage = 0;
        double shoesAverage = 0;
        Map<String, Integer> buyRoute = new HashMap<>(16);
        if (shoesOrderByPhoneAndTime != null && shoesOrderByPhoneAndTime.size() > 0) {
            /*购买路线*/
            for (ShoesOrder order : shoesOrderByPhoneAndTime) {
                Integer reduction = order.getReduction();
                realMoney = realMoney.add(order.getMoney().subtract(BigDecimal.valueOf(reduction)));
                ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
                shoesOrderItem.setOrderId(order.getId());
                List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
                if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                    shoesNum += orderItemByShoesOrderItem.size();
                }


                /*统计购买路线*/
                Date createDate = order.getCreateDate();
                String format = formatter.format(createDate);
                if (buyRoute.containsKey(format)) {
                    Integer number = buyRoute.get(format);
                    if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                        number += orderItemByShoesOrderItem.size();
                    }
                    buyRoute.put(format, number);
                } else {
                    if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                        buyRoute.put(format, orderItemByShoesOrderItem.size());
                    }
                }
            }
            orderNum = shoesOrderByPhoneAndTime.size();
        }
        double realMoneyDouble = realMoney.doubleValue();

        /*总收款金额*/
        showUserBuy.put("realMoney", new String[]{String.valueOf(realMoney)});
        /*订单数量*/
        showUserBuy.put("orderNum", new String[]{String.valueOf(orderNum)});
        /*鞋子数量*/
        showUserBuy.put("shoesNum", new String[]{String.valueOf(shoesNum)});
        /*平均每单价格*/
        if (orderNum > 0) {
            orderAverage = realMoneyDouble / orderNum;
            orderAverage = new BigDecimal(String.valueOf(orderAverage)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        showUserBuy.put("orderAverage", new String[]{String.valueOf(orderAverage)});
        /*平均没双鞋子价格*/
        if (shoesNum > 0) {
            shoesAverage = realMoneyDouble / shoesNum;
            shoesAverage = new BigDecimal(String.valueOf(shoesAverage)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        showUserBuy.put("shoesAverage", new String[]{String.valueOf(shoesAverage)});
        String buyDate[] = null;
        String buyNum[] = null;
        String shoesAve[] = null;
        if (buyRoute != null && buyRoute.size() > 0) {
            SimpleDateFormat formatter1 = new SimpleDateFormat(dateFormat);
            int buyRoteSize = buyRoute.size();
            buyDate = new String[buyRoteSize];
            buyNum = new String[buyRoteSize];
            shoesAve = new String[buyRoteSize];
            int i = 0;
            Map<String, String> shoesNum2 = new HashMap<>(16);

            System.out.println("hashMap排序" + buyRoute);

            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(buyRoute.entrySet());

            // 对HashMap中的key 进行排序

            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
//				System.out.println(o1.getKey()+"   ===  "+o2.getKey());
                    return (o1.getKey()).toString().compareTo(o2.getKey().toString());
                }
            });


            System.out.println("hashMap排序hou" + buyRoute);

            Map<String, Integer> sortMap = new TreeMap<>();

            for(Map.Entry<String, Integer> mapping:list){
                sortMap.put(mapping.getKey(), mapping.getValue());
            }




            for (String key : buyRoute.keySet()) {
                String value = buyRoute.get(key).toString();
                String startDate = key + " 00:00:00";
                String endDate = key + " 23:59:59";
                log.info(startDate + "查看一下日期格式");
                try {
                    shoesOrder.setCreateDate(formatter1.parse(startDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    shoesOrder.setEndDate(formatter1.parse(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<ShoesOrder> shoesOrderByPhoneAndTime1 = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
                System.out.println("结算1：" + shoesOrderByPhoneAndTime1);
                BigDecimal realMoney1 = new BigDecimal(0);
                double shoesAve1 = 0d;
                int shoesNum1 = 0;
                if (shoesOrderByPhoneAndTime1 != null && shoesOrderByPhoneAndTime1.size() > 0) {
                    for (ShoesOrder order : shoesOrderByPhoneAndTime1) {

                        Integer reduction = order.getReduction();
                        realMoney1 = realMoney1.add(order.getMoney().subtract(BigDecimal.valueOf(reduction)));

                        if (!shoesNum2.containsKey(key)) {
                            shoesNum1 = 0;
                            shoesNum2.put(key, String.valueOf(shoesNum1));
                        }

                        ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
                        shoesOrderItem.setOrderId(order.getId());
                        List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
                        if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                            shoesNum1 += orderItemByShoesOrderItem.size();
                        }
                    }

                }

                log.info("个人总金额" + realMoney1.doubleValue() + "==鞋子数量" + shoesNum1);
                if (shoesNum1 > 0) {
                    shoesAve1 = realMoney1.doubleValue() / shoesNum1;
                    shoesAve1 = new BigDecimal(String.valueOf(shoesAve1)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                buyDate[i] = key;
                System.out.println("结算2：" + buyDate[i]);
                buyNum[i] = value;
                shoesAve[i] = String.valueOf(shoesAve1);
                i++;
            }
        }
        showUserBuy.put("buyDate", buyDate);
        showUserBuy.put("buyNum", buyNum);
        showUserBuy.put("shoesAve", shoesAve);


        return showUserBuy;
    }


    /**
     * 跳转到数据分析页面
     *
     * @return
     */
    @RequestMapping("/dataStatistics")
    public String dataStatistics(HttpServletRequest httpServletRequest, ModelMap map) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }
        return "shoes/dataStatistics";
    }

    /**
     * 获取五年内年度数据-年度数据查询
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping("/yearStatistics")
    @ResponseBody
    public Result yearStatistics(HttpServletRequest httpServletRequest) {

        Result resultObject = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);
        if (!isRightSystem) {
            return resultObject;
        }

        Map<String, List<String>> map = new HashMap<>(5);

        List<String> yearList = new ArrayList<>();
        List<String> peopleList = new ArrayList<>();
        List<String> orderList = new ArrayList<>();
        List<String> moneyList = new ArrayList<>();
        List<String> realMoneyList = new ArrayList<>();
        Integer nowYear = DatesUtil.getNowYear();

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // 获取五年内年度数据
        for (int i = 0; i < 5; i++) {


            String startDate = nowYear + "-01-01 00:00:00";
            String endDate = nowYear + "-12-31 23:59:59";

            ShoesOrder shoesOrder = new ShoesOrder();
            try {
                shoesOrder.setCreateDate(formatter.parse(startDate));
                shoesOrder.setEndDate(formatter.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<ShoesOrder> shoesOrderByPhoneAndTime = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
            if (shoesOrderByPhoneAndTime != null && shoesOrderByPhoneAndTime.size() > 0) {

                BigDecimal money = new BigDecimal(0);
                BigDecimal realMoney = new BigDecimal(0);
                Set<Integer> peopleNum = new HashSet<>();
                for (ShoesOrder order : shoesOrderByPhoneAndTime) {
                    peopleNum.add(order.getUserId());
                    money = order.getMoney().add(money);
                    Integer reduction = order.getReduction();
                    realMoney = realMoney.add(order.getMoney().subtract(BigDecimal.valueOf(reduction)));
                }


                yearList.add(String.valueOf(nowYear));
                orderList.add(String.valueOf(shoesOrderByPhoneAndTime.size()));
                peopleList.add(String.valueOf(peopleNum.size()));
                moneyList.add(String.valueOf(money));
                realMoneyList.add(String.valueOf(realMoney));
            }
            map.put("yearList", yearList);
            map.put("peopleList", peopleList);
            map.put("orderList", orderList);
            map.put("moneyList", moneyList);
            map.put("realMoneyList", realMoneyList);

            nowYear--;
        }

        resultObject = ResultGenerator.genSuccessResult(map);
        return resultObject;
    }


    /**
     * 具体的月度数据
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping("/monthStatistics")
    @ResponseBody
    public Result monthStatistics(HttpServletRequest httpServletRequest, @RequestParam(required = false) Integer searchYear) {

        Result resultObject = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);
        if (!isRightSystem) {
            return resultObject;
        }

        Map<String, List<String>> map = new HashMap<>(5);

        List<String> monthList = new ArrayList<>();
        List<String> peopleList = new ArrayList<>();
        List<String> orderList = new ArrayList<>();
        List<String> moneyList = new ArrayList<>();
        List<String> realMoneyList = new ArrayList<>();
        String tip;

        if (searchYear == null || searchYear.equals("")) {
            searchYear = DatesUtil.getNowYear();
        }
        tip = String.valueOf(searchYear);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // 获取五年内年度数据
        for (int i = 12; i > 0; i--) {

            String startDate = searchYear + "-" + i + "-01 00:00:00";
            String endDate = searchYear + "-" + i + "-31 23:59:59";

            ShoesOrder shoesOrder = new ShoesOrder();
            try {
                shoesOrder.setCreateDate(formatter.parse(startDate));
                shoesOrder.setEndDate(formatter.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<ShoesOrder> shoesOrderByPhoneAndTime = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
            if (shoesOrderByPhoneAndTime != null && shoesOrderByPhoneAndTime.size() > 0) {

                BigDecimal money = new BigDecimal(0);
                BigDecimal realMoney = new BigDecimal(0);

                Set<Integer> peopleNum = new HashSet<>();
                for (ShoesOrder order : shoesOrderByPhoneAndTime) {
                    peopleNum.add(order.getUserId());
                    money = order.getMoney().add(money);
                    Integer reduction = order.getReduction();
                    realMoney = realMoney.add(order.getMoney().subtract(BigDecimal.valueOf(reduction)));
                }

                monthList.add(String.valueOf(i));
                orderList.add(String.valueOf(shoesOrderByPhoneAndTime.size()));
                peopleList.add(String.valueOf(peopleNum.size()));
                moneyList.add(String.valueOf(money));
                realMoneyList.add(String.valueOf(realMoney));
            }

            List<String> tips = new ArrayList<>();
            tips.add(tip);
            map.put("tips", tips);

            map.put("monthList", monthList);
            map.put("peopleList", peopleList);
            map.put("orderList", orderList);
            map.put("moneyList", moneyList);
            map.put("realMoneyList", realMoneyList);

        }
        resultObject = ResultGenerator.genSuccessResult(map);
        return resultObject;
    }


    /**
     * 销售和销售额的统计数据
     * 本来是要给图表的 暂时不适用
     *
     * @return
     */
    @RequestMapping("/saleAndAmountChart")
    @ResponseBody
    public List<NewDTO> saleAndAmountChart() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String newsReturn = httpClientUtil.getParameter("http://v.juhe.cn/toutiao/index?type=shishang&key=320fe5990868976ec7a68fa3627c7fe2", null, null, 2000, 2000, 2000);
        JSONObject parse = JSONObject.parseObject(newsReturn);
        JSONObject result = parse.getJSONObject("result");
        String data = result.getString("data");
        List<NewDTO> NewDTOs = JsonUtils.jsonToList(data, NewDTO.class);
        return NewDTOs;
    }


    /**
     * 返回新闻
     *
     * @return
     */
    @RequestMapping("/getNews")
    @ResponseBody
    public List<NewDTO> getNews() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String newsReturn = httpClientUtil.getParameter("http://v.juhe.cn/toutiao/index?type=shishang&key=320fe5990868976ec7a68fa3627c7fe2", null, null, 2000, 2000, 2000);
        JSONObject parse = JSONObject.parseObject(newsReturn);
        JSONObject result = parse.getJSONObject("result");
        String data = result.getString("data");
        List<NewDTO> NewDTOs = JsonUtils.jsonToList(data, NewDTO.class);
        return NewDTOs;
    }

    /**
     * 返回热销产品的信息
     *
     * @return
     */
    @RequestMapping("/getHotSale")
    @ResponseBody
    public Map<String, String[]> getHotSale(@RequestParam(required = false, defaultValue = "1") Integer countDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // 1 本周 2 上个月 3 今年全部 4 去年全部 5 所有全部 6 本月 7 最近三个月
        String tip = null;
        String startDate = null;
        String endDate = null;
        switch (countDate) {
            case 1:
                tip = "本周产品热销排名（前20）";
                startDate = formatter.format(DatesUtil.getBeginDayOfWeek());
                endDate = formatter.format(DatesUtil.getEndDayOfWeek());
                break;
            case 2:
                tip = "上个月产品热销排名（前20）";
                startDate = formatter.format(DatesUtil.getBeginDayOfLastMonth());
                endDate = formatter.format(DatesUtil.getEndDayOfLastMonth());
                break;
            case 6:
                tip = "本月产品热销排名（前20）";
                startDate = formatter.format(DatesUtil.getBeginDayOfMonth());
                endDate = formatter.format(DatesUtil.getEndDayOfMonth());
                break;
            case 3:
                tip = "今年产品热销排名（前20）";
                startDate = formatter.format(DatesUtil.getBeginDayOfYear());
                endDate = formatter.format(DatesUtil.getEndDayOfYear());
                break;
            case 4:
                tip = "去年产品热销排名（前20）";
                startDate = DatesUtil.getNowYear() - 1 + "-01-01 00:00:00";
                endDate = DatesUtil.getNowYear() + "-01-01 00:00:00";
                break;
            case 7:
                tip = "近三个月产品热销排名（前20）";
                //当前时间
                Date dNow = DatesUtil.getBeginDayOfMonth();
                //得到日历
                Calendar calendar = Calendar.getInstance();
                //把当前时间赋给日历
                calendar.setTime(dNow);
                //设置为前3月
                calendar.add(Calendar.MONTH, -3);
                //得到前3月的时间
                Date dBefore = calendar.getTime();
                startDate = formatter.format(dBefore);
                endDate = formatter.format(DatesUtil.getEndDayOfMonth());
                break;
            case 5:
            default:
                tip = "热销产品排名（前20）";
                break;

        }

        Map<String, String[]> hotsale = new HashMap<>(2);

        List<ShoesOrderItem> shoesOrderItems = shoesOrderItemService.hotSaleCount(20, startDate, endDate);
        int size = shoesOrderItems.size();
        if (shoesOrderItems != null && size > 0) {
            String[] countNum = new String[size];
            String[] productName = new String[size];
            for (int i = 0; i < shoesOrderItems.size(); i++) {
                countNum[i] = String.valueOf(shoesOrderItems.get(i).getNumber());
                productName[i] = shoesProductService.getProductInfoByProductId(shoesOrderItems.get(i).getProductId()).getName();
            }
            hotsale.put("countNum", countNum);
            hotsale.put("productName", productName);
        }
        String[] tips = new String[1];
        tips[0] = tip;
        hotsale.put("tips", tips);
        return hotsale;
    }


    /**
     * 返回天气的信息
     *
     * @return
     */
    @RequestMapping("/getWeather")
    @ResponseBody
    public List<WeatherDTO> getWeather(HttpServletRequest request) {

        HttpClientUtil httpClientUtil = new HttpClientUtil();

        String ipFromRequest = IpUtils.getIpFromRequest(request);
        log.info("请求地址：" + ipFromRequest);
        //读取json
        //a6189d40a6e51b60ef1e55f7593fe962

        String weatherReturn = httpClientUtil.getParameter("http://t.weather.sojson.com/api/weather/city/101180602", null, null, 2000, 2000, 2000);
       /* String weatherReturn = "{\"message\":\"success感谢又拍云(upyun.com)提供CDN赞助\"," +
                "\"status\":200,\"date\":\"20191130\",\"time\":\"2019-11-30 14:50:13\"," +
                "\"cityInfo\":{\"city\":\"青浦区\",\"citykey\":\"101020800\",\"parent\":\"上海市\",\"updateTime\":\"14:00\"}," +
                "\"data\":{\"shidu\":\"90%\",\"pm25\":21.0,\"pm10\":39.0,\"quality\":\"优\",\"wendu\":" +
                "\"11\",\"ganmao\":\"各类人群可自由活动\",\"forecast\":[{\"date\":\"30\"," +
                "\"high\":\"高温 13℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-30\",\"week\":\"星期六\",\"sunrise\":\"06:34\",\"sunset\":\"16:53\",\"aqi\":47,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"01\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-01\",\"week\":\"星期日\",\"sunrise\":\"06:35\",\"sunset\":\"16:53\",\"aqi\":122,\"fx\":\"西北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"02\",\"high\":\"高温 11℃\",\"low\":\"低温 0℃\",\"ymd\":\"2019-12-02\",\"week\":\"星期一\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":57,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"},{\"date\":\"03\",\"high\":\"高温 11℃\",\"low\":\"低温 -1℃\",\"ymd\":\"2019-12-03\",\"week\":\"星期二\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":56,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"04\",\"high\":\"高温 12℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-04\",\"week\":\"星期三\",\"sunrise\":\"06:37\",\"sunset\":\"16:53\",\"aqi\":67,\"fx\":\"北风\",\"fl\":\"4-5级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"05\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-05\",\"week\":\"星期四\",\"sunrise\":\"06:38\",\"sunset\":\"16:53\",\"aqi\":48,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"06\",\"high\":\"高温 9℃\",\"low\":\"低温 3℃\",\"ymd\":\"2019-12-06\",\"week\":\"星期五\",\"sunrise\":\"06:39\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"07\",\"high\":\"高温 9℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-07\",\"week\":\"星期六\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"08\",\"high\":\"高温 10℃\",\"low\":\"低温 4℃\",\"ymd\":\"2019-12-08\",\"week\":\"星期日\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"09\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-09\",\"week\":\"星期一\",\"sunrise\":\"06:41\",\"sunset\":\"16:53\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"10\",\"high\":\"高温 14℃\",\"low\":\"低温 8℃\",\"ymd\":\"2019-12-10\",\"week\":\"星期二\",\"sunrise\":\"06:42\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"11\",\"high\":\"高温 14℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-11\",\"week\":\"星期三\",\"sunrise\":\"06:43\",\"sunset\":\"16:53\",\"fx\":\"西北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"12\",\"high\":\"高温 10℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-12\",\"week\":\"星期四\",\"sunrise\":\"06:43\",\"sunset\":\"16:54\",\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"13\",\"high\":\"高温 10℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-13\",\"week\":\"星期五\",\"sunrise\":\"06:44\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"14\",\"high\":\"高温 12℃\",\"low\":\"低温 7℃\",\"ymd\":\"2019-12-14\",\"week\":\"星期六\",\"sunrise\":\"06:45\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"}],\"yesterday\":{\"date\":\"29\",\"high\":\"高温 12℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-29\",\"week\":\"星期五\",\"sunrise\":\"06:33\",\"sunset\":\"16:53\",\"aqi\":32,\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"}}}";*/
        JSONObject parse = JSONObject.parseObject(weatherReturn);
        JSONObject data = parse.getJSONObject("data");
        JSONArray forecast = data.getJSONArray("forecast");
        List<WeatherDTO> weatherDTOS = new ArrayList<>();
        for (Object o : forecast) {
            WeatherDTO weatherDTO = JsonUtils.jsonToPojo(o.toString(), WeatherDTO.class);
            weatherDTO.setHigh(weatherDTO.getHigh().substring(3, weatherDTO.getHigh().indexOf("℃")));
            weatherDTO.setLow(weatherDTO.getLow().substring(3, weatherDTO.getLow().indexOf("℃")));
            weatherDTOS.add(weatherDTO);
        }
        return weatherDTOS;
    }


    /**
     * 删除订单
     *
     * @return
     */
    @RequestMapping("/deleteOrderByOrderId")
    @ResponseBody
    public Result deleteOrderByOrderId(HttpServletRequest httpServletRequest, @RequestParam Integer orderId) {

        Result resultObject = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);

        if (isRightSystem) {
            ShoesOrder shoesOrderByOrderId = shoesOrderService.getShoesOrderByOrderId(orderId);
            if (shoesOrderByOrderId == null) {
                resultObject = ResultGenerator.genFailResult("没有找到订单数据");
            } else {
                BigDecimal money = shoesOrderByOrderId.getMoney();
                Integer reduction = shoesOrderByOrderId.getReduction();
                ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(shoesOrderByOrderId.getUserId());
                shoesUser.setId(shoesOrderByOrderId.getUserId());
                // 计算出来最后的积分值
                money = money.subtract(BigDecimal.valueOf(reduction));
                Integer empirical = shoesUser.getEmpirical() - money.intValue();
                shoesUser.setEmpirical(empirical);
                shoesUserMapper.updateByPrimaryKey(shoesUser);
                shoesOrderByOrderId.setState(2);
                int i = shoesOrderService.updateShoesOrder(shoesOrderByOrderId);
                if (i > 0) {
                    shoesOrderService.deleteOrderAndOrderItem(orderId);
                    resultObject = ResultGenerator.genSuccessResult();
                } else {
                    resultObject = ResultGenerator.genFailResult("确认订单错误");
                }
            }
        }

        return resultObject;
    }


    /**
     * 通过orderId获取详细内容
     */
    @RequestMapping("/showOrderItemByOrderId")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String orderItemInfoByPage(HttpServletRequest httpServletRequest,
                                      @RequestParam Integer orderId,
                                      @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        if (orderId == null) {
            map.addAttribute("pageResult", null);
            map.addAttribute("indexPage", null);
            map.addAttribute("totalPage", null);
            map.addAttribute("phoneNum", null);
            return "shoes/orderItemInfo";
        }


        ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
        shoesOrderItem.setOrderId(orderId);
        List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);

        PageHelper.startPage(page, size);
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() >= 0) {

            for (ShoesOrderItem orderItem : orderItemByShoesOrderItem) {
                OrderItemVO orderItemVO = new OrderItemVO();
                ShoesProduct productInfoByProductId = shoesProductService.getProductInfoByProductId(orderItem.getProductId());
                BeanUtils.copyProperties(productInfoByProductId, orderItemVO);
                BeanUtils.copyProperties(orderItem, orderItemVO);
                orderItemVO.setOrderId(orderItem.getOrderId());
                orderItemVO.setCreateDate(orderItem.getCreateDate());
                orderItemVOS.add(orderItemVO);
            }
            /*ShoesOrderItem orderItem = orderItemByShoesOrderItem.get(0);*/

        }

        PageInfo pageInfo = new PageInfo(orderItemVOS);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("phoneNum", null);
        return "shoes/orderItemInfo";
    }


    /**
     * 打印小票
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping("/printTicket")
    @ResponseBody
    public Result printTicket(HttpServletRequest httpServletRequest, @RequestParam Integer orderId) {

        Result resultObject = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);

        if (isRightSystem) {
            ShoesOrder shoesOrderByOrderId = shoesOrderService.getShoesOrderByOrderId(orderId);
            if (shoesOrderByOrderId == null) {
                resultObject = ResultGenerator.genFailResult("没有找到订单数据");
            } else {
                BigDecimal money = shoesOrderByOrderId.getMoney();
                Integer reduction = shoesOrderByOrderId.getReduction();
                ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(shoesOrderByOrderId.getUserId());
                shoesUser.setId(shoesOrderByOrderId.getUserId());
                // 计算出来最后的积分值
                money = money.subtract(BigDecimal.valueOf(reduction));
                Integer empirical = shoesUser.getEmpirical() + money.intValue();
                shoesUser.setEmpirical(empirical);
                shoesUserMapper.updateByPrimaryKey(shoesUser);
                shoesOrderByOrderId.setState(2);
                int i = shoesOrderService.updateShoesOrder(shoesOrderByOrderId);
                if (i > 0) {
                    resultObject = ResultGenerator.genSuccessResult();
                    ShoesTicketVO shoesTicketVO = new ShoesTicketVO();
                    shoesTicketVO.setShoesOrder(shoesOrderByOrderId);
                    shoesTicketVO.setShoesUser(shoesUserMapper.selectByPrimaryKey(shoesUser.getId()));
                    ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
                    shoesOrderItem.setOrderId(orderId);
                    List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
                    log.info(orderItemByShoesOrderItem.toString());
                    List<ShoesItemAndProduct> shoesItemAndProductList = new ArrayList<>();

                    for (ShoesOrderItem orderItem : orderItemByShoesOrderItem) {
                        ShoesItemAndProduct shoesItemAndProduct = new ShoesItemAndProduct();
                        ShoesProduct productInfoByProductId = shoesProductService.getProductInfoByProductId(orderItem.getProductId());
                        shoesItemAndProduct.setColor(productInfoByProductId.getColor());
                        shoesItemAndProduct.setSize(productInfoByProductId.getSize());
                        shoesItemAndProduct.setName(productInfoByProductId.getName());
                        shoesItemAndProduct.setPrice(productInfoByProductId.getPrice());
                        shoesItemAndProduct.setNumber(orderItem.getNumber());
                        shoesItemAndProductList.add(shoesItemAndProduct);
                    }
                    shoesTicketVO.setShoesItemAndProductList(shoesItemAndProductList);
                    //打印小票
                    new PrintTest().print3(shoesTicketVO);
                } else {
                    resultObject = ResultGenerator.genFailResult("确认订单错误");
                }
            }
        }


        return resultObject;
    }


    /**
     * 跳转到真实订单的页面
     *
     * @return
     */
    @RequestMapping("/realOrderPage")
    public String realOrderPage(HttpServletRequest httpServletRequest, @RequestParam Integer orderId,
                                ModelMap modelMap) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                modelMap.addAttribute("errorCode", "NOT FOUND");
                modelMap.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            modelMap.addAttribute("errorCode", "NO COOKIE");
            modelMap.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        ShoesOrder shoesOrderByOrderId = shoesOrderService.getShoesOrderByOrderId(orderId);
        ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(shoesOrderByOrderId.getUserId());
        ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
        shoesOrderItem.setOrderId(orderId);
        List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
        List<RealOrderVO> realOrderVOS = new ArrayList<>();
        for (ShoesOrderItem orderItem : orderItemByShoesOrderItem) {
            RealOrderVO realOrderVO = new RealOrderVO();
            ShoesProduct productInfoByProductId = shoesProductService.getProductInfoByProductId(orderItem.getProductId());
            /*realOrderVO.setProductCode(productInfoByProductId.getCode());
            realOrderVO.setProductName(productInfoByProductId.getName());
            realOrderVO.setPrice(orderItem.getPrice());
            realOrderVO.setMoney(orderItem.getMoney());
            realOrderVO.setNumber(orderItem.getNumber());*/
            BeanUtils.copyProperties(productInfoByProductId, realOrderVO);
            BeanUtils.copyProperties(orderItem, realOrderVO);
            realOrderVOS.add(realOrderVO);
        }
        modelMap.addAttribute("shoesOrder", shoesOrderByOrderId);
        modelMap.addAttribute("shoesUser", shoesUser);
        modelMap.addAttribute("realOrderVOList", realOrderVOS);
        return "shoes/realOrder";
    }


    /**
     * 查询订单详情列表
     */
    @RequestMapping("/orderItemInfoByPage")
    public String orderItemInfoByPage(HttpServletRequest httpServletRequest,
                                      @RequestParam(value = "orderItemInfo-phoneNum", required = false) String phoneNum,
                                      @RequestParam(value = "orderInfo-startDate", required = false) String startDate,
                                      @RequestParam(value = "orderInfo-endDate", required = false) String endDate,
                                      @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        if (phoneNum == null) {
            map.addAttribute("pageResult", null);
            map.addAttribute("indexPage", null);
            map.addAttribute("totalPage", null);
            map.addAttribute("phoneNum", null);
            return "shoes/orderItemInfo";
        }

        ShoesUser shoesUser = null;
        Example example = new Example(ShoesUser.class);
        Example.Criteria criteria = example.createCriteria();
        if (phoneNum != null) {
            criteria.andEqualTo("phoneNum", phoneNum);
            shoesUser = shoesUserMapper.selectOneByExample(example);
        }
        List<Integer> orderIdList = new ArrayList<>();
        ShoesOrderItem shoesOrderItem = null;
        if (shoesUser != null) {
            List<ShoesOrder> shoesOrderByUserId = shoesOrderService.getShoesOrderByUserId(shoesUser.getId());
            for (ShoesOrder shoesOrder : shoesOrderByUserId) {
                orderIdList.add(shoesOrder.getId());
            }
        } else {
            // 如果没有查找到用户也不显示任何内容
            map.addAttribute("pageResult", null);
            map.addAttribute("indexPage", null);
            map.addAttribute("totalPage", null);
            map.addAttribute("phoneNum", phoneNum);
            return "shoes/orderItemInfo";
        }

        List<ShoesOrderItem> orderItemByShoesOrderItem = null;
        if (orderIdList.size() > 0) {
            for (Integer orderId : orderIdList) {
                shoesOrderItem = new ShoesOrderItem();
                shoesOrderItem.setOrderId(orderId);
                orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
            }
        } else {
            shoesOrderItem = new ShoesOrderItem();
            orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
        }

        PageHelper.startPage(page, size);
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        if (orderItemByShoesOrderItem != null) {
            for (ShoesOrderItem orderItem : orderItemByShoesOrderItem) {
                OrderItemVO orderItemVO = new OrderItemVO();
                ShoesProduct productInfoByProductId = shoesProductService.getProductInfoByProductId(orderItem.getProductId());
                BeanUtils.copyProperties(productInfoByProductId, orderItemVO);
                BeanUtils.copyProperties(orderItem, orderItemVO);
                orderItemVO.setOrderId(orderItem.getOrderId());
                orderItemVO.setCreateDate(orderItem.getCreateDate());
                orderItemVOS.add(orderItemVO);
            }
        }

        PageInfo pageInfo = new PageInfo(orderItemVOS);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("phoneNum", phoneNum);
        return "shoes/orderItemInfo";
    }


    /**
     * 查询订单列表
     */
    @RequestMapping("/orderInfoByPage")
    public String orderInfoByPage(HttpServletRequest httpServletRequest,
                                  @RequestParam(value = "orderInfo-phoneNum", required = false) String phoneNum,
                                  @RequestParam(value = "orderInfo-startDate", required = false) String startDate,
                                  @RequestParam(value = "orderInfo-endDate", required = false) String endDate,
                                  @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        ShoesOrder shoesOrder = new ShoesOrder();

        int userId = 0;
        Example example = new Example(ShoesUser.class);
        Example.Criteria criteria = example.createCriteria();
        List<ShoesUser> shoesUserList = null;
        if (phoneNum != null && !phoneNum.equals("")) {
            criteria.andEqualTo("phoneNum", phoneNum);
            shoesUserList = shoesUserMapper.selectByExample(example);
        }

        ShoesUser shoesUser = null;
        if (shoesUserList != null && shoesUserList.size() > 0) {
            shoesUser = shoesUserList.get(0);
            userId = shoesUser.getId();
            shoesOrder.setUserId(userId);
        } else if (StringUtils.isNotBlank(phoneNum)) {
            shoesOrder.setUserId(userId);
        }


        if (StringUtils.isNotBlank(startDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat(shortDateFormat);

            startDate = startDate.replaceAll(",", "");
            log.info(startDate + "开始时间");
            try {
                shoesOrder.setCreateDate(formatter.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            endDate = endDate.replaceAll(",", "") + " 23:59:59";
            log.info("结束时间：" + endDate);
            try {
                shoesOrder.setEndDate(formatter.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        log.info(shoesOrder.toString() + "开始时间");
        /*if (userId > 0) {
            shoesOrder.setUserId(userId);
        } else {
            map.addAttribute("pageResult", null);
            map.addAttribute("indexPage", 0);
            map.addAttribute("totalPage", 0);
            map.addAttribute("phoneNum", phoneNum);
            map.addAttribute("shoesOrder", shoesOrder);
            return "shoes/orderInfo";
        }*/


        PageHelper.startPage(page, size);
        List<ShoesOrder> shoesOrderByPhoneAndTime = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
        if (shoesOrderByPhoneAndTime == null || shoesOrderByPhoneAndTime.size() <= 0) {
            map.addAttribute("pageResult", null);
            map.addAttribute("indexPage", 0);
            map.addAttribute("totalPage", 0);
            map.addAttribute("phoneNum", phoneNum);
            map.addAttribute("shoesOrder", shoesOrder);
            return "shoes/orderInfo";
        }
        PageInfo pageInfo = new PageInfo(shoesOrderByPhoneAndTime);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("phoneNum", phoneNum);
        map.addAttribute("shoesOrder", shoesOrder);
        return "shoes/orderInfo";
    }


    /**
     * 关键字搜索
     *
     * @return
     */
    @RequestMapping("/keySearchByShoesCode")
    @ResponseBody
    public Result keySearchByShoesCode(@RequestParam String shoesCode) {

        Result resultObject = null;

        if (shoesCode == null) {
            resultObject = ResultGenerator.genFailResult("shoesCode不能为空");
        }

        if (resultObject == null || resultObject.getCode() == 200) {
            List<ShoesProduct> productInfoByProductCode = shoesProductService.getShoesProductByKeyWordShoesCode(shoesCode);
            resultObject = ResultGenerator.genSuccessResult(productInfoByProductCode);

        } else {
            resultObject = ResultGenerator.genFailResult("数据整理失败");
        }

        return resultObject;
    }

    /**
     * 最后下单
     *
     * @return
     */
    @RequestMapping("/realAddOrder")
    @ResponseBody
    public Result realAddOrder(HttpServletRequest httpServletRequest, @RequestParam Integer orderId,
                               @RequestParam(required = false, defaultValue = "0") Integer orderReduction) {

        Result resultObject = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);
        if (isRightSystem) {
            ShoesOrder shoesOrderByOrderId = shoesOrderService.getShoesOrderByOrderId(orderId);
            if (shoesOrderByOrderId == null) {
                resultObject = ResultGenerator.genFailResult("没有找到订单数据");
            } else {
                shoesOrderByOrderId.setReduction(orderReduction);
                // 积分按照真实付款金额1:1计算
                shoesOrderByOrderId.setEmpirical(shoesOrderByOrderId.getMoney().intValue());
                //shoesOrderByOrderId.setState(2);
                int i = shoesOrderService.updateShoesOrder(shoesOrderByOrderId);
                if (i > 0) {
                    resultObject = ResultGenerator.genSuccessResult();
                    //打印小票
                    //PrintTest.print1();
                } else {
                    resultObject = ResultGenerator.genFailResult("确认订单错误");
                }

            }
        }


        return resultObject;
    }


    /**
     * 更新订单详情中数据
     *
     * @return
     */
    @RequestMapping("/updateOrderItemById")
    @ResponseBody
    public Result updateOrderItemById(HttpServletRequest httpServletRequest, @RequestParam Integer orderItemId,
                                      @RequestParam Integer orderItemNumber) {

        Result resultObject = null;


        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        if (resultObject == null || resultObject.getCode() == 200) {
            if (orderItemId == null) {
                resultObject = ResultGenerator.genFailResult("orderItemId不能为空");
            }
        }

        if (resultObject == null || resultObject.getCode() == 200) {
            if (orderItemNumber == null) {
                resultObject = ResultGenerator.genFailResult("orderItemId不能为空");
            }
        }


        if (resultObject == null || resultObject.getCode() == 200) {


            ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
            shoesOrderItem.setId(orderItemId);
            shoesOrderItem.setNumber(orderItemNumber);
            List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
            if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                shoesOrderItem = orderItemByShoesOrderItem.get(0);
                shoesOrderItem.setNumber(orderItemNumber);
                shoesOrderItem.setMoney(shoesOrderItem.getPrice().multiply(new BigDecimal(String.valueOf(orderItemNumber))));

                log.info("orderitem数据：" + shoesOrderItem.toString());

                int i = shoesOrderItemService.updateShoesOrderItemByShoesOrderItem(shoesOrderItem);
                if (i < 0) {
                    resultObject = ResultGenerator.genFailResult("更新数据失败");
                } else {
                    ShoesOrderItem shoesOrderItem1 = new ShoesOrderItem();
                    shoesOrderItem1.setOrderId(shoesOrderItem.getOrderId());
                    log.info("orderitem1数据：" + shoesOrderItem1.toString());
                    List<ShoesOrderItem> shoesOrderItemByShoesOrderItem = shoesOrderItemService.getShoesOrderItemByShoesOrderItem(shoesOrderItem1);
                    Integer empirical = new Integer(0);
                    BigDecimal totalMoney = new BigDecimal(0);

                    ShoesOrder shoesOrder = new ShoesOrder();

                    for (ShoesOrderItem orderItem : shoesOrderItemByShoesOrderItem) {

                        empirical += orderItem.getMoney().intValue();
                        totalMoney = orderItem.getMoney().add(totalMoney);
                    }

                    shoesOrder.setId(shoesOrderItem.getOrderId());
                    shoesOrder.setEmpirical(empirical);
                    shoesOrder.setMoney(totalMoney);
                    log.info("shoesOrder:" + shoesOrder.toString());
                    // 更新订单
                    shoesOrderService.updateShoesOrder(shoesOrder);
                    resultObject = ResultGenerator.genSuccessResult();
                }
            } else {
                resultObject = ResultGenerator.genFailResult("查询订单详情失败");
            }

        } else {
            resultObject = ResultGenerator.genFailResult("数据整理失败");
        }

        return resultObject;
    }


    /**
     * 删除订单详情中数据
     *
     * @return
     */
    @RequestMapping("/deleteOrderItemById")
    @ResponseBody
    public Result deleteOrderItemById(HttpServletRequest httpServletRequest, @RequestParam Integer orderItemId) {
        Result resultObject = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (resultObject == null || resultObject.getCode() == 200);
        if (isRightSystem) {
            if (orderItemId == null) {
                resultObject = ResultGenerator.genFailResult("orderItemId不能为空");
            } else {
                ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
                shoesOrderItem.setId(orderItemId);
                List<ShoesOrderItem> shoesOrderItemByShoesOrderItem = shoesOrderItemService.getShoesOrderItemByShoesOrderItem(shoesOrderItem);

                if (shoesOrderItemByShoesOrderItem == null || shoesOrderItemByShoesOrderItem.size() <= 0) {
                    resultObject = ResultGenerator.genFailResult("找不到数据");
                } else {

                    ShoesOrderItem shoesOrderItem1 = shoesOrderItemByShoesOrderItem.get(0);
                    log.info("订单详情1：" + shoesOrderItem1.toString());
                    ShoesOrder shoesOrderByOrderId = shoesOrderService.getShoesOrderByOrderId(shoesOrderItem1.getOrderId());
                    if (shoesOrderByOrderId == null) {
                        resultObject = ResultGenerator.genFailResult("订单未能匹配成功");
                    } else {

                        int i = shoesOrderItemService.delShoesOrderItemByShoesOrderItem(shoesOrderItem);
                        if (i < 0) {
                            resultObject = ResultGenerator.genFailResult("删除成功");
                        } else {
                            Integer empirical = shoesOrderByOrderId.getEmpirical() - shoesOrderItem1.getMoney().intValue();
                            BigDecimal totalMoney = shoesOrderByOrderId.getMoney().subtract(shoesOrderItem1.getMoney());
                            shoesOrderByOrderId.setEmpirical(empirical);
                            shoesOrderByOrderId.setMoney(totalMoney);
                            log.info(shoesOrderByOrderId.toString() + "数据是否正确");
                            // 更新订单
                            shoesOrderService.updateShoesOrder(shoesOrderByOrderId);
                            resultObject = ResultGenerator.genSuccessResult();
                        }
                    }

                }
            }
        }


        return resultObject;
    }


    /**
     * 添加订单和订单详情列表
     *
     * @return
     */
    @RequestMapping("/addOrderAndOrderItem")
    @ResponseBody
    public Result addOrderAndOrderItem(@RequestParam Integer userId,
                                       @RequestParam String productCode,
                                       @RequestParam(required = false, defaultValue = "1") Integer shoesNumber,
                                       HttpServletRequest httpServletRequest) {
        Result resultObject = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                resultObject = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            resultObject = ResultGenerator.genFailResult("需要先登录");
        }

        if (resultObject == null || resultObject.getCode() == 200) {
            if (userId == null) {
                resultObject = ResultGenerator.genFailResult("userId为必要参数");
            } else if (productCode == null) {
                resultObject = ResultGenerator.genFailResult("productCode为必要参数");
            } else {
                ShoesOrder shoesOrder = new ShoesOrder();
                shoesOrder.setUserId(userId);
                shoesOrder.setState(0);
                shoesOrder.setSystemId(sysUserId);
                ShoesOrder shoesOrderByShoesOrder = shoesOrderService.getShoesOrderByShoesOrder(shoesOrder);
                if (shoesOrderByShoesOrder == null) {
                    int i = shoesOrderService.addShoesOrder(shoesOrder);
                    if (i <= 0) {
                        resultObject = ResultGenerator.genFailResult("创建订单失败");
                    }
                }

                if (resultObject == null || resultObject.getCode() == 200) {
                    shoesOrderByShoesOrder = shoesOrderService.getShoesOrderByShoesOrder(shoesOrder);
                    ShoesProduct productInfoByProductCode = shoesProductService.getProductInfoByProductCode(productCode);
                    if (productInfoByProductCode == null) {
                        resultObject = ResultGenerator.genFailResult("未发现该商品信息");
                    } else {
                        ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
                        shoesOrderItem.setProductId(productInfoByProductCode.getId());
                        shoesOrderItem.setOrderId(shoesOrderByShoesOrder.getId());

                        log.info("数据显示1" + shoesOrderItem.toString());
                        List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
                        if (orderItemByShoesOrderItem == null || orderItemByShoesOrderItem.size() <= 0) {
                            shoesOrderItem.setPrice(productInfoByProductCode.getPrice());
                            shoesOrderItem.setNumber(shoesNumber);
                            shoesOrderItem.setMoney(productInfoByProductCode.getPrice().multiply(new BigDecimal(String.valueOf(shoesNumber))));
                            int i1 = shoesOrderItemService.addOrderItem(shoesOrderItem);
                            if (i1 <= 0) {
                                resultObject = ResultGenerator.genFailResult("为订单添加鞋子失败");
                            } else {
                                resultObject = ResultGenerator.genSuccessResult();
                                Integer empirical = shoesOrderByShoesOrder.getEmpirical() + shoesOrderItem.getMoney().intValue();

                                BigDecimal totalMoney = shoesOrderByShoesOrder.getMoney().add(shoesOrderItem.getMoney());
                                shoesOrderByShoesOrder.setEmpirical(empirical);
                                shoesOrderByShoesOrder.setMoney(totalMoney);
                                log.info("订单详情：" + shoesOrderByShoesOrder.toString());
                                // 更新订单
                                shoesOrderService.updateShoesOrder(shoesOrderByShoesOrder);
                            }
                        } else {
                            resultObject = ResultGenerator.genFailResult("订单中已经存在该鞋子");
                        }


                    }
                }
            }

        }


        return resultObject;
    }


    /**
     * 通过orderId获取用户信息
     *
     * @return
     */
    @RequestMapping("/getOrderItemByOrderId")
    @ResponseBody
    public Result<List<ShoesOrderItem>> getOrderItemByOrderId(HttpServletRequest httpServletRequest, @RequestParam Integer orderId) {

        Result<List<ShoesOrderItem>> shoesOrderResult = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesOrderResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesOrderResult = ResultGenerator.genFailResult("需要先登录");
        }

        boolean isRightSystem = (shoesOrderResult == null || shoesOrderResult.getCode() == 200);

        if (isRightSystem && orderId != null) {
            ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
            shoesOrderItem.setOrderId(orderId);

            List<ShoesOrderItem> shoesOrderItemList = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
            if (shoesOrderItem == null) {
                shoesOrderResult = ResultGenerator.genFailResult("没有发现订单信息");
            } else {
                shoesOrderResult = ResultGenerator.genSuccessResult(shoesOrderItemList);
            }
            log.info(shoesOrderItemList.toString());
        } else {
            shoesOrderResult = ResultGenerator.genFailResult("参数错误");
        }

        return shoesOrderResult;
    }


    /**
     * 通过productCode获取用户信息
     *
     * @return
     */
    @RequestMapping("/getShoesOrderByIncomplete")
    @ResponseBody
    public Result<ShoesOrder> getShoesOrderByIncomplete(HttpServletRequest httpServletRequest, @RequestParam Integer userId) {


        Result<ShoesOrder> shoesOrderResult = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesOrderResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesOrderResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesOrderResult == null || shoesOrderResult.getCode() == 200);
        if (isRightSystem && userId != null) {
            ShoesOrder shoesOrder = new ShoesOrder();
            shoesOrder.setUserId(userId);
            shoesOrder.setState(1);
            shoesOrder = shoesOrderService.getShoesOrderByShoesOrder(shoesOrder);
            if (shoesOrder == null) {
                shoesOrderResult = ResultGenerator.genFailResult("没有发现订单信息");
            } else {
                shoesOrderResult = ResultGenerator.genSuccessResult(shoesOrder);
            }
            log.info(shoesOrder.toString());
        } else {
            shoesOrderResult = ResultGenerator.genFailResult("没有发现用户有未完成订单");
        }

        return shoesOrderResult;
    }


    /**
     * 通过productCode获取用户信息
     *
     * @return
     */
    @RequestMapping("/getProductInfoByProductCode")
    @ResponseBody
    public Result<ShoesProduct> getProductInfoByProductCode(HttpServletRequest httpServletRequest, @RequestParam String productCode) {
        Result<ShoesProduct> shoesProductResult = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesProductResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesProductResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesProductResult == null || shoesProductResult.getCode() == 200);

        ShoesProduct shoesProduct = shoesProductService.getProductInfoByProductCode(productCode);
        if (isRightSystem && shoesProduct == null) {
            shoesProductResult = ResultGenerator.genFailResult("没有发现商品信息");
        } else {
            shoesProductResult = ResultGenerator.genSuccessResult(shoesProduct);
        }
        log.info(shoesProductResult.toString());

        return shoesProductResult;
    }


    /**
     * 跳转到添加订单的页面
     *
     * @return
     */
    @RequestMapping("/addOrderPage")
    public String addOrderPage(HttpServletRequest httpServletRequest, @RequestParam String userId, ModelMap map) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(userId);
        ShoesOrder shoesOrder = new ShoesOrder();
        shoesOrder.setUserId(Integer.parseInt(userId));
        shoesOrder.setState(0);
        shoesOrder = shoesOrderService.getShoesOrderByShoesOrder(shoesOrder);
        if (shoesOrder != null) {
            log.info(shoesOrder.toString() + "数据如上");
        }
        map.addAttribute("shoesUser", shoesUser);
        map.addAttribute("shoesOrder", shoesOrder);
        return "shoes/addOrder";
    }

    /**
     * 添加商品信息
     *
     * @return
     */
    @RequestMapping("/addProductInfo")
    public String addProductInfo(HttpServletRequest httpServletRequest,
                                 @RequestParam(required = false) String attribute,
                                 @RequestParam(required = false, defaultValue = "-1") int size,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String code,
                                 @RequestParam(required = false) String color,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false, defaultValue = "-1") Double price,
                                 ModelMap map) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        if (price < 0 || size < 0 || StringUtils.isBlank(attribute) || StringUtils.isBlank(name) || StringUtils.isBlank(code) || StringUtils.isBlank(color)) {
            return "shoes/addProductInfo";
        }
        ShoesProduct shoesProduct = new ShoesProduct();
        shoesProduct.setAttribute(attribute);
        shoesProduct.setSize(size);
        shoesProduct.setName(name);
        shoesProduct.setColor(color);
        shoesProduct.setCode(code);
        shoesProduct.setPrice(new BigDecimal(price));
        shoesProduct.setDescription(description);

        int i = shoesProductService.addProduct(shoesProduct);
        if (i > 0) {
            map.addAttribute("addProduct", "添加商品成功");
        } else {
            map.addAttribute("addProduct", "添加商品失败！请联系技术");
        }

        return "shoes/addProductInfo";
    }

    /**
     * 删除商品信息
     *
     * @return
     */
    @RequestMapping("/deleteProduct")
    @ResponseBody
    public Result<ShoesProduct> deleteProduct(HttpServletRequest httpServletRequest, @RequestParam int productId) {
        Result<ShoesProduct> shoesUserResult = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesUserResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesUserResult == null || shoesUserResult.getCode() == 200);
        if (isRightSystem) {
            int i = shoesProductService.delProduct(productId);
            if (i <= 0) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现商品信息");
            } else {
                shoesUserResult = ResultGenerator.genSuccessResult();
            }
        }

        return shoesUserResult;
    }

    /**
     * 更新商品信息
     *
     * @return
     */
    @RequestMapping("/updateProduct")
    @ResponseBody
    public Result<ShoesProduct> updateProduct(HttpServletRequest httpServletRequest,

                                              @RequestParam(required = false) String attribute,
                                              @RequestParam(required = false, defaultValue = "-1") int size,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false, defaultValue = "-1") Double price,
                                              @RequestParam(required = false) String code,
                                              @RequestParam(required = false) String color,
                                              @RequestParam(required = false) int productId,
                                              @RequestParam(required = false) String description) {
        Result<ShoesProduct> shoesProductResult = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesProductResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesProductResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesProductResult == null || shoesProductResult.getCode() == 200);

        if (isRightSystem) {
            if (productId == 0) {
                shoesProductResult = ResultGenerator.genFailResult("productId必要");
            } else {
                ShoesProduct shoesProduct = shoesProductService.getProductInfoByProductId(productId);
                if (shoesProduct == null) {
                    shoesProductResult = ResultGenerator.genFailResult("没有发现商品信息");
                } else {

                    log.info("数据如下" + price);
                    if (price >= 0) {
                        shoesProduct.setPrice(new BigDecimal(price));
                    }
                    if (StringUtils.isNotBlank(attribute)) {
                        shoesProduct.setAttribute(attribute);
                    }
                    if (size >= 0) {
                        shoesProduct.setSize(size);
                    }
                    if (StringUtils.isNotBlank(attribute)) {
                        shoesProduct.setAttribute(attribute);
                    }
                    if (StringUtils.isNotBlank(name)) {
                        shoesProduct.setName(name);
                    }
                    if (StringUtils.isNotBlank(code)) {
                        shoesProduct.setCode(code);
                    }
                    if (StringUtils.isNotBlank(color)) {
                        shoesProduct.setColor(color);
                    }
                    if (StringUtils.isNotBlank(description)) {
                        shoesProduct.setDescription(description);
                    }

                    log.info("实体类" + shoesProduct.toString());
                    int i = shoesProductService.updateProduct(shoesProduct);
                    if (i > 0) {
                        shoesProductResult = ResultGenerator.genSuccessResult(shoesProduct);
                    } else {
                        shoesProductResult = ResultGenerator.genFailResult("更新失败");
                    }
                }
            }
        }

        return shoesProductResult;
    }


    /**
     * 通过productId获取用户信息
     *
     * @return
     */
    @RequestMapping("/getProductInfoByProductId")
    @ResponseBody
    public Result<ShoesProduct> getProductInfoByProductId(@RequestParam int productId) {
        Result<ShoesProduct> shoesProductResult;
        ShoesProduct shoesProduct = shoesProductService.getProductInfoByProductId(productId);
        if (shoesProduct == null) {
            shoesProductResult = ResultGenerator.genFailResult("没有发现商品信息");
        } else {
            shoesProductResult = ResultGenerator.genSuccessResult(shoesProduct);
        }
        return shoesProductResult;
    }


    /**
     * 查询商品信息界面
     *
     * @return
     */
    @RequestMapping("/productInfoByPage")
    public String productInfoByPage(HttpServletRequest httpServletRequest,
                                    @RequestParam(value = "productInfo-attribute", required = false) String attribute,
                                    @RequestParam(value = "productInfo-size", required = false, defaultValue = "-1") int shoesSize,
                                    @RequestParam(value = "productInfo-name", required = false) String name,
                                    @RequestParam(value = "productInfo-code", required = false) String code,
                                    @RequestParam(value = "productInfo-color", required = false) String color,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }
        ShoesProduct shoesProduct = new ShoesProduct();
        shoesProduct.setAttribute(attribute);
        if (shoesSize >= 0) {
            shoesProduct.setSize(shoesSize);
        }
        shoesProduct.setName(name);
        shoesProduct.setCode(code);
        shoesProduct.setColor(color);

        PageHelper.startPage(page, size);
        List<ShoesProduct> productByShoesProduct =
                shoesProductService.getProductByShoesProduct(shoesProduct);
        PageInfo pageInfo = new PageInfo(productByShoesProduct);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("shoesProduct", shoesProduct);
        return "shoes/productInfo";
    }


    @RequestMapping("/addShoesProduct")
    public String addShoesProduct() {
        JSONObject jsonObject = null;
        return jsonObject.toJSONString();
    }



   /* @RequestMapping("/addOrderAndOrderItem")
    @ResponseBody
    public String addOrderAndOrderItem() {
        JSONObject jsonObject = shoesUserService.showBuyTrendByUserId();
        return jsonObject.toJSONString();
    }*/


    /**
     * 展示订单信息
     *
     * @return
     */
    @RequestMapping("/showBuyTrendByUserId")
    @ResponseBody
    public String showBuyTrendByUserId(@RequestParam int userId) {
        JSONObject jsonObject = shoesUserService.showBuyTrendByUserId(userId);
        return jsonObject.toJSONString();
    }

    /**
     * 删除用户信息
     *
     * @return
     */
    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result<ShoesUser> deleteUser(HttpServletRequest httpServletRequest,
                                        @RequestParam int userId) {

        Result<ShoesUser> shoesUserResult = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesUserResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesUserResult == null || shoesUserResult.getCode() == 200);

        if (isRightSystem) {
            int i = shoesUserMapper.deleteByPrimaryKey(userId);
            if (i <= 0) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现用户信息");
            } else {
                shoesUserResult = ResultGenerator.genSuccessResult();
            }
        }

        return shoesUserResult;
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public Result<ShoesUser> updateUserInfo(HttpServletRequest httpServletRequest,
                                            @RequestParam int userId,
                                            @RequestParam(required = false) String phoneNum,
                                            @RequestParam(required = false) String realName,
                                            @RequestParam(required = false) String birthday) {
        Result<ShoesUser> shoesUserResult = null;

        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesUserResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesUserResult == null || shoesUserResult.getCode() == 200);

        if (isRightSystem) {
            if (userId == 0) {
                shoesUserResult = ResultGenerator.genFailResult("userId必要");
            } else {
                ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(userId);
                if (shoesUser == null) {
                    shoesUserResult = ResultGenerator.genFailResult("没有发现用户信息");
                } else {

                    SimpleDateFormat formatter = new SimpleDateFormat(shortDateFormat);
                    if (StringUtils.isNotBlank(phoneNum)) {
                        shoesUser.setPhoneNum(phoneNum);
                    }
                    if (StringUtils.isNotBlank(realName)) {
                        shoesUser.setRealName(realName);
                    }

                    if (StringUtils.isNotBlank(birthday)) {
                        try {
                            shoesUser.setBirthday(formatter.parse(birthday));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    int i = shoesUserMapper.updateByPrimaryKey(shoesUser);
                    if (i > 0) {
                        shoesUserResult = ResultGenerator.genSuccessResult(shoesUser);
                    } else {
                        shoesUserResult = ResultGenerator.genFailResult("更新失败");
                    }
                }
            }
        }

        return shoesUserResult;
    }

    /**
     * 通过userId获取用户信息
     *
     * @return
     */
    @RequestMapping("/getUserInfoByUserId")
    @ResponseBody
    public Result<ShoesUser> getUserInfoByUserId(HttpServletRequest httpServletRequest,
                                                 @RequestParam int userId) {
        Result<ShoesUser> shoesUserResult = null;
        int sysUserId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            sysUserId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(sysUserId);
            if (shoesSystem == null) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现管理员信息");
            }
        } else {
            shoesUserResult = ResultGenerator.genFailResult("需要先登录");
        }
        boolean isRightSystem = (shoesUserResult == null || shoesUserResult.getCode() == 200);

        if (isRightSystem) {
            ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(userId);
            if (shoesUser == null) {
                shoesUserResult = ResultGenerator.genFailResult("没有发现用户信息");
            } else {
                shoesUserResult = ResultGenerator.genSuccessResult(shoesUser);
            }
        }

        return shoesUserResult;

    }


    /**
     * 添加用户信息
     *
     * @return
     */
    @RequestMapping("/addUserInfo")
    public String addUserInfo(HttpServletRequest httpServletRequest,
                              @RequestParam(required = false) String phoneNum,
                              @RequestParam(required = false) String realName,
                              @RequestParam(required = false) String birthday, ModelMap map) {

        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        if (StringUtils.isBlank(phoneNum) || StringUtils.isBlank(realName) || StringUtils.isBlank(birthday)) {
            return "shoes/addUserInfo";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(shortDateFormat);
        ShoesUser shoesUser = new ShoesUser();
        try {
            shoesUser.setBirthday(formatter.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        shoesUser.setPhoneNum(phoneNum);
        shoesUser.setRealName(realName);
        int i = shoesUserMapper.insertSelective(shoesUser);
        if (i > 0) {
            map.addAttribute("addUserInfo", "添加用户成功");
        } else {
            map.addAttribute("addUserInfo", "添加用户失败！请联系技术");
        }

        return "shoes/addUserInfo";
    }


    /**
     * 查询商品码是否存在
     *
     * @return
     */
    @RequestMapping("/validateShoesCode")
    @ResponseBody
    public String validateShoesCode(@RequestParam String code) {

        ShoesProduct productInfoByProductCode = shoesProductService.getProductInfoByProductCode(code);
        boolean isValidate = false;
        if (productInfoByProductCode == null) {
            isValidate = true;
        }
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("valid", isValidate);
        ObjectMapper mapper = new ObjectMapper();
        String resultString = "";
        try {
            resultString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultString;
    }


    /**
     * 查询用户手机号是否存在
     *
     * @return
     */
    @RequestMapping("/validatePhoneNum1")
    @ResponseBody
    public Result<String> validatePhoneNum1(@RequestParam String phoneNum) {
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phoneNum", phoneNum);
        List<ShoesUser> shoesUsers = shoesUserMapper.selectByExample(example);
        Result<String> shoesUserResult = null;
        if (shoesUsers == null || shoesUsers.size() <= 0) {
            shoesUserResult = ResultGenerator.genSuccessResult();
        } else {
            shoesUserResult = ResultGenerator.genFailResult("手机号已经存在");
        }

        return shoesUserResult;
    }

    /**
     * 查询用户手机号是否存在
     *
     * @return
     */
    @RequestMapping("/validatePhoneNum")
    @ResponseBody
    public String addUserInfo(@RequestParam String phoneNum) {
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phoneNum", phoneNum);
        List<ShoesUser> shoesUsers = shoesUserMapper.selectByExample(example);
        boolean isValidate = false;
        if (shoesUsers == null || shoesUsers.size() <= 0) {
            isValidate = true;
        }
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("valid", isValidate);
        ObjectMapper mapper = new ObjectMapper();
        String resultString = "";
        try {
            resultString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultString;
    }


    /**
     * 查询用户信息界面
     *
     * @return
     */
    @RequestMapping("/userInfoByPage")
    public String userInfoByPage(HttpServletRequest httpServletRequest,
                                 @RequestParam(required = false) String phoneNum,
                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
        if (ShoesCookie.isLogin(httpServletRequest)) {
            int userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }

        PageHelper.startPage(page, size);
        List<ShoesUser> userInfoByPage;
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        example.orderBy("createTime").desc();
        if (phoneNum != null && !phoneNum.equals("")) {
            map.addAttribute("phoneNum", phoneNum);
            criteria.andEqualTo("phoneNum", phoneNum);
            //按照时间倒叙
            userInfoByPage = shoesUserMapper.selectByExample(example);
        } else {
            //按照时间倒叙
            userInfoByPage = shoesUserMapper.selectByExample(example);
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
            return "shoes/index";
        } else {
            return "forward:/home/loginPage";
        }
    }

    @RequestMapping("/indexCount")
    public String indexCount(HttpServletRequest httpServletRequest, ModelMap map) {
        int userId = 0;
        if (ShoesCookie.isLogin(httpServletRequest)) {
            userId = ShoesCookie.getUserId(httpServletRequest);
            ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
            if (shoesSystem == null) {
                map.addAttribute("errorCode", "NOT FOUND");
                map.addAttribute("errorDesc", "请重新登录系统");
                return "communal/error/error";
            }
        } else {
            map.addAttribute("errorCode", "NO COOKIE");
            map.addAttribute("errorDesc", "请重新登录系统");
            return "communal/error/error";
        }
        ShoesSystem shoesSystem = shoesService.shoesSystemUserById(userId);
        map.addAttribute("shoesSystem", shoesSystem);
        SimpleDateFormat formatter = new SimpleDateFormat(shortDateFormat);
        String date = formatter.format(new Date());
        Map<String, String> todayShoesOrder = shoesOrderService.getTodayShoesOrder(date + " 0:0:0", date + " 23:59:59");
        map.addAttribute("todayData", todayShoesOrder);
        return "shoes/indexCount";
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
