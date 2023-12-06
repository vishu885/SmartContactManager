package com.smartContactManager.smartContact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
	import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class MyConfig extends GlobalAuthenticationConfigurerAdapter
{
	@Bean
	public UserDetailsService getUserDetails() 
	{
		return new UserDetailsServiceImpl();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetails());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
		
	}
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	/*public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().withUser("/admin/**").roles("ADMIN").and().withUser("/user/**")
		.roles("USER").and().withUser("/**").all
	}*/
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception 
	{
	        http.authorizeHttpRequests((authz)->{
				try {
					authz.requestMatchers("/user/**").hasRole("USER")
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().permitAll().and().formLogin()
						.loginPage("/signin")
						.loginProcessingUrl("/dologin")
						.defaultSuccessUrl("/user/index")
						.failureUrl("/login-fail")
						.and().csrf().disable();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	       
	        return http.build();
	}
	}

