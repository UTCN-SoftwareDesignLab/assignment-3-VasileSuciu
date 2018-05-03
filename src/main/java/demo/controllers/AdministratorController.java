package demo.controllers;


import demo.model.User;
import demo.model.validation.Notification;
import demo.service.UserManagementServiceMySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AdministratorController {

    @Autowired
    private UserManagementServiceMySQL userManagementServiceMySQL;

    @RequestMapping(value = "/administrator", method = RequestMethod.GET)
    public String showAdministratorPage(ModelMap model){
            List<User> userList = userManagementServiceMySQL.getAllUsers();
            model.addAttribute("userList", userList);
            model.addAttribute("user", new User());
            model.addAttribute("errorMessage2", "");
        return "administrator";
    }

    @RequestMapping(value = "/administrator", params="deleteBtn", method = RequestMethod.POST)
    public String handleUserDelete(ModelMap model,  @ModelAttribute("user") User user) {
        userManagementServiceMySQL.deleteUser(user.getUsername());
        model.addAttribute("userList",userManagementServiceMySQL.getAllUsers());
        return "administrator";
    }

    @RequestMapping(value = "/administrator", params="updateBtn", method = RequestMethod.POST)
    public String handleUserUpdate(ModelMap model,  @ModelAttribute("user") User user) {
        Notification<Boolean> notification =  userManagementServiceMySQL.updateUser(user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getRoles());
        if (notification.hasErrors()){
            model.addAttribute("errorMessage2",notification.getFormattedErrors());
        }
        else{
            model.addAttribute("errorMessage2", "");
        }
        model.addAttribute("userList",userManagementServiceMySQL.getAllUsers());
        return "administrator";
    }

    @RequestMapping(value = "/administrator", params="createBtn", method = RequestMethod.POST)
    public String handleUserCreate(ModelMap model, @ModelAttribute("user") User user){
        Notification<Boolean> notification = userManagementServiceMySQL.register(user.getUsername(), user.getName(),
                user.getPassword(), user.getRoles());
        if (notification.hasErrors()){
            model.addAttribute("errorMessage2",notification.getFormattedErrors());
        }
        else{
            model.addAttribute("errorMessage2", "");
        }
        model.addAttribute("userList",userManagementServiceMySQL.getAllUsers());
        return "administrator";
    }

    @RequestMapping(value = "/administrator", params="secretaryBtn", method = RequestMethod.GET)
    public String handleSecretaryView(ModelMap model){
        return "redirect:/secretary";
    }

    @RequestMapping(value = "/administrator", params="doctorBtn", method = RequestMethod.GET)
    public String handleDoctorView(ModelMap model){
        return "redirect:/doctor";
    }
}
