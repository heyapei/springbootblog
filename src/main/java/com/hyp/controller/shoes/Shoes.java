package com.hyp.controller.shoes;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.mapper.ShoesUserMapper;
import com.hyp.pojo.shoes.dataobject.*;
import com.hyp.pojo.shoes.dto.ShoesCookieDTO;
import com.hyp.pojo.shoes.utils.PrintTest;
import com.hyp.pojo.shoes.vo.OrderItemVO;
import com.hyp.pojo.shoes.vo.RealOrderVO;
import com.hyp.service.shoes.ShoesOrderItemService;
import com.hyp.service.shoes.ShoesOrderService;
import com.hyp.service.shoes.ShoesProductService;
import com.hyp.service.shoes.ShoesUserService;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import com.hyp.utils.shoes.ShoesCookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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


    /**
     * 打印小票
     *
     * @return
     */
    @RequestMapping("/printTicket")
    @ResponseBody
    public Result printTicket(@RequestParam Integer orderId) {

        Result resultObject;
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
                //打印小票
                PrintTest.print1();
            } else {
                resultObject = ResultGenerator.genFailResult("确认订单错误");
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
    public String realOrderPage(@RequestParam Integer orderId,
                                ModelMap modelMap) {
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
    public String orderItemInfoByPage(@RequestParam(value = "orderItemInfo-phoneNum", required = false) String phoneNum,
                                      @RequestParam(value = "orderInfo-startDate", required = false) String startDate,
                                      @RequestParam(value = "orderInfo-endDate", required = false) String endDate,
                                      @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {

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
    public String orderInfoByPage(@RequestParam(value = "orderInfo-phoneNum", required = false) String phoneNum,
                                  @RequestParam(value = "orderInfo-startDate", required = false) String startDate,
                                  @RequestParam(value = "orderInfo-endDate", required = false) String endDate,
                                  @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
        ShoesOrder shoesOrder = new ShoesOrder();

        int userId = 0;
        Example example = new Example(ShoesUser.class);
        Example.Criteria criteria = example.createCriteria();
        List<ShoesUser> shoesUserList = null;
        if (phoneNum != null) {
            criteria.andEqualTo("phoneNum", phoneNum);
        }
        shoesUserList = shoesUserMapper.selectByExample(example);
        ShoesUser shoesUser = null;
        if (shoesUserList == null || shoesUserList.size() <= 0) {
            shoesUser = shoesUserList.get(0);
            userId = shoesUser.getId();
        }

        shoesOrder.setUserId(userId);

        PageHelper.startPage(page, size);

        List<ShoesOrder> shoesOrderByPhoneAndTime = shoesOrderService.getShoesOrderByPhoneAndTime(shoesOrder);
        PageInfo pageInfo = new PageInfo(shoesOrderByPhoneAndTime);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        map.addAttribute("pageResult", objectResult);
        map.addAttribute("indexPage", pageInfo.getPageNum());
        map.addAttribute("totalPage", pageInfo.getPages());
        map.addAttribute("phoneNum", phoneNum);
        map.addAttribute("shoesOrder", shoesOrder);
        return "orderItemInfo";
    }


    /**
     * 关键字搜索
     *
     * @return
     */
    @RequestMapping("/keySearchByShoesCode")
    @ResponseBody
    public Result updateOrderItemById(@RequestParam String shoesCode) {

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
    public Result realAddOrder(@RequestParam Integer orderId,
                               @RequestParam(required = false, defaultValue = "0") Integer orderReduction) {

        Result resultObject;

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


        return resultObject;
    }


    /**
     * 更新订单详情中数据
     *
     * @return
     */
    @RequestMapping("/updateOrderItemById")
    @ResponseBody
    public Result updateOrderItemById(@RequestParam Integer orderItemId,
                                      @RequestParam Integer orderItemNumber) {

        log.info(orderItemId + "===" + orderItemNumber);

        Result resultObject = null;

        if (orderItemId == null) {
            resultObject = ResultGenerator.genFailResult("orderItemId不能为空");
        } else {
            log.info("cuowu1");
        }

        if (resultObject == null || resultObject.getCode() == 200) {
            if (orderItemNumber == null) {
                resultObject = ResultGenerator.genFailResult("orderItemId不能为空");
            } else {
                log.info("cuowu2-2");
            }
        } else {
            log.info("cuowu2-1");
        }


        if (resultObject == null || resultObject.getCode() == 200) {
            log.info("能进入该方法");

            ShoesOrderItem shoesOrderItem = new ShoesOrderItem();
            shoesOrderItem.setId(orderItemId);
            shoesOrderItem.setNumber(orderItemNumber);
            List<ShoesOrderItem> orderItemByShoesOrderItem = shoesOrderItemService.getOrderItemByShoesOrderItem(shoesOrderItem);
            if (orderItemByShoesOrderItem != null && orderItemByShoesOrderItem.size() > 0) {
                shoesOrderItem = orderItemByShoesOrderItem.get(0);
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
    public Result deleteOrderItemById(@RequestParam Integer orderItemId) {
        Result resultObject;

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
    public Result<List<ShoesOrderItem>> getOrderItemByOrderId(@RequestParam Integer orderId) {

        Result<List<ShoesOrderItem>> shoesOrderResult;

        if (orderId != null) {
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
    public Result<ShoesOrder> getShoesOrderByIncomplete(@RequestParam Integer userId) {


        Result<ShoesOrder> shoesOrderResult;

        if (userId != null) {
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
    public Result<ShoesProduct> getProductInfoByProductCode(@RequestParam String productCode) {
        Result<ShoesProduct> shoesProductResult;
        ShoesProduct shoesProduct = shoesProductService.getProductInfoByProductCode(productCode);
        if (shoesProduct == null) {
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
    public String addOrderPage(@RequestParam String userId, ModelMap map) {
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
    public String addProductInfo(@RequestParam(required = false) String attribute,
                                 @RequestParam(required = false, defaultValue = "-1") int size,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String code,
                                 @RequestParam(required = false) String color,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false, defaultValue = "-1") Double price,
                                 ModelMap map) {

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
    public Result<ShoesProduct> deleteProduct(@RequestParam int productId) {
        int i = shoesProductService.delProduct(productId);
        Result<ShoesProduct> shoesUserResult;
        if (i <= 0) {
            shoesUserResult = ResultGenerator.genFailResult("没有发现商品信息");
        } else {
            shoesUserResult = ResultGenerator.genSuccessResult();
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
    public Result<ShoesProduct> updateProduct(@RequestParam(required = false) String attribute,
                                              @RequestParam(required = false, defaultValue = "-1") int size,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false, defaultValue = "-1") Double price,
                                              @RequestParam(required = false) String code,
                                              @RequestParam(required = false) String color,
                                              @RequestParam(required = false) int productId,
                                              @RequestParam(required = false) String description) {
        Result<ShoesProduct> shoesProductResult;

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
    public String productInfoByPage(@RequestParam(value = "productInfo-attribute", required = false) String attribute,
                                    @RequestParam(value = "productInfo-size", required = false, defaultValue = "-1") int shoesSize,
                                    @RequestParam(value = "productInfo-name", required = false) String name,
                                    @RequestParam(value = "productInfo-code", required = false) String code,
                                    @RequestParam(value = "productInfo-color", required = false) String color,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
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
    public Result<ShoesUser> deleteUser(@RequestParam int userId) {
        int i = shoesUserMapper.deleteByPrimaryKey(userId);
        Result<ShoesUser> shoesUserResult;
        if (i <= 0) {
            shoesUserResult = ResultGenerator.genFailResult("没有发现用户信息");
        } else {
            shoesUserResult = ResultGenerator.genSuccessResult();
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
    public Result<ShoesUser> updateUserInfo(@RequestParam int userId,
                                            @RequestParam(required = false) String phoneNum,
                                            @RequestParam(required = false) String realName,
                                            @RequestParam(required = false) String birthday) {
        Result<ShoesUser> shoesUserResult;

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
        return shoesUserResult;
    }

    /**
     * 通过userId获取用户信息
     *
     * @return
     */
    @RequestMapping("/getUserInfoByUserId")
    @ResponseBody
    public Result<ShoesUser> getUserInfoByUserId(@RequestParam int userId) {
        Result<ShoesUser> shoesUserResult;
        ShoesUser shoesUser = shoesUserMapper.selectByPrimaryKey(userId);
        if (shoesUser == null) {
            shoesUserResult = ResultGenerator.genFailResult("没有发现用户信息");
        } else {
            shoesUserResult = ResultGenerator.genSuccessResult(shoesUser);
        }
        return shoesUserResult;

    }


    /**
     * 添加用户信息
     *
     * @return
     */
    @RequestMapping("/addUserInfo")
    public String addUserInfo(@RequestParam(required = false) String phoneNum,
                              @RequestParam(required = false) String realName,
                              @RequestParam(required = false) String birthday, ModelMap map) {

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
    public String userInfoByPage(@RequestParam(required = false) String phoneNum,
                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer size, ModelMap map) {
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
        int userId = ShoesCookie.getUserId(httpServletRequest);
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
