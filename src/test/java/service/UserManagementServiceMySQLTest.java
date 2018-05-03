package service;

import demo.model.User;
import demo.model.builder.UserBuilder;
import demo.repository.UserRepository;
import demo.service.UserManagementServiceMySQL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserManagementServiceMySQLTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserManagementServiceMySQL userManagementServiceMySQL;

    User user;


    @Before
    public void init() {
        user = new UserBuilder()
                .setName("user")
                .setId(1L)
                .setPassword("LongPassword!1")
                .setUsername("user@UT.com")
                .setRoles("administrator")
                .build();
    }

    @Test
    public void registerTest(){
        Assert.assertFalse(userManagementServiceMySQL.register(
                user.getUsername(), user.getName(),
                user.getPassword(), user.getRoles()).hasErrors());
    }

    /*
    @Test
    public void loginTest(){
        when(userRepository.findByUsernameAndPassword(user.getUsername(),new BCryptPasswordEncoder().encode(user.getPassword()))).thenReturn(user);
        System.out.println(userManagementServiceMySQL.login(user.getUsername(),user.getPassword()).getFormattedErrors());
        Assert.assertFalse(userManagementServiceMySQL.login(user.getUsername(),user.getPassword())
        .hasErrors());
    }*/

    @Test
    public void updateUserTest(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        System.out.println(userManagementServiceMySQL.updateUser(user.getUsername(), user.getName(),"LongPassword!2",
                user.getRoles()).getFormattedErrors());
        Assert.assertFalse(
                userManagementServiceMySQL.updateUser(user.getUsername(), user.getName(),"LongPassword!2",
                        user.getRoles()).hasErrors());

    }

    @Test
    public void getUserTest(){
        when(userRepository.findByUsername("user")).thenReturn(user);
        Assert.assertEquals(userManagementServiceMySQL.getUser("user").getName(),"user");
    }

    @Test
    public void deleteUserTest(){
        when(userRepository.findByUsername("user")).thenReturn(user);
        Assert.assertTrue(userManagementServiceMySQL.deleteUser("user"));
    }

    @Test
    public void getAllUsersTest(){
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(userManagementServiceMySQL.getAllUsers().size(),0);
    }

    @Test
    public void getAllDoctors(){
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(userManagementServiceMySQL.getAllDoctors().size(),0);
    }
}
