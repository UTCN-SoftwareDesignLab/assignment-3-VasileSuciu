package demo.controllers;

import demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class LogInController {


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(HttpServletRequest request, ModelMap model) {
        model.addAttribute("user", new User());
        HttpSession session = request.getSession(true);
        String error = "";
        if (session.getAttribute("errorMessage")!=null){
            error = (String)session.getAttribute("errorMessage");
        }
        model.addAttribute("errorMessage", error);
        return "login";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String handleAccessDenied(ModelMap model) {
        return "accessDenied";
    }



    @RequestMapping(value = "/helloWorld", method = RequestMethod.GET)
    public String handleUserLogin(ModelMap model) {
        return "helloWorld";
    }

}
