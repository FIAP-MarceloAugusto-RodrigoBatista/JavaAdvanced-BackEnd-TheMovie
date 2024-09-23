package br.com.fiap.theMovie.exception;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException(Long id) {
        super("Comment not found with id: " + id);
    }

}
