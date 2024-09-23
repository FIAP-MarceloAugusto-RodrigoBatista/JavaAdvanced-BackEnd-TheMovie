package br.com.fiap.theMovie.controllers.dtos;

import br.com.fiap.theMovie.models.User;

public record UserProfileResponse(
        String email,
        String name,
        String surname
) {

    public static UserProfileResponse fromModel(User user){
        return new UserProfileResponse(user.getEmail(), user.getName(), user.getSurname());
    }

}
