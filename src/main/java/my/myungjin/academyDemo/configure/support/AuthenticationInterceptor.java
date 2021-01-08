package my.myungjin.academyDemo.configure.support;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        boolean isAdminPage = false;
        if(uri.startsWith("/mall") || uri.startsWith("/api/mall")){
            return true;
        }
        if(uri.startsWith("/admin") || uri.startsWith("/api/admin")){
            isAdminPage = true;
            if(!("127.0.0.1".equals(ip) || "14.38.17.145".equals(ip)))
                return false;
        }

        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) (session.getAttribute("SPRING_SECURITY_CONTEXT"));
        if(securityContext == null || !securityContext.getAuthentication().isAuthenticated()) {
            if(isAdminPage)
                response.sendRedirect("/admin/login");
            else if(uri.equals("/mall/index"))
                return true;
            else
                response.sendRedirect("/mall/login");
        } else {
            session.setMaxInactiveInterval(30 * 60);
            if(uri.equals("/"))
                response.sendRedirect("/mall/index");
        }
        return true;
    }
}
