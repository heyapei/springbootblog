package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.pojo.shoes.dataobject.ShoesSystem;
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
public interface ShoesOrderItemMapper {

    /**
     * 通过realName和密码进行查询
     *
     * @param realName
     * @param passWord
     * @return
     */
    ShoesOrderItem shoesSystemUserLogin(@Param("realName")String realName, @Param("passWord")String passWord);


}