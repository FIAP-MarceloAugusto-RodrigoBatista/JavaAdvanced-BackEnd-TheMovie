package br.com.fiap.theMovie.controllers.dtos;

import br.com.fiap.theMovie.models.User;

public record UserProfileResponse(
        String email,
        String name,
        String surname
) {

    public UserProfileResponse(User user){
        this(user.getEmail(), user.getName(), user.getSurname());
    }

}
