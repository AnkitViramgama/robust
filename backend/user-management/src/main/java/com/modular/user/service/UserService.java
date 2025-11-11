package com.modular.user.service;

import com.modular.core.exception.BadRequestException;
import com.modular.core.exception.ResourceNotFoundException;
import com.modular.core.repository.BaseRepository;
import com.modular.core.service.BaseService;
import com.modular.user.dto.RegisterRequest;
import com.modular.user.dto.UserDto;
import com.modular.user.entity.User;
import com.modular.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User service
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }

    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setActive(true);
        user.setEmailVerified(false);

        User saved = create(user);
        return toDto(saved);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(user);
    }

    public User findUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDto getUserById(String id) {
        User user = findById(id);
        return toDto(user);
    }

    public List<UserDto> getAllUsers() {
        return findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(String id, UserDto userDto) {
        User user = findById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setActive(userDto.getActive());

        User updated = update(id, user);
        return toDto(updated);
    }

    public void assignRoles(String userId, List<String> roleIds) {
        User user = findById(userId);
        user.getRoleIds().clear();
        user.getRoleIds().addAll(roleIds);
        update(userId, user);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
