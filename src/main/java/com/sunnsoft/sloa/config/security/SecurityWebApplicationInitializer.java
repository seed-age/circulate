package com.sunnsoft.sloa.config.security;

/**
 * spring security java config 无法与原有的web.xml里的其他类型filter顺序自定义，导致很多问题。因此暂时不采用java config方式进行配置。
 * @author llade
 *
 */
public class SecurityWebApplicationInitializer
//	extends AbstractSecurityWebApplicationInitializer //只要不继承这个类，就不会采用java config方式配置
	{

}
