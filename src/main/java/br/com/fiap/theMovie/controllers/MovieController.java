package br.com.fiap.theMovie.controllers;

import br.com.fiap.theMovie.controllers.dtos.MovieProfileResponse;
import br.com.fiap.theMovie.controllers.dtos.UserProfileResponse;
import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;

    @Autowired
    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<Movie> findAll(@RequestParam(required = false) Long userId){
        return service.findAll();
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

    @GetMapping("/info_movie")
    public MovieProfileResponse getMovieProfile(@PathVariable String name){
        return MovieProfileResponse.fromModel( service.getMovieByName(name) );
    }

    @PostMapping("photo")
    public void uploadPhoto(@RequestBody MultipartFile file){
        var photo = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        service.uploadPhoto(photo, file);
    }

    // /users/avatar/avatar.jpg
    @GetMapping("photo/{filename}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename){
        return service.getPhoto(filename);
    }

    @GetMapping("/{userId}")
    public List<Movie> findByUserId(@RequestParam(required = false) Long userId){
        if (userId != null) return service.findByUserId(userId);
        return service.findByUserId(userId);
    }

    @GetMapping("/{user_id}")
    public MovieProfileResponse getUserById(@PathVariable Long user_id) {
        return MovieProfileResponse.fromModel( service.getByUserId(user_id) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        service.deleteMovie(id);
    }

}
