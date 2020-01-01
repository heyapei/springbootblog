package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesMapper;
import com.hyp.pojo.shoes.dataobject.ShoesSystem;
import com.hyp.service.shoes.ShoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 14:24
 * @Description: TODO
 */
@Service
public class ShoesServiceImpl implements ShoesService {

    @Autowired
    private ShoesMapper shoesMapper;


    @Override
    public ShoesSystem shoesSystemUserLogin(String realName, String passWord) {
        ShoesSystem shoesSystem = shoesMapper.shoesSystemUserLogin(realName, passWord);
        return shoesSystem;
    }

    @Override
    public ShoesSystem shoesSystemUserById(int id) {
        return shoesMapper.selectByPrimaryKey(id);
    }
}
