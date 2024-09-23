package br.com.fiap.theMovie.controllers.dtos;

import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.models.User;

public record MovieProfileResponse(
        String photo,
        String name,
        String description,
        String actors,
        Long userId
) {

    public MovieProfileResponse(Movie movie){
        this(movie.getPhoto(), movie.getName(), movie.getDescription(), movie.getActors(), movie.getId() );
    }

}
