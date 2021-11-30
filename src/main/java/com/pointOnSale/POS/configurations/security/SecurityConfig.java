package com.pointOnSale.POS.configurations.security;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;
import com.embeddigital.configurations.security.CustomAuthenticationEntryPoint;
import com.embeddigital.configurations.security.MethodSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.embeddigital.common.CONSTANTS.REMEMBER_ME_KEY;

/**
 * Security Configuration for DMC Application
 *
 * <P>Uses {@link BCryptPasswordEncoder} for user password management</P>
 * <P>RoleHierarchy is defined in {@link MethodSecurityConfig #roleHierarchy()}</P>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private RoleHierarchy roleHierarchy;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Bean
  public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
    return defaultWebSecurityExpressionHandler;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and().authorizeRequests()
        .expressionHandler(webSecurityExpressionHandler())
        // TODO : Figure out API Authentication
        .antMatchers("/actuator/**", "/webjars/**", "/css/**", "/js/**",
                     "/dmc-api/assets/**", "/passwordreset/**","/scheduledTasks/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login").permitAll()
        .defaultSuccessUrl("/secured").and()
        .logout().deleteCookies("JSESSIONID")
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .permitAll()
        .and()
        .httpBasic()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .and().csrf().ignoringAntMatchers("/dmc-api/**")
        .and()
        .rememberMe().key(REMEMBER_ME_KEY); // Default Remember Me validity is 2 weeks.

    // Adding CSRF token to Header to enable API processing
    http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);

    // We need this to allow H2 console frame to be displayed
    http.headers().frameOptions().disable();
  }


  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .mvcMatchers("/resources/**"); // Completely ignore URLs starting with /resources/
    DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(this.roleHierarchy);
    web.expressionHandler(expressionHandler);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


}



