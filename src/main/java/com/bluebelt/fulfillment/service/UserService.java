package com.bluebelt.fulfillment.service;

import com.bluebelt.fulfillment.model.user.User;
import com.bluebelt.fulfillment.payload.UserIdentityAvailability;
import com.bluebelt.fulfillment.payload.UserProfile;
import com.bluebelt.fulfillment.payload.UserSummary;
import com.bluebelt.fulfillment.payload.request.InfoRequest;
import com.bluebelt.fulfillment.payload.request.SignUpRequest;
import com.bluebelt.fulfillment.payload.response.ApiResponse;
import com.bluebelt.fulfillment.security.UserPrincipal;

public interface UserService {

    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfile getUserProfile(String username);

    User addUser(SignUpRequest signUpRequest);

    User updateUser(User newUser, String username, UserPrincipal currentUser);

    ApiResponse deleteUser(String username, UserPrincipal currentUser);

    ApiResponse giveAdmin(String username);

    ApiResponse removeAdmin(String username);

    UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}
