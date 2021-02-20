package com.nboukehil.springldap.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.nboukehil.springldap.constant.LdapAuthConstant;
import com.nboukehil.springldap.model.LdapAuthStructure;

@Configuration
@ComponentScan(basePackages = {"com.nboukehil.springldap.config"})
public class LdapDataConfig {

	private Logger logger = LoggerFactory.getLogger(LdapDataConfig.class); 
	
	@Value("${spring.ldap.urls}")
	private String ldapUrls;
	
	@Value("${spring.ldap.base}")
	private String ldapBase;
	
	@Value("${spring.ldap.password}")
	private String ldapManagerPwd;
	
	@Value("${spring.ldap.username}")
	private String ldapManagerUserName;
	
	@Bean("ldapAuthStrecture")
	public LdapAuthStructure getLdapAuthStructure() {
		LdapAuthStructure authStructure = new LdapAuthStructure();
		
		authStructure.setLdapUrl(ldapUrls);
		authStructure.setLdapBase(ldapBase);
		authStructure.setLdapManagerDn(ldapManagerUserName);
		authStructure.setLdapManagerPwd(ldapManagerPwd);
		authStructure.setUserDnPattern(LdapAuthConstant.LDAP_USER_DN_PATTERN);
		authStructure.setUserSearchBase(LdapAuthConstant.LDAP_USER_SEARCH);
		authStructure.setGroupSearchBase(LdapAuthConstant.LDAP_GROUP_SEARCH);
		
		return authStructure;
	}
	
	@Bean("ldapTemplate")
	public LdapTemplate getLdapTemplate() {
		return new LdapTemplate(getLdapContextSrc());
	}

	@Bean
	public ContextSource getLdapContextSrc() {
		LdapContextSource ldapContextSrc = new LdapContextSource();
		ldapContextSrc.setUrl(ldapUrls);
		ldapContextSrc.setUserDn(ldapManagerUserName);
		ldapContextSrc.setPassword(ldapManagerPwd);
		ldapContextSrc.setBase(ldapBase);
		ldapContextSrc.afterPropertiesSet();
		return ldapContextSrc;
	}
}
