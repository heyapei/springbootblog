package com.hyp.pojo.shoes.vo;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import lombok.Data;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/14 14:45
 * @Description: TODO
 */
@Data
public class ShoesTicketVO {
    private ShoesOrder shoesOrder;
    private ShoesUser shoesUser;
    private List<ShoesItemAndProduct> shoesItemAndProductList;
}
