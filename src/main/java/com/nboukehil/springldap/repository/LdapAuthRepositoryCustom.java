package com.nboukehil.springldap.repository;

import java.util.List;

import com.nboukehil.springldap.model.LdapAuthUser;
import com.nboukehil.springldap.model.LdapGrantedAuthority;

public interface LdapAuthRepositoryCustom {

	LdapAuthUser findByUserName(String username);
	List<LdapAuthUser> findByMatchingUserName(String username);
	boolean authenticateLdapUserWithContext(String username, String password);
	boolean authenticateLdapUserWithLdapQuery(String username, String password);
	void create(LdapAuthUser ldapAuthUser);
	void deleteFromTemplate(LdapAuthUser ldapAuthUser);
	void createByBindOperation(LdapAuthUser ldapAuthUser);
	void deleteFromTemplateWithUnbind(String username);
	void updateWithTemplate(LdapAuthUser ldapAuthUser);
	LdapAuthUser findByUid(String uid);
	List<LdapAuthUser> findAllWithTemplate();
	List<LdapAuthUser> findBySurname(String surname);
	List<LdapGrantedAuthority> getUserAuthorities(String username);
}
