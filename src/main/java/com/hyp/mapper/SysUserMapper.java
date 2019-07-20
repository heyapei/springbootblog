package com.hyp.mapper;

import com.hyp.pojo.SysUser;
import com.hyp.utils.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * 不加@Repository也不会有错 只是会有idea报错而已
 */
@Repository
public interface SysUserMapper  extends MyMapper<SysUser>   {
}