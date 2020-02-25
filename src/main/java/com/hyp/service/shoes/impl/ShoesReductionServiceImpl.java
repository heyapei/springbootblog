package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesReductionMapper;
import com.hyp.mapper.ShoesUserMapper;
import com.hyp.pojo.shoes.dataobject.ShoesReduction;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.service.shoes.ShoesReductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/2/22 10:08
 * @Description: TODO
 */
@Slf4j
@Service
public class ShoesReductionServiceImpl implements ShoesReductionService {

    @Autowired
    private ShoesReductionMapper shoesReductionMapper;

    @Autowired
    private ShoesUserMapper shoesUserMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addShoesReduction(ShoesReduction shoesReduction) {
        int i = shoesReductionMapper.insertUseGeneratedKeys(shoesReduction);
        if (i > 0) {
            ShoesUser shoesUser = new ShoesUser();
            shoesUser.setPhoneNum(shoesReduction.getPhoneNum());
            shoesUser = shoesUserMapper.selectOne(shoesUser);
            shoesUser.setEmpirical(shoesUser.getEmpirical() - shoesReduction.getReduction());
            shoesUserMapper.updateByPrimaryKey(shoesUser);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ShoesReduction> getShoesReduction(ShoesReduction shoesReduction) {

        List<ShoesReduction> shoesReductionList = new ArrayList<>();
        Example example = new Example(ShoesReduction.class);
        Example.Criteria criteria = example.createCriteria();
        if (shoesReduction != null) {
            // 通过订单号
            if (shoesReduction.getPhoneNum() != null) {
                criteria.andEqualTo("phoneNum", shoesReduction.getPhoneNum());
            }


            //按照时间倒叙
            example.orderBy("createDate").desc();


            shoesReductionList = shoesReductionMapper.selectByExample(example);

            log.info(shoesReductionList.toString());

            if (shoesReductionList != null && shoesReductionList.size() > 0) {
                return shoesReductionList;
            }
        }

        return shoesReductionList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteShoesReduction(int id) {

        ShoesReduction shoesReduction = shoesReductionMapper.selectByPrimaryKey(id);

        if (shoesReduction != null) {
            ShoesUser shoesUser = new ShoesUser();
            shoesUser.setPhoneNum(shoesReduction.getPhoneNum());
            shoesUser = shoesUserMapper.selectOne(shoesUser);
            shoesUser.setEmpirical(shoesUser.getEmpirical() + shoesReduction.getReduction());
            int i = shoesUserMapper.updateByPrimaryKey(shoesUser);
            if (i > 0) {
                int i1 = shoesReductionMapper.deleteByPrimaryKey(id);
                if (i1 > 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
