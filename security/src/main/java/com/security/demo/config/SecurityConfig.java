package com.security.demo.config;


import com.security.demo.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private MyUserDetailService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","index","/login","/login-error","/401","/css/**","/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage( "/login" ).failureUrl( "/login-error" )
                .and()
                .exceptionHandling().accessDeniedPage( "/401" );
        http.logout().logoutSuccessUrl( "/" );
    }



    @Autowired
    private void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{

      auth.userDetailsService( userService ).passwordEncoder( new PasswordEncoder() {
          //对密码进行加密

          @Override
          public String encode(CharSequence charSequence) {
              System.out.println(charSequence.toString());
              return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
          }
          //对密码进行判断匹配
          @Override
          public boolean matches(CharSequence charSequence, String s) {
              String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
              boolean res = s.equals( encode );
              return res;
          }
      } );
  }

}
