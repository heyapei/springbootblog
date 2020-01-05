package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesProductMapper;
import com.hyp.pojo.shoes.dataobject.ShoesProduct;
import com.hyp.service.shoes.ShoesProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/2 17:07
 * @Description: TODO
 */

@Service
public class ShoesProductServiceImpl implements ShoesProductService {

    @Autowired
    private ShoesProductMapper shoesProductMapper;

    @Override
    public List<ShoesProduct> getShoesProductByKeyWordShoesCode(String shoesCode) {
        Example example = new Example(ShoesProduct.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("code", "%" + shoesCode + "%");
        List<ShoesProduct> shoesProductList = shoesProductMapper.selectByExample(example);
        return shoesProductList;
    }

    @Override
    public int addProduct(ShoesProduct shoesProduct) {
        int i = shoesProductMapper.insertSelective(shoesProduct);
        return i;
    }

    @Override
    public int updateProduct(ShoesProduct shoesProduct) {
        int i = shoesProductMapper.updateByPrimaryKey(shoesProduct);
        return i;
    }

    @Override
    public int delProduct(int id) {
        int i = shoesProductMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public ShoesProduct getProductInfoByProductId(int id) {
        return shoesProductMapper.selectByPrimaryKey(id);
    }

    @Override
    public ShoesProduct getProductInfoByProductCode(String productCode) {
        Example example = new Example(ShoesProduct.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", productCode);
        List<ShoesProduct> shoesProductList = shoesProductMapper.selectByExample(example);
        if (shoesProductList != null && shoesProductList.size() > 0) {
            return shoesProductList.get(0);
        }
        return null;
    }

    @Override
    public List<ShoesProduct> getProductByShoesProduct(ShoesProduct shoesProduct) {
        List<ShoesProduct> shoesProductList = null;
        Example example = new Example(ShoesProduct.class);
        Example.Criteria criteria = example.createCriteria();
        if (shoesProduct != null) {

            if (shoesProduct.getCreateDate() != null) {
                criteria.andGreaterThanOrEqualTo("createDate", shoesProduct.getCreateDate());
            }
            if (shoesProduct.getModifyDate() != null) {
                criteria.andLessThanOrEqualTo("createDate", shoesProduct.getModifyDate());
            }
            if (shoesProduct.getAttribute() != null) {
                criteria.andLike("attribute", "%" + shoesProduct.getAttribute() + "%");
            }
            if (shoesProduct.getName() != null) {
                criteria.andLike("name", "%" + shoesProduct.getName() + "%");
            }
            if (shoesProduct.getCode() != null) {
                criteria.andLike("code", "%" + shoesProduct.getCode() + "%");
            }
            if (shoesProduct.getSize() != null) {
                criteria.andEqualTo("size", shoesProduct.getSize());
            }
            if (shoesProduct.getColor() != null) {
                criteria.andLike("color", "%" + shoesProduct.getColor() + "%");
            }
            //按照时间倒叙
            example.orderBy("createDate").desc();
            shoesProductList = shoesProductMapper.selectByExample(example);
        }

        return shoesProductList;
    }
}
