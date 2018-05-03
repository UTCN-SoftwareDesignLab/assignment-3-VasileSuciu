package demo.model.builder;

import demo.model.User;

import java.util.List;

public class UserBuilder {
    private User user;

    public UserBuilder(){
        this.user = new User();
    }

    public UserBuilder setId(Long id){
        user.setId(id);
        return this;
    }

    public UserBuilder setUsername(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder setName(String name){
        user.setName(name);
        return this;
    }

    public UserBuilder setPassword(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder setRoles(String roles){
        user.setRoles(roles);
        return this;
    }

    public User build(){
        return this.user;
    }

}
