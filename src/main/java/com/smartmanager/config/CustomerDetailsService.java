package com.smartmanager.config;

import com.smartmanager.dao.UserRepository;
import com.smartmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user= userRepository.getUserByUserName(userName);
        if(user==null){
            throw new UsernameNotFoundException("Could not found user");
        }
        CustomUserDetails cud= new CustomUserDetails(user);
        return cud;
    }
}
