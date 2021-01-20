package my.myungjin.academyDemo.security;

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
        boolean isAdminPage = uri.startsWith("/admin"),
                isMallPage = uri.startsWith("/mall"),
                isHome = uri.equals("/mall/index"),
                isPwdChangePage = uri.startsWith("/mall/changePassword");

        if(isHome || isPwdChangePage)
            return true;

        if(isAdminPage && !("127.0.0.1".equals(ip) || "14.38.17.145".equals(ip))){
            response.sendRedirect("/mall/index");
            return false;
        }

        if(uri.equals("/")){
            response.sendRedirect("/mall/index");
            return false;
        }

        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if(securityContext != null && securityContext.getAuthentication().isAuthenticated()) {
            session.setMaxInactiveInterval(30 * 60);
            return true;
        } else {
            if(uri.equals("/mall/login") || uri.equals("/admin/login"))
                return true;
            else if(isAdminPage)
                response.sendRedirect("/admin/login");
            else if(isMallPage)
                response.sendRedirect("/mall/login");
        }
        return false;
    }
}
