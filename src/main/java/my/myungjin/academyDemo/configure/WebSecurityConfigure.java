package my.myungjin.academyDemo.configure;

import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.security.MyAuthenticationProvider;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // user와 password 정보를 h2와 같은 in memory에 저장
                .inMemoryAuthentication()
                .withUser("mjkim").password(passwordEncoder().encode("mjkim_password")).roles("ADMIN");
    }
*/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/templates/**", "/h2/**");
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, MyAuthenticationProvider authenticationProvider) {
        builder.authenticationProvider(authenticationProvider);
    }

    @Bean
    public MyAuthenticationProvider authenticationProvider(MemberService memberService, AdminService adminService) {
        return new MyAuthenticationProvider(memberService, adminService);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross Site Request Forgery : 사이트 간 요청 위조) protection 해제
                // 개발 옵션
                .csrf()
                .disable()
                // 개발 옵션
                .headers()
                .disable()
                //.exceptionHandling()
                //.accessDeniedHandler(accessDeniedHandler)
                //.authenticationEntryPoint(unauthorizedHandler)
                //.and()
                // No session will be created or used by spring security
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/member/join").permitAll()
                .antMatchers("/member/password").permitAll()
                .antMatchers("/member/**").authenticated()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                //.accessDecisionManager(accessDecisionManager())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable()
        ;
        //http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
