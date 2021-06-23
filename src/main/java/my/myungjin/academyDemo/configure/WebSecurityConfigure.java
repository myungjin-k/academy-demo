package my.myungjin.academyDemo.configure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.security.*;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
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
    public SessionRegistry mySessionRegistry() {
        return new SessionRegistryImpl();
    }
/*

    @Bean
    public MyAuthenticationFilter authenticationFilter() {
        return new MyAuthenticationFilter(mySessionRegistry());
    }
*/

    @Bean
    public MyAuthenticationProvider authenticationProvider(MemberService memberService, AdminService adminService) {
        return new MyAuthenticationProvider(memberService, adminService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, MyAuthenticationProvider authenticationProvider) {
        builder.authenticationProvider(authenticationProvider);
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

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    /*@Bean
    public MyAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler(mySessionRegistry());
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
                .antMatchers("/admin/login")
                    .access("hasIpAddress('127.0.0.1') or hasIpAddress('14.38.17.145') or hasIpAddress('221.145.101.36')")
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                //.accessDecisionManager(accessDecisionManager())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable()
                /*.loginPage("/mall/login")
                .loginPage("/admin/login")
                //.loginProcessingUrl("/auth")
                .successHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    mySessionRegistry().registerNewSession(session.getId(), authentication.getPrincipal());
                    Role role  = ((MyAuthentication) authentication.getPrincipal()).role;
                    String redirectUri = String.valueOf(authentication.getDetails());

                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                    redirectStrategy.sendRedirect(request, response,
                            redirectUri != null ? redirectUri :
                                role == Role.ADMIN ? "/admin/codeIndex" : "/");
                })
                .and()*/
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) /*로그아웃시 세션 제거*/
                .deleteCookies("JSESSIONID") /*쿠키 제거*/
                .clearAuthentication(true) /*권한정보 제거*/
                .and()
                .sessionManagement()
                .sessionFixation().migrateSession()
                .maximumSessions(2) /* session 허용 갯수 */
                .expiredSessionStrategy(sessionInformationExpiredEvent -> {
                    String requestUri = sessionInformationExpiredEvent.getRequest().getRequestURI();
                    HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
                    if(requestUri.startsWith("/mall") || requestUri.startsWith("/api/mall")){
                        response.sendRedirect("/mall/login");
                    } else if(requestUri.startsWith("/admin") || requestUri.startsWith("/api/admin")){
                        response.sendRedirect("/admin/login");
                    }
                })
                .sessionRegistry(mySessionRegistry())
                .maxSessionsPreventsLogin(true)

        ;
 //       http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}