package com.nboukehil.springldap.repository.impl;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.DirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;

import com.nboukehil.springldap.mapper.LdapRoleMapper;
import com.nboukehil.springldap.model.LdapAuthUser;
import com.nboukehil.springldap.model.LdapGrantedAuthority;
import com.nboukehil.springldap.repository.LdapAuthRepositoryCustom;

public class LdapAuthRepositoryCustomImpl implements LdapAuthRepositoryCustom {

	private Logger logger = LoggerFactory.getLogger(LdapAuthRepositoryCustomImpl.class);
	
	private LdapTemplate ldapTemplate;
	
	@Override
	public LdapAuthUser findByUserName(String username) {
		return ldapTemplate.findOne(LdapQueryBuilder.query().where("uid").is(username), LdapAuthUser.class);
	}

	@Override
	public List<LdapAuthUser> findByMatchingUserName(String username) {
		return ldapTemplate.find(LdapQueryBuilder.query().where("uid").like(username), LdapAuthUser.class);
	}

	@Override
	public boolean authenticateLdapUserWithContext(String username, String password) {
		DirContext ctx = null;
		try {
			String userDn = getDnForUser(username);
			ctx = ldapTemplate.getContextSource().getContext(userDn, password);
			return true;
		} catch (Exception ex) {
			logger.error("Authentication failed ", ex.getMessage(), ex);
			return false;
		} finally {
			LdapUtils.closeContext(ctx); // have to close DirContext
		}
	}

	
	@Override
	public boolean authenticateLdapUserWithLdapQuery(String username, String password) {
		try {
			ldapTemplate.authenticate(LdapQueryBuilder.query().where("uid").is(username), password);
			return true;
		} catch (Exception ex) {
			logger.error("Exception occured while authenticating user with name " + username, ex.getMessage(), ex);
		}
		return false;
	}

	@Override
	public void create(LdapAuthUser ldapAuthUser) {
		ldapAuthUser.setNew(true);
		ldapTemplate.create(ldapAuthUser);
	}

	@Override
	public void deleteFromTemplate(LdapAuthUser ldapAuthUser) {
		ldapTemplate.delete(ldapAuthUser);
	}

	@Override
	public void createByBindOperation(LdapAuthUser ldapAuthUser) {
		DirContextOperations ctx = new DirContextAdapter();
		ctx.setAttributeValues("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
		ctx.setAttributeValue("cn", ldapAuthUser.getFirstname());
		ctx.setAttributeValue("sn", ldapAuthUser.getSurName());
		ctx.setAttributeValue("uid", ldapAuthUser.getUserName());
		ctx.setAttributeValue("userPasswrod", ldapAuthUser.getPassword());

		Name dn = LdapNameBuilder.newInstance().add("ou=users")
												.add("uid=akiyama")
												.build();
		ctx.setDn(dn);
		ldapTemplate.bind(ctx);
	}

	@Override
	public void deleteFromTemplateWithUnbind(String username) {
		Name dn = LdapNameBuilder.newInstance().add("ou=user")
												.add("uid" + username)
												.build();
		ldapTemplate.unbind(dn);
	}

	@Override
	public void updateWithTemplate(LdapAuthUser ldapAuthUser) {
		ldapTemplate.update(ldapAuthUser);
	}

	@Override
	public LdapAuthUser findByUid(String uid) {
		return ldapTemplate.findOne(LdapQueryBuilder.query().where("uid").is(uid), LdapAuthUser.class);
	}

	@Override
	public List<LdapAuthUser> findAllWithTemplate() {
		return ldapTemplate.findAll(LdapAuthUser.class);
	}

	@Override
	public List<LdapAuthUser> findBySurname(String surname) {
		return ldapTemplate.find(LdapQueryBuilder.query().where("sn").is(surname), LdapAuthUser.class);
	}

	@Override
	public List<LdapGrantedAuthority> getUserAuthorities(String username) {
		AndFilter groupFilter = new AndFilter();
		groupFilter.and(new EqualsFilter("objectclass", "groupOfNames"));
		groupFilter.and(new EqualsFilter("member", "uid" + username + ",ou=users,o=packetPublisher"));
		List<LdapGrantedAuthority> userRoleLst = ldapTemplate.search(LdapQueryBuilder.query().filter(groupFilter), new LdapRoleMapper());
		
		return userRoleLst;
	}
	
	private String getDnForUser(String uid) {
		List<String> result = ldapTemplate.search(
				LdapQueryBuilder.query().where("uid").is(uid),
				new AbstractContextMapper<String>() {
					protected String doMapFromContext(DirContextOperations ctx) {
						logger.info("##### NameInNameSpace ==> " + ctx.getNameInNamespace());
						return ctx.getNameInNamespace();
					}
				});
		if (result.size() != 1) {
			throw new RuntimeException("User not found or not unique");
		}
		
		return result.get(0);
	}

}
