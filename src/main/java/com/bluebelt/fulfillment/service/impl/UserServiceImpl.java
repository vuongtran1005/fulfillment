package com.bluebelt.fulfillment.service.impl;

import com.bluebelt.fulfillment.exception.BadRequestException;
import com.bluebelt.fulfillment.model.role.Role;
import com.bluebelt.fulfillment.model.role.RoleName;
import com.bluebelt.fulfillment.model.user.User;
import com.bluebelt.fulfillment.payload.UserIdentityAvailability;
import com.bluebelt.fulfillment.payload.UserProfile;
import com.bluebelt.fulfillment.payload.UserSummary;
import com.bluebelt.fulfillment.payload.request.SignupRequest;
import com.bluebelt.fulfillment.payload.response.ApiResponse;
import com.bluebelt.fulfillment.repository.RoleRepository;
import com.bluebelt.fulfillment.repository.UserRepository;
import com.bluebelt.fulfillment.security.UserPrincipal;
import com.bluebelt.fulfillment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userDAO;
    private final RoleRepository roleDAO;

    // password

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary().builder().id(currentUser.getId()).username(currentUser.getUsername())
                .firstName(currentUser.getFirstName()).lastName(currentUser.getLastName()).build();
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userDAO.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userDAO.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userDAO.getUserByName(username);
        return new UserProfile().builder().id(user.getId()).username(user.getUsername())
                .firstName(user.getFirstName()).lastName(user.getLastName())
                .joinedAt(user.getCreatedAt()).email(user.getEmail())
                .phone(user.getPhone()).build();
    }

    @Override
    public User addUser(SignupRequest signupRequest) {
        if(userDAO.existsByUsername(signupRequest.getUsername())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
            throw new BadRequestException(apiResponse);
        }

        if(userDAO.existsByEmail(signupRequest.getEmail())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
            throw new BadRequestException(apiResponse);
        }

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role userRole = roleDAO.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleDAO.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(adminRole);

                        break;

                    case "moderator":
                        Role moderatorRole = roleDAO.findByName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(moderatorRole);

                        break;

                    default:
                        Role userRole = roleDAO.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(userRole);

                        break;
                }
            });
        }

        User user = new User().builder().username(signupRequest.getUsername()).email(signupRequest.getEmail())
                .firstName(signupRequest.getFirstName()).lastName(signupRequest.getLastName())
                .phone(signupRequest.getPhone()).roles(roles)
                .password(p).build();



        return null;
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        return null;
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        return null;
    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser) {
        return null;
    }
}
