package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesSystem;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 13:13
 * @Description: TODO
 */
public interface ShoesService {

    /**
     * 通过realName和密码进行查询
     *
     * @param realName
     * @param passWord
     * @return
     */
    ShoesSystem shoesSystemUserLogin(String realName, String passWord);

    /**
     * 通过userId获取管理员信息
     *
     * @param id
     * @return
     */
    ShoesSystem shoesSystemUserById(int id);
}

