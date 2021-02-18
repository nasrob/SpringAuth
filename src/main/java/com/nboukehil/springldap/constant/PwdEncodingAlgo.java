package com.nboukehil.springldap.constant;

public enum PwdEncodingAlgo {

	BCrypt("bcrypt"), Pbkdf2("pbkdf2"), SCrypt("scrypt");
	
	private String status;
	
	private PwdEncodingAlgo(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
