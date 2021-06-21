package my.myungjin.academyDemo.configure;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.security.EntryPointUnauthorizedHandler;
import my.myungjin.academyDemo.security.MyAccessDeniedHandler;
import my.myungjin.academyDemo.security.MyAuthenticationProvider;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final EntryPointUnauthorizedHandler unauthorizedHandler;

    private final MyAccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/templates/**", "/h2/**",
                "/v2/api-docs", "/configuration/ui", "/configuration/security",
                "/swagger-ui.html", "/swagger-resources", "/webjars/**", "/swagger/**");
    }

    @Bean
    public MyAuthenticationProvider authenticationProvider(MemberService memberService, AdminService adminService) {
        return new MyAuthenticationProvider(memberService, adminService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // TODO 인가
/*    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter());
        // 모든 voter가 승인해야 해야한다.
        return new UnanimousBased(decisionVoters);
    }*/

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
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler)
                //.and()
                // No session will be created or used by spring security
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/api/mall/join").permitAll()
                .antMatchers("/api/mall/find/**").permitAll()
                .antMatchers("/api/mall/item/**").permitAll()
                .antMatchers("/api/mall/member/password").permitAll()
                .antMatchers("/api/mall/member/**").hasRole(Role.MEMBER.name())
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                //.accessDecisionManager(accessDecisionManager())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable()
                .logout(logout -> logout.logoutSuccessHandler((request, response, authentication) -> {
                    Role role = ((User)(authentication.getDetails())).getRole();
                    if(role.equals(Role.ADMIN)){
                        response.sendRedirect("/admin/login");
                    } else {
                        response.sendRedirect("/");
                    }
                }))
                .sessionManagement()
                .maximumSessions(1) /* session 허용 갯수 */
                .expiredSessionStrategy(sessionInformationExpiredEvent -> {
                    String requestUri = sessionInformationExpiredEvent.getRequest().getRequestURI();
                    HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
                    if(requestUri.startsWith("/mall") || requestUri.startsWith("/api/mall")){
                        response.sendRedirect("/mall/login");
                    } else if(requestUri.startsWith("/admin") || requestUri.startsWith("/api/admin")){
                        response.sendRedirect("/admin/login");
                    }
                })
                .maxSessionsPreventsLogin(true)
        ;
    }


}
