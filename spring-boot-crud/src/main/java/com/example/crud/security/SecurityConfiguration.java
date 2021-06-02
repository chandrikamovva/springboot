package com.example.crud.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.crud.filter.AuthTokenFilter;
import com.example.crud.util.AuthEntryPointJwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	AuthEntryPointJwt unauthorizedHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		//auth.parentAuthenticationManager(authenticationManagerBean()).userDetailsService(userDetailsService);
		 //auth.userDetailsService(userDetailsService);
		/*
		 * auth.inMemoryAuthentication().withUser("chandrika") .password("123456")
		 * .roles("USER") .and() .withUser("movva") .password("123456") .roles("ADMIN");
		 */
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/api/auth/**").permitAll()
		.antMatchers("/api/test/**").permitAll()
		.anyRequest().authenticated();

	http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		

		/*
		 * http.authorizeRequests().antMatchers("/api/tutorials/{id}").hasRole("ADMIN")
		 * .antMatchers("/api/tutorials").hasAnyRole("ADMIN","ROLE_USER")
		 * .antMatchers("/").permitAll().and().formLogin();
		 */
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		// TODO Auto-generated method stub
		return new AuthTokenFilter();
	}

	

	@Bean
	@Primary
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	/*
	 * @Bean public BCryptPasswordEncoder getPasswordEncoder() { return new
	 * BCryptPasswordEncoder(); }
	 */

}
