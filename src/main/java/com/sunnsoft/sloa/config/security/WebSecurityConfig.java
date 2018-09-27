package com.sunnsoft.sloa.config.security;

/**
 * spring security java config 无法与原有的web.xml里的其他类型filter进行顺序自定义。因此暂时不采用java config方式进行配置。此类配置暂时搁置。
 * @author llade
 *
 */
//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig  {
//	
//	private @Value("${project.name}") String projectName ;
//	
//	@Bean(name="passwordEncoder")
//	public PasswordEncoder passwordEncoder(){
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean(name="systemAuthenticationProvider")
//	public AuthenticationProvider systemAuthenticationProvider(){
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		provider.setPasswordEncoder(passwordEncoder());
//		provider.setUserDetailsService(systemUserDetailsService());
//		return provider;
//	}
//	
//	@Bean(name="accessTokenAuthenticationProvider")
//	public AuthenticationProvider accessTokenAuthenticationProvider(){
//		return new AccessTokenAuthenticationProvider();
//	}
//	
//	@Bean(name="systemUserDetailsService")
//	public UserDetailsService systemUserDetailsService(){
//		return new SystemUserDetailsService(userService);
//	}
//	
//	@Bean(name="systemSessionRegistry")
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//	
//	@Configuration
//	@Order(1)                                                        
//	public class SystemWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//		
//		private static final String SYSTEM_REMEMBER_KEY_SUBFFIX = "-system-rmekey";
//		private static final String SYSTEM_LOGOUT_URL = "/system/logout";
//		private static final String SYSTEM_LOGIN_PROCESS_URL = "/system/login-process";
//		private static final String SYSTEM_LOGIN_URL = "/system/login.htm";
//
//		@Bean
//		public Filter validateCodeCheckFilter(){
//			return new ValidateCodeCheckFilter(SYSTEM_LOGIN_PROCESS_URL, SYSTEM_LOGIN_URL + "?error=2");
//		}
//		
//		@Bean
//		public PersistentTokenRepository tokenRepository(){
//			JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//			tokenRepository.setDataSource(dataSource);
//			return tokenRepository;
//		}
//		
//		@Bean
//		public RememberMeServices rememberMeServices(){
//			PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(projectName + SYSTEM_REMEMBER_KEY_SUBFFIX, userDetailsService(), tokenRepository());
//			rememberMeServices.setParameter("remember_me");
//			rememberMeServices.setCookieName(projectName+"-system-rmecookie");
//			rememberMeServices.setTokenValiditySeconds(1209600);
//			return rememberMeServices;
//		}
//		
//		@Bean
//		public TokenUserDetailsService tokenUserDetailsService(){
//			return new TokenUserDetailsService(userService);
//		}
//		
//		@Bean
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}
//		
//		@Bean
//		public AccessTokenServices accessTokenServices(){
//			return new AccessTokenServices(tokenUserDetailsService(),"_access_token");
//		}
//		
//		@Bean
//		public AccessTokenLoginFilter accessTokenLoginFilter() throws Exception{
//			return new AccessTokenLoginFilter(authenticationManagerBean(),accessTokenServices());
//		}
//		
//		protected void configure(HttpSecurity http) throws Exception {
//			
//			http
//				.authenticationProvider(systemAuthenticationProvider())
//				.authenticationProvider(accessTokenAuthenticationProvider())
//				.addFilterBefore(validateCodeCheckFilter(), UsernamePasswordAuthenticationFilter.class);
//				
//			http
//				.addFilterBefore(accessTokenLoginFilter(),ValidateCodeCheckFilter.class)
//				.antMatcher("/system/**")  //相当于<sec:http auto-config="false" pattern="/system/**">     
////				.csrf().disable()       //可以禁止csrf保护，这样就不需要每次POST请求都需要提交一个csrf token了。
//				.authorizeRequests()
//					.antMatchers("/system/login-window.htm",SYSTEM_LOGIN_URL).permitAll()
//					.antMatchers("/system/druid/**","/system/mr").hasRole("SYS_ADMIN")
//					.antMatchers("/system/**").hasRole("USER")
//					.and()
//				.formLogin()
//					.loginPage(SYSTEM_LOGIN_URL)
//					.loginProcessingUrl(SYSTEM_LOGIN_PROCESS_URL)
//					.usernameParameter("j_username")
//					.passwordParameter("j_password")
//					.failureUrl(SYSTEM_LOGIN_URL + "?error=1")
//					.defaultSuccessUrl("/system/main.htm", true)//由于是后台，只有一个main.htm可以用，所以登录成功后总是重定向到该页，其他前台用户的登录可能不需要这样做。用main.htm而不用index.htm是避免攻击工具使用默认页
//					.and()
//				.logout()
//					//.logoutUrl("/system/logout") //在csrf模式下这样设置的话，Logout只能POST模式提交，而且需要提交csrf token。
//					.logoutRequestMatcher(new AntPathRequestMatcher(SYSTEM_LOGOUT_URL)) //使用这种配置，在csrf模式下 get方式就可以logout，也不需要csrf token.
//					.logoutSuccessUrl(SYSTEM_LOGIN_URL)
//					.and()
//				.sessionManagement()
//					.sessionFixation().newSession()
//					.maximumSessions(1)//默认一个账号只有一个用户登录，如果需要不限制登录用户数，设置为-1。限制一个用户登录的
//						.sessionRegistry(sessionRegistry())//可以管理对应的session注册表，可以方便踢出用户登录等管理行为。
//						.and()
//					.and()
//				.rememberMe()
//					.key(projectName + SYSTEM_REMEMBER_KEY_SUBFFIX)
//					.rememberMeServices(rememberMeServices())
//					.and()
//					;
//			System.out.println("HttpSecurity configue done");
//		}
//		
//	}
//
//	@Configuration                                                   
//	public class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.authorizeRequests()
//					.anyRequest().permitAll();
//		}
//		
//	}
//	
//	private @Resource UserService userService;
//	
//	private @Resource DataSource dataSource;

}
