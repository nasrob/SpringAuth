package com.nboukehil.springldap.service;

import java.util.List;
import java.util.Optional;

import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import com.nboukehil.springldap.model.LdapAuthUser;
import com.nboukehil.springldap.model.LdapGrantedAuthority;
import com.nboukehil.springldap.repository.LdapAuthRepository;

@Service
public class LdapAuthService {

	private Logger logger = LoggerFactory.getLogger(LdapAuthService.class);
	
	@Autowired
	private LdapAuthRepository ldapAuthRepository;
	
	public void addUser(LdapAuthUser ldapAuthUser) {
		Name dn = LdapNameBuilder.newInstance()
								.add("uid", ldapAuthUser.getUserName())
								.add("ou", "users")
								.build();
		
		boolean isExist = ldapAuthRepository.existsById(dn);
		if (isExist == false) {
			ldapAuthRepository.save(ldapAuthUser);
		} else {
			logger.info("User with username " + ldapAuthUser.getUserName() + " already exists");
		}
	}
	
	public LdapAuthUser getUser(String userName) {
		Optional<LdapAuthUser> ldapUserOptional = ldapAuthRepository
													.findOne(LdapQueryBuilder.query().where("uid").is(userName));
		if (ldapUserOptional.isPresent()) {
			return ldapUserOptional.get();
		} else {
			return null;
		}
	}
	
	public void updateLdapUser(LdapAuthUser ldapUser) {
		ldapAuthRepository.save(ldapUser);
	}
	
	public void deleteUser(String userName) {
		Optional<LdapAuthUser> ldapUserOptional = ldapAuthRepository
													.findOne(LdapQueryBuilder.query().where("uid").is(userName));
		if (ldapUserOptional.isPresent()) {
			ldapAuthRepository.delete(ldapUserOptional.get());
		} else {
			logger.info("User with username " + userName + " does not exist ");
		}
	}
	
	public void deleteWithTemplate() {
		ldapAuthRepository.deleteFromTemplate(null);
	}
	
	public void createUserWithLdapTemplate() {
		ldapAuthRepository.create(null);
	}
	
	public boolean authenticateLdapUserWithContext(String username, String password) {
		return ldapAuthRepository.authenticateLdapUserWithContext(username, password);
	}
	
	public boolean authenticateLdapUserWithLdapQuery(String username, String password) {
		return ldapAuthRepository.authenticateLdapUserWithLdapQuery(username, password);
	}
	
	public List<LdapGrantedAuthority> getUserAuthorities(String username) {
		return ldapAuthRepository.getUserAuthorities(username);
	}
}
