package com.hyp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 主动设置URL匹配路径
 *
 * @author 何亚培
 * @date 2018年8月4日 13:36:38
 */

@Configuration
public class MyURLPatternConfiguration extends WebMvcConfigurationSupport {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploadImg/**").addResourceLocations("classpath:/uploadImg/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

		super.addResourceHandlers(registry);
	}
}

