package com.bluebelt.fulfillment.service.impl;

import com.bluebelt.fulfillment.exception.*;
import com.bluebelt.fulfillment.model.role.Role;
import com.bluebelt.fulfillment.model.role.RoleName;
import com.bluebelt.fulfillment.model.user.Info;
import com.bluebelt.fulfillment.model.user.User;
import com.bluebelt.fulfillment.payload.UserIdentityAvailability;
import com.bluebelt.fulfillment.payload.UserProfile;
import com.bluebelt.fulfillment.payload.UserSummary;
import com.bluebelt.fulfillment.payload.request.InfoRequest;
import com.bluebelt.fulfillment.payload.request.SignUpRequest;
import com.bluebelt.fulfillment.payload.response.ApiResponse;
import com.bluebelt.fulfillment.repository.RoleRepository;
import com.bluebelt.fulfillment.repository.UserRepository;
import com.bluebelt.fulfillment.security.UserPrincipal;
import com.bluebelt.fulfillment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.bluebelt.fulfillment.utils.AppConstants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDAO;

    @Autowired
    private RoleRepository roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // password

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary().builder().id(currentUser.getId()).username(currentUser.getUsername()).build();
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
                .email(user.getEmail()).info(user.getInfo()).joinedAt(user.getCreatedAt()).build();
    }

    @Override
    public User addUser(SignUpRequest signUpRequest) {
        if(userDAO.existsByUsername(signUpRequest.getUsername())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, USERNAME_IS_ALREADY);
            throw new BadRequestException(apiResponse);
        }

        if(userDAO.existsByEmail(signUpRequest.getEmail())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, EMAIL_IS_ALREADY);
            throw new BadRequestException(apiResponse);
        }

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role userRole = roleDAO.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleDAO.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));

                        roles.add(adminRole);

                        break;

                    case "moderator":
                        Role moderatorRole = roleDAO.findByName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));

                        roles.add(moderatorRole);

                        break;

                    default:
                        Role userRole = roleDAO.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));

                        roles.add(userRole);

                        break;
                }
            });
        }

        User user = new User().builder().username(signUpRequest.getUsername()).email(signUpRequest.getEmail())
                .roles(roles).password(passwordEncoder.encode((signUpRequest.getPassword()))).build();

        return userDAO.save(user);
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {

        User user = userDAO.getUserByName(username);
        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setInfo(newUser.getInfo());

            return userDAO.save(user);

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
        throw new UnauthorizedException(apiResponse);


    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + username);
            throw new AccessDeniedException(apiResponse);
        }

        userDAO.deleteById(user.getId());

        return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);

    }

    @Override
    public ApiResponse giveAdmin(String username) {

        User user = userDAO.getUserByName(username);
        Set<Role> roles = new HashSet<>();
        roles.add(roleDAO.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("User role not set")));
        roles.add(
                roleDAO.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userDAO.save(user);
        return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);

    }

    @Override
    public ApiResponse removeAdmin(String username) {

        User user = userDAO.getUserByName(username);
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleDAO.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userDAO.save(user);
        return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);

    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {

        User user = userDAO.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(USER, USERNAME, currentUser.getUsername()));

        String firstName = infoRequest.getFirstName();
        String lastName = infoRequest.getLastName();
        String phone = infoRequest.getPhone();

        Info info = new Info().builder().firstName(firstName).lastName(lastName)
                .phone(phone).build();

        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            user.setInfo(info);
            User updatedUser = userDAO.save(user);

            return new UserProfile().builder()
                    .id(updatedUser.getId()).username(updatedUser.getUsername()).info(updatedUser.getInfo())
                    .email(updatedUser.getEmail()).joinedAt(updatedUser.getCreatedAt())
                    .build();
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile", HttpStatus.FORBIDDEN);
        throw new AccessDeniedException(apiResponse);

    }
}
