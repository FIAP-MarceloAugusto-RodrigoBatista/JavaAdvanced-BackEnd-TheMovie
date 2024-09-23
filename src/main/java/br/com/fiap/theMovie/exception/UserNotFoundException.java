package br.com.fiap.theMovie.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("Comment not found with id: " + id);
    }

    public UserNotFoundException(String name) {
        super("Comment not found with id: " + name);
    }

}
