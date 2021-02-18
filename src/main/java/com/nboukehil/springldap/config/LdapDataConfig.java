package com.nboukehil.springldap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.nboukehil.springldap.config"})
public class LdapDataConfig {

	@Value("${spring.ldap.urls}")
	private String ldapUrls;
	
	@Value("${spring.ldap.base}")
	private String ldapBase;
	
	@Value("${spring.ldap.password}")
	private String ldapManagerPwd;
	
	@Value("${spring.ldap.username}")
	private String ldapManagerUserName;
	
//	@Bean("ldapAuthStrecture")
}
