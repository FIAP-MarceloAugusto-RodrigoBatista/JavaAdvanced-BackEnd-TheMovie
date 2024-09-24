package br.com.fiap.theMovie.controllers.dtos;

import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.models.User;

public record MovieProfileResponse(
        String name,
        String description,
        String actors,
        String photo,
        Long userId
) {
    public MovieProfileResponse(Movie movie){
        this(movie.getName(), movie.getDescription(), movie.getActors(), movie.getPhoto(), movie.getUserId());
    }
}
