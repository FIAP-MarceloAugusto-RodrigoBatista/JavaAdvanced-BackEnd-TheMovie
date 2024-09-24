package br.com.fiap.theMovie.controllers.dtos;

import br.com.fiap.theMovie.models.User;

public record UserProfileResponse(
        String name,
        String surname,
        String email
        ) {

    public static UserProfileResponse fromModel(User user){
        return new UserProfileResponse(user.getName(), user.getSurname(), user.getEmail());
    }

}
