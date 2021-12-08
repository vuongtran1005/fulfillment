package com.bluebelt.fulfillment.service.impl;

import com.bluebelt.fulfillment.model.user.User;
import com.bluebelt.fulfillment.repository.UserRepository;
import com.bluebelt.fulfillment.security.UserPrincipal;
import com.bluebelt.fulfillment.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    private final UserRepository userDAO;

    @Override
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userDAO.findById(id).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User not found with id: %s", id)));
        return UserPrincipal.build(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("OK");
        User user = userDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with this username or email: %s", usernameOrEmail)));
        System.out.println("OK1");
        System.out.println(UserPrincipal.build(user));
        return UserPrincipal.build(user);
    }
}
