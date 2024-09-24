package br.com.fiap.theMovie.controllers;

import br.com.fiap.theMovie.controllers.dtos.MovieProfileResponse;
import br.com.fiap.theMovie.controllers.dtos.UserProfileResponse;
import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.repositories.MovieRepository;
import br.com.fiap.theMovie.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService service, MovieRepository movieRepository) {
        this.service = service;
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie, UriComponentsBuilder uriBuilder){
        service.create(movie);

        var uri = uriBuilder
                .path("/movies/{id}")
                .buildAndExpand(movie.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(movie);
    }

    // Listagem de Filmes
    @GetMapping
    public ResponseEntity<List<Movie>> findAll(){
        List<Movie> movies = service.findAll();
        return ResponseEntity.ok(movies);
    }

    // Busca do Filme pelo Id - Get Movie By Id
    @GetMapping("/{id}")
    public ResponseEntity<Movie> findMovieById(@PathVariable Long id){
        Movie movie = service.findMovieById(id);
        return ResponseEntity.ok(movie);
    }

    // Pesquisa de Filme pelo ID do Usuário
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Movie> findMovieByUserId(@PathVariable Long user_id) {
        Movie movie = service.findByUserId(user_id);
        return ResponseEntity.ok(movie);
    }

    // Pesquisa de Filme pelo Nome
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> findMoviesByNameContaining(@RequestParam String name) {
        List<Movie> movies = service.findMoviesByNameContaining(name);
        return ResponseEntity.ok(movies);
    }

    @PutMapping("/{id}/photo")
    public ResponseEntity<Void> uploadPhoto(@PathVariable Long id,
                                            @RequestPart MultipartFile file) {
        service.uploadPhoto(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id,
                                             @RequestBody Movie movie) {
        Movie updatedMovie = service.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
    }

    /*
        @PutMapping("/photo")
        public void uploadPhoto(@PathVariable Long id, @RequestBody MultipartFile file){
            var photo = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            service.uploadPhoto(id, photo, file);
        }

    */

    // /movies/photo/filme.jpg
    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename){
        return service.getPhoto(filename);
    }
}
