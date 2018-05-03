package demo.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Get the role of logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        System.out.println(role);
        String targetUrl = "";
        if(role.contains("administrator")) {
            targetUrl = "/administrator";
        } else if(role.contains("secretary")) {
            targetUrl = "/secretary";
        } else if (role.contains("doctor")){
            targetUrl = "/doctor";
        }

        return targetUrl;
    }

}
