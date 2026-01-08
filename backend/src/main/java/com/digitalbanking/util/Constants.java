package com.digitalbanking.util;

public class Constants {
  public static final String API_BASE_PATH = "/api/v1";
  public static final String AUTH_PATH = API_BASE_PATH + "/auth";
  public static final String ACCOUNT_PATH = API_BASE_PATH + "/accounts";
  public static final String TRANSACTION_PATH = API_BASE_PATH + "/transactions";
  public static final String PAYMENT_PATH = API_BASE_PATH + "/payments";
  public static final String LOAN_PATH = API_BASE_PATH + "/loans";
  public static final String ASSURANCE_PATH = API_BASE_PATH + "/assurance";
  public static final String ADMIN_PATH = API_BASE_PATH + "/admin";

  public static final String JWT_SECRET = "your_jwt_secret_key_should_be_at_least_256_bits_long_for_security";
  public static final long JWT_EXPIRATION = 86400000; // 24 hours

  public static final String ROLE_CUSTOMER = "CUSTOMER";
  public static final String ROLE_ADMIN = "ADMIN";
}
