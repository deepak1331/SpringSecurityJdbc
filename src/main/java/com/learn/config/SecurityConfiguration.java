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
		
		//Now since we have added the default schema, i.e schema.sql and data.sql the above code is commented
//		auth.jdbcAuthentication().dataSource(dataSource);
		
		//If we're using different db which has different tables to store user/roles details, we can declare that using below queries.
		
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, enabled from users where username = ?")
			.authoritiesByUsernameQuery("SELECT username, authority from authorities where username = ?");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		  http.authorizeRequests()
		  .antMatchers("/admin/**").hasRole(Roles.ADMIN.name())
		  .antMatchers("/user/**").hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())
          .antMatchers("/h2-console/**").permitAll()									//allow h2 console access to admins only
          .anyRequest().authenticated()													//all other urls can be access by any authenticated role
          .and().formLogin()															//enable form login instead of basic login
          .and().csrf().ignoringAntMatchers("/h2-console/**")							//don't apply CSRF protection to /h2-console
          .and().headers().frameOptions().sameOrigin();									//Since the H2 database console runs inside a frame, you need to enable this in in Spring Security.
	
	}
	
	@Bean
	public PasswordEncoder getEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
