package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 14:02
 * @Description: TODO
 */
@Repository
public interface ShoesOrderItemMapper extends MyMapper<ShoesOrderItem> {

    /**
     * 查询最近countNum个热销产品
     * @param countNum
     * @return
     */
    List<ShoesOrderItem> hotSaleCount(@Param("countNum")int countNum, @Param("startDate")String startDate, @Param("endDate")String endDate);
}