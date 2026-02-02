package airbnb.example.airbnb.service;

import airbnb.example.airbnb.dto.ProfileUpdateRequestDto;
import airbnb.example.airbnb.dto.UserDto;
import airbnb.example.airbnb.entity.User;

public interface UserService {
    User getUserById(Long userId);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
