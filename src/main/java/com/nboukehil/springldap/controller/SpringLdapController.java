package com.nboukehil.springldap.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.nboukehil.springldap.constant.LdapAuthConstant;
import com.nboukehil.springldap.model.LdapAuthUser;
import com.nboukehil.springldap.service.LdapAuthService;

@Controller
public class SpringLdapController {

	private Logger logger = LoggerFactory.getLogger(SpringLdapController.class);
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;	
	
	@Autowired
	private LdapAuthService ldapAuthService;
		
	@GetMapping("/")
	public String showHomePage(Model model) {
		logger.info("This is show home page method ");
		
		setProcessingData(model, LdapAuthConstant.TITLE_HOME_PAGE);
		return "home";
	}

	@GetMapping("/privatePage")
	public String showControlPage(Model model) {
		logger.info("Private Page Method ");
		setProcessingData(model, LdapAuthConstant.TITLE_PRIVATE_PAGE);
		return "private-page";
	}
	
	@GetMapping("/adminPage")
	public String showAdminPage(Model model) {
		logger.info("admin Page Method ");
		setProcessingData(model, LdapAuthConstant.TITLE_ADMIN_PAGE);
		return "admin-page";
	}
	
	@GetMapping("/userPage")
	public String showUserPage(Model model) {
		logger.info("user Page Method ");
		setProcessingData(model, LdapAuthConstant.TITLE_USER_PAGE);
		return "user-page";
	}
	
	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error,
								@RequestParam(value = "logout", required = false) String logout,
								Model model) {
		logger.info("Login Page Url ");
		
		if (error != null) {
			model.addAttribute("error", "Invalid Credentials provided !!!");
		}
		
		if (logout != null) {
			model.addAttribute("message", "Logged Out");
		}
		
		String authorizationRequestBaseUri = "oauth2/authorization";
		Map<String, String> oauth2AuthenticationUrls = new HashMap<String, String>();
		
		Iterable<ClientRegistration> clientRegirations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		
		clientRegirations.forEach(registration -> 
										oauth2AuthenticationUrls.put(registration.getClientName(),
																	authorizationRequestBaseUri + "/" +
																	registration.getRegistrationId()));
		model.addAttribute("urls", oauth2AuthenticationUrls);
		
		setProcessingData(model, LdapAuthConstant.TITLE_LOGIN_PAGE);

		return "login";
	}
	
	@ModelAttribute("currentUserName")
	public String getCurrentUserName() {
		String name = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
				name = (String) oAuth2Token.getPrincipal().getAttributes().get("name");
			} else {
				String userName = authentication.getName();
				LdapAuthUser ldapAuthUser = ldapAuthService.getUser(userName);
				if (ldapAuthUser != null) {
					name = ldapAuthUser.getFirstname() + " " + ldapAuthUser.getSurName();
				}
			}
		}
		return name;
	}
	
	private void setProcessingData(Model model, String pageTitle) {
		model.addAttribute(LdapAuthConstant.PAGE_TITLE, pageTitle);		
	}
}
