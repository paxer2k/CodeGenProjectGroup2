package io.swagger.api.annotation;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "user", password = "password", roles = "CUSTOMER")
public @interface WithCustomerUser {
}
