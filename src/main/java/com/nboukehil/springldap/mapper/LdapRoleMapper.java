package com.nboukehil.springldap.mapper;

import javax.naming.NamingException;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import com.nboukehil.springldap.model.LdapGrantedAuthority;

public class LdapRoleMapper implements ContextMapper<LdapGrantedAuthority> {

	@Override
	public LdapGrantedAuthority mapFromContext(Object ctx) throws NamingException {
		DirContextAdapter adapter = (DirContextAdapter) ctx;
		String role = adapter.getStringAttribute("cn");
		LdapGrantedAuthority ldapGrantedAuthority = new LdapGrantedAuthority();
		ldapGrantedAuthority.setAuthority(role);
		return ldapGrantedAuthority;
	}

}
