package br.com.fiap.theMovie.services;

import br.com.fiap.theMovie.controllers.dtos.UserProfileResponse;
import br.com.fiap.theMovie.exception.UserNotFoundException;
import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.models.User;
import br.com.fiap.theMovie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user){
        user.setName(user.getName());
        user.setSurname(user.getSurname());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                    () -> new UserNotFoundException(id)
                );
    }

    public User updateUser(Long id, User user){
        User updatedUser = repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );

        // Atualiza os dados primários do filme
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getSurname() != null) {
            updatedUser.setSurname(user.getSurname());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repository.save(updatedUser);
    }

    public void deleteUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else{
            throw new UserNotFoundException(id);
        }
    }

}
