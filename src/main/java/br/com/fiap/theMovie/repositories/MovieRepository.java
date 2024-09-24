package br.com.fiap.theMovie.repositories;

import br.com.fiap.theMovie.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByNameContainingIgnoreCase(String name);

    Movie findByUserId(Long user_id);
}
