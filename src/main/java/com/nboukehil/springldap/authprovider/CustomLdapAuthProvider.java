package com.nboukehil.springldap.authprovider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.nboukehil.springldap.model.LdapGrantedAuthority;
import com.nboukehil.springldap.service.LdapAuthService;

@Component
public class CustomLdapAuthProvider implements AuthenticationProvider{

	@Autowired
	LdapAuthService ldapAuthService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		
		boolean isAuthenticate = ldapAuthService.authenticateLdapUserWithContext(username, password);
		
		if (isAuthenticate == true) {
			List<LdapGrantedAuthority> userRoles = ldapAuthService.getUserAuthorities(username);
			return new UsernamePasswordAuthenticationToken(username, password, userRoles);
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
