package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesReduction;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/2/22 10:07
 * @Description: TODO
 */
public interface ShoesReductionService {

    /**
     * 记录抵免情况
     *
     * @param shoesReduction
     * @return
     */
    boolean addShoesReduction(ShoesReduction shoesReduction);

    // 查询

    /**
     * 通过shoesReduction获取抵免信息
     *
     * @param shoesReduction
     * @return
     */
    List<ShoesReduction> getShoesReduction(ShoesReduction shoesReduction);

    // 删除

    /**
     * 通过Id删除数据
     *
     * @param id
     * @return
     */
    boolean deleteShoesReduction(int id);
}
