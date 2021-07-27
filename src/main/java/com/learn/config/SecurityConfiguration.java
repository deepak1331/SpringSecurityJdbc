package com.learn.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		/* Uncomment below code to let spring create the schema and insert the users values as mentioned below
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.withDefaultSchema()
			.withUser(User.withUsername("user").password("pass").roles(Roles.USER.name()))
			.withUser(User.withUsername("admin").password("pass").roles(Roles.ADMIN.name()));
			*/
		
		//Now since we have added the schema.sql and data.sql the above code is commented
		auth.jdbcAuthentication().dataSource(dataSource);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole(Roles.ADMIN.name())
			.antMatchers("/user/**").hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())
			.antMatchers("/").permitAll()
			.and().formLogin();
	}
	
	@Bean
	public PasswordEncoder getEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
