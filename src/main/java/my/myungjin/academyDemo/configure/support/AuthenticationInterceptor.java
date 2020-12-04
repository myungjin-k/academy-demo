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
        int port = request.getServerPort();
        if(uri.equals("/")){
            if(port == 7090)
                response.sendRedirect("/index");
            else if(port == 7091)
                response.sendRedirect("/adminLogin");
            return false;
        }


        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) (session.getAttribute("SPRING_SECURITY_CONTEXT"));
        if(securityContext == null || !securityContext.getAuthentication().isAuthenticated()) {
            if(port == 7090)
                response.sendRedirect("/login");
            else if(port == 7091)
                response.sendRedirect("/adminLogin");
            return false;
        }

        session.setMaxInactiveInterval(30 * 60);
        return true;
    }
}
