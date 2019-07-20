package com.hyp.mapper;

import java.util.List;

import com.hyp.pojo.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapperCustom {
	
	List<SysUser> queryUserSimplyInfoById(String id);
}