package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesUserMapper extends MyMapper<ShoesUser> {
    /**
     * 添加用户
     *
     * @param shoesUser
     * @return
     */
    /*int addShoesUser(ShoesUser shoesUser);*/

    /**
     * 更新ShoesUser
     *
     * @param shoesUser
     * @return
     *//*
    int updateShoesUser(ShoesUser shoesUser);

    *//**
     * 通过用户信息查询
     *
     * @param shoesUser
     * @return
     *//*
    ShoesUser getShoesUserByOrderId(ShoesUser shoesUser);*/


}