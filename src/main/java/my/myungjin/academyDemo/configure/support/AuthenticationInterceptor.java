package my.myungjin.academyDemo.configure.support;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) (session.getAttribute("SPRING_SECURITY_CONTEXT"));
        if(securityContext == null || !securityContext.getAuthentication().isAuthenticated()) {
            response.sendRedirect("/login");
            return false;
        }

        session.setMaxInactiveInterval(30 * 60);
        return true;
    }
}
