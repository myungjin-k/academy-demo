package my.myungjin.academyDemo.configure;

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // user와 password 정보를 h2와 같은 in memory에 저장
                .inMemoryAuthentication()
                .withUser("mjkim").password(passwordEncoder().encode("mjkim_password")).roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/templates/**", "/h2/**");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross Site Request Forgery : 사이트 간 요청 위조) protection 해제
                // 개발 옵션
               // .csrf()
                //.disable()
                // 개발 옵션
                //.headers()
                //.disable()
                //.exceptionHandling()
                //.accessDeniedHandler(accessDeniedHandler)
                //.authenticationEntryPoint(unauthorizedHandler)
                //.and()
                // No session will be created or used by spring security
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                //.antMatchers("/member/**").authenticated()
                //.accessDecisionManager(accessDecisionManager())
                .anyRequest().authenticated()
                .and()
                //.formLogin()
                //.disable()
                .httpBasic()
        ;
        //http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
