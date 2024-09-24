package br.com.fiap.theMovie.exception;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException(Long id) {
        super("Movie not found with id: " + id);
    }

    public MovieNotFoundException(String name) {
        super("Movie not found with name: " + name);
    }

}
