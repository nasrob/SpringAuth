package com.nboukehil.springldap.model;

import org.springframework.security.core.GrantedAuthority;

public class LdapGrantedAuthority implements GrantedAuthority{

	String authority;
	
		
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
}
