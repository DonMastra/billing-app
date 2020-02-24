package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

//Enables use of security notations decoratives
//Pre & Post enable is the same as secured, except that implementation requires to use hasRole method
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSuccessHandler successHandler;
	
	//@Autowired 
	//private DataSource dataSource;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//Configures all access for users with defined roles
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale").permitAll()
		
				//Authorizations for roles in each path (commented as one way of granting access to role users,
		//the other way is using notations decoratives with @Secured / @PreAuthorize
		/*.antMatchers("/ver/**").hasAnyRole("USER")
				.antMatchers("uploads/**").hasAnyRole("USER")
				.antMatchers("/form/**").hasAnyRole("ADMIN")
				.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
				.antMatchers("/factura/**").hasAnyRole("ADMIN")*/ 
		
				.anyRequest().authenticated()
				.and()
					.formLogin()
						.successHandler(successHandler)
						.loginPage("/login")
					.permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/error_403");
	}

	// Method used to generate and encode password, also to generate users and roles
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		//Spring Security with JDBC Authentication
		//builder.jdbcAuthentication()
		//.dataSource(dataSource)
		//.passwordEncoder(passwordEncoder)
		//Login query
		//.usersByUsernameQuery("select username, password, enabled from users where username=?")
		//Roles with username query
		//.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");

		
	}

}
