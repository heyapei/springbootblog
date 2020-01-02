package com.hyp.service.shoes;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/2 13:15
 * @Description: TODO
 */
public interface ShoesUserService {
    /**
     * 获取动态图
     * @param userId
     * @return
     */
    JSONObject showBuyTrendByUserId(int userId);
}
