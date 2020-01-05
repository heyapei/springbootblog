package com.hyp.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/1 10:51
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesUserMapperTest {

    @Autowired
    private ShoesUserMapper shoesUserMapper;

    @Test
    public void addShoesUser() {
        /*ShoesUser shoesUser = new ShoesUser();
        shoesUser.setBirthday(new Date());
        shoesUser.setPassWord("11");
        shoesUser.setRealName("hayepei");
        shoesUser.setPhoneNum("15518901416");
        shoesUser.setCreateTime(new Date());
        int insert = shoesUserMapper.insertUseGeneratedKeys(shoesUser);
        log.info("返回值{}", insert);
        log.info("返回的主键{}", shoesUser.getId());*/
       /* ShoesUser shoesUser = new ShoesUser();
        shoesUser.setBirthday(new Date());
        shoesUser.setPassWord("11");
        shoesUser.setRealName("hayepei");
        shoesUser.setPhoneNum("15518901416");
        int i = shoesUserMapper.addShoesUser(shoesUser);
        log.info("返回值{}", i);
        log.info("返回的主键{}", shoesUser.getId());*/
    }

    @Test
    public void delShoesUser() {
        ShoesUser shoesUser = new ShoesUser();
        shoesUser.setBirthday(new Date());
        shoesUser.setPassWord("110");
        shoesUser.setRealName("hayepe1");
        shoesUser.setPhoneNum("15518901410");
        shoesUser.setId(2);
        int i = shoesUserMapper.updateByPrimaryKeySelective(shoesUser);
        log.info("按照主键更新" + i);
        log.info("更新后的结果" + shoesUser);

    }

    @Test
    public void selectShoesUser() {
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phoneNum", "15518901416");
        List<ShoesUser> lists = shoesUserMapper.selectByExample(example);
        for (ShoesUser list : lists) {
            System.out.println(list.toString());
        }


    }


    @Test
    public void testUserInfo() {
        String phoneNum = "";
        PageHelper.startPage(1, 1);
        List<ShoesUser> userInfoByPage;
        Example example = new Example(ShoesUser.class);
        Criteria criteria = example.createCriteria();
        if (phoneNum != null && !phoneNum.equals("")) {
            criteria.andEqualTo("phoneNum", phoneNum);
            userInfoByPage = shoesUserMapper.selectByExample(example);
        } else {
            example.orderBy("createTime").desc();
            userInfoByPage = shoesUserMapper.selectAll();
        }
        PageInfo pageInfo = new PageInfo(userInfoByPage);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        System.out.println(objectResult.toString());
    }
}