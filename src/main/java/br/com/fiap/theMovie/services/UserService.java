package br.com.fiap.theMovie.services;

import br.com.fiap.theMovie.controllers.dtos.UserProfileResponse;
import br.com.fiap.theMovie.models.User;
import br.com.fiap.theMovie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll(){
        return repository.findAll();
    }

    public User create(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setName(user.getName());
        user.setSurname(user.getSurname());
        return repository.save(user);
    }

    public UserProfileResponse getUserProfile(String email) {
        return repository.findByEmail(email)
                .map(UserProfileResponse::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}
