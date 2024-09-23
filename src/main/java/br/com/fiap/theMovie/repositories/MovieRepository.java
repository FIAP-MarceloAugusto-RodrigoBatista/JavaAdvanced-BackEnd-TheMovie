package br.com.fiap.theMovie.repositories;

import br.com.fiap.theMovie.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByName(String name);

    List<Movie> findByUserId(Long user_id);

//    List<Movie> findByMovieContainingIgnoreCase(String movie);
}
