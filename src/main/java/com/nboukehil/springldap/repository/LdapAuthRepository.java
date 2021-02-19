package com.nboukehil.springldap.repository;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import com.nboukehil.springldap.model.LdapAuthUser;

@Repository
public interface LdapAuthRepository extends LdapRepository<LdapAuthUser> {

}
