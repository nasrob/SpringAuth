package com.nboukehil.springldap.security;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.nboukehil.springldap.constant.PwdEncodingAlgo;
import com.nboukehil.springldap.model.LdapAuthStructure;

@Configuration
@EnableWebSecurity
@ComponentScan("com.nboukehil.springldap.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LdapAuthStructure ldapAuthStructure;
	
	private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/css/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login").permitAll()
			.antMatchers("/adminPage/").hasAnyAuthority("ADMIN")
			.antMatchers("/userPage").hasAnyAuthority("USER")
			.anyRequest().fullyAuthenticated()
			.and()
			.oauth2Login().loginPage("/login")//.permitAll()
			.defaultSuccessUrl("/privatePage", true)
			.failureUrl("/login?error=true")
			.and()
			.logout().permitAll().logoutSuccessUrl("/login?logout=true");
		
		logger.info("Configure method called to make resources secure ...");
		
//		super.configure(http);		
	}
	/*
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.ldapAuthentication()
			.userDnPatterns(ldapAuthStructure.getUserDnPattern())
			.userSearchBase(ldapAuthStructure.getUserSearchBase())
			.groupSearchBase(ldapAuthStructure.getGroupSearchBase())
			.groupSearchFilter("member={0}").rolePrefix("")
			.contextSource()
			.url(ldapAuthStructure.getLdapUrl() + "/" + ldapAuthStructure.getLdapBase())
			.managerDn(ldapAuthStructure.getLdapManagerDn())
			.managerPassword(ldapAuthStructure.getLdapManagerPwd())
			.and()
			.passwordCompare()
			.passwordEncoder(new BCryptPasswordEncoder())
			.passwordAttribute("userPassword");
		
		logger.info("configure method called to build Authentication Manager ...");
	}*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		
		encoders.put(PwdEncodingAlgo.BCrypt.getStatus(), new BCryptPasswordEncoder());
		encoders.put(PwdEncodingAlgo.Pbkdf2.getStatus(), new Pbkdf2PasswordEncoder());
		encoders.put(PwdEncodingAlgo.SCrypt.getStatus(), new SCryptPasswordEncoder());
		
		return new DelegatingPasswordEncoder(PwdEncodingAlgo.BCrypt.getStatus(), encoders);
	}
}
