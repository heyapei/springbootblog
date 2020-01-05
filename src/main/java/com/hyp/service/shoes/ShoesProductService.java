package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesProduct;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/2 17:02
 * @Description: TODO
 */
public interface ShoesProductService {

    /**
     * 通过关键字shoesCode搜索
     *
     * @param shoesCode
     * @return
     */
    List<ShoesProduct> getShoesProductByKeyWordShoesCode(String shoesCode);

    /**
     * 添加shoesProduct
     *
     * @param shoesProduct
     * @return 主键
     */
    int addProduct(ShoesProduct shoesProduct);

    /**
     * 根据主键更新shoesProduct
     *
     * @param shoesProduct
     * @return
     */
    int updateProduct(ShoesProduct shoesProduct);

    /**
     * 根据主键删除shoesProduct
     *
     * @param id
     * @return
     */
    int delProduct(int id);

    /**
     * 通过id查询商品信息
     *
     * @param id
     * @return 实体类
     */
    ShoesProduct getProductInfoByProductId(int id);

    /**
     * 通过productCode查询商品信息
     *
     * @param productCode
     * @return 实体类
     */
    ShoesProduct getProductInfoByProductCode(String productCode);

    /**
     * 根据shoesProduct查询shoesProduct
     *
     * @param shoesProduct
     * @return 主键
     */
    List<ShoesProduct> getProductByShoesProduct(ShoesProduct shoesProduct);
}
