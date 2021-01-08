package my.myungjin.academyDemo.configure.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        boolean isAdminPage = false;
        if(uri.startsWith("/admin") || uri.startsWith("/api/admin")){
            isAdminPage = true;
            if(!("127.0.0.1".equals(ip) || "14.38.17.145".equals(ip))){
                response.sendRedirect("/mall/index");
                return false;
            }
        }

        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) (session.getAttribute("SPRING_SECURITY_CONTEXT"));
        if(securityContext == null || !securityContext.getAuthentication().isAuthenticated()) {
            if(uri.equals("/mall/login") || uri.equals("/admin/login"))
                return true;
            else if(isAdminPage)
                response.sendRedirect("/admin/login");
            else
                response.sendRedirect("/mall/login");
        } else {
            session.setMaxInactiveInterval(30 * 60);
        }
        if(uri.equals("/"))
            response.sendRedirect("/mall/index");
        return true;
    }
}
