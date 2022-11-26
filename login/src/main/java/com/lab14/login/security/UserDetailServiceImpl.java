package com.lab14.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lab14.login.entity.User;
import com.lab14.login.repositoriy.UserRepository;

@Service //Se anota con service cuando se quiera trabajar con la bse de datos
public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                    .findOneByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("The user with email " + email + " does not exist"));
        return new UserDetailsImpl(user);
    }
}
