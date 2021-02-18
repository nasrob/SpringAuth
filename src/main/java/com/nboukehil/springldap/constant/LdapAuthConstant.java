package com.nboukehil.springldap.constant;

public interface LdapAuthConstant {

	String PAGE_TITLE = "pageTitle";
	
	String TITLE_HOME_PAGE = "Home";
	String TITLE_PRIVATE_PAGE = "Private";
	String TITLE_LOGIN_PAGE = "Login";
	
	String LDAP_USER_SEARCH = "ou=users";
	String LDAP_GROUP_SEARCH = "ou=roles";
	String LDAP_USER_DN_PATTERN = "uid={0},ou=users";
}
