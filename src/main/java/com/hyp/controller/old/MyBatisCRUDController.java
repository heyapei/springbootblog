package com.hyp.controller.old;

import com.hyp.pojo.IMoocJSONResult;
import com.hyp.pojo.SysUser;
import com.hyp.service.UserService;
import org.hyp.utils.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("mybatis")
public class MyBatisCRUDController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private Sid sid;

	@RequestMapping("/getSid")
	public IMoocJSONResult getSid() throws Exception {
		/**
		 * 获取第三方的工具包 这里注意要扫描第三方的包@ComponentScan(basePackages= {"com.hyp", "org.hyp.utils"})
		* */
		return IMoocJSONResult.ok(sid.hid());
	}
	
	@RequestMapping("/saveUser")
	public IMoocJSONResult saveUser() throws Exception {

		SysUser user = new SysUser();
		for (int i = 1; i < 30; i++) {
			user.setId(i);
			user.setUsername("imooc" + new Date());
			user.setNickname("imooc" + new Date());
			user.setPassword("abc123");
			user.setIsdelete("0");
			user.setRegisttime(new Date());

			userService.saveUser(user);
		}

		
		return IMoocJSONResult.ok("保存成功");
	}

	@RequestMapping("/updateUser")
	public IMoocJSONResult updateUser() {

		SysUser user = new SysUser();
		user.setId(123);
		user.setUsername("10011001-updated" + new Date());
		user.setNickname("10011001-updated" + new Date());
		user.setPassword("10011001-updated");
		user.setPassword("abc123");
		user.setIsdelete("0");
		user.setRegisttime(new Date());
		userService.updateUser(user);
		return IMoocJSONResult.ok("保存成功");
	}


	@RequestMapping("/queryUserListPaged")
	public IMoocJSONResult queryUserListPaged(Integer page) {

		// page为空或者为0就查第一页 page大于最大页码 查最后一页
		if (page == null) {
			page = 1;
		}
		int pageSize = 10;
		SysUser user = new SysUser();

		List<SysUser> userList = userService.queryUserListPaged(user, page, pageSize);
		return IMoocJSONResult.ok(userList);
	}














	

}
