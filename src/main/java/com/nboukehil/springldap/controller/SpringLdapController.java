package com.nboukehil.springldap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nboukehil.springldap.constant.LdapAuthConstant;
import com.nboukehil.springldap.service.LdapAuthService;

@Controller
public class SpringLdapController {

	private Logger logger = LoggerFactory.getLogger(SpringLdapController.class);
	
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
	
	private void setProcessingData(Model model, String pageTitle) {
		model.addAttribute(LdapAuthConstant.PAGE_TITLE, pageTitle);		
	}
}