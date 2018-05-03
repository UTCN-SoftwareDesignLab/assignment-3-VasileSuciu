package demo.configuration;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errMsg="";
        if(exception.getClass().isAssignableFrom(BadCredentialsException.class)){
            errMsg="Invalid username or password.";
        }else{
            errMsg="Unknown error - "+exception.getMessage();
        }
        request.getSession().setAttribute("errorMessage", errMsg);
        response.sendRedirect("/login");
    }
}
