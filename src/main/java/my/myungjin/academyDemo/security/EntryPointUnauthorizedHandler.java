package my.myungjin.academyDemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.web.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static my.myungjin.academyDemo.web.Response.ERROR;

@Component
@RequiredArgsConstructor
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

  static Response<?> E401 = ERROR("Authentication error (cause: unauthorized)", HttpStatus.UNAUTHORIZED);

  private final ObjectMapper om;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException, ServletException {
    String redirectURI = request.getRequestURI().contains("admin") ? "/admin/login" : "/mall/login";

    response.sendRedirect(redirectURI);
    /*response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("content-type", "application/json");
    response.getWriter().write(om.writeValueAsString(E401));
    response.getWriter().flush();
    response.getWriter().close();*/
  }

}