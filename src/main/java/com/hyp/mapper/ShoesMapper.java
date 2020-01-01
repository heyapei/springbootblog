package com.hyp.mapper;

import com.hyp.controller.shoes.Shoes;
import com.hyp.pojo.shoes.dataobject.ShoesSystem;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 14:02
 * @Description: TODO
 */
@Repository
public interface ShoesMapper extends MyMapper<ShoesSystem> {

    /**
     * 通过realName和密码进行查询
     *
     * @param realName
     * @param passWord
     * @return
     */
    ShoesSystem shoesSystemUserLogin(@Param("realName")String realName, @Param("passWord")String passWord);


}
