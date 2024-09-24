package br.com.fiap.theMovie.services;

import br.com.fiap.theMovie.exception.MovieNotFoundException;
import br.com.fiap.theMovie.exception.UserNotFoundException;
import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.models.User;
import br.com.fiap.theMovie.repositories.MovieRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
public class MovieService {

    private final MovieRepository repository;

    @Autowired
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public Movie create(Movie movie){
        movie.setName(movie.getName());
        movie.setDescription(movie.getDescription());
        movie.setActors(movie.getActors());
        movie.setPhoto("https://avatar.iran.liara.run/username?username=" + movie.getName().replace(" ", "-"));
        return repository.save(movie);
    }

    public List<Movie> findAll(){
        return repository.findAll();
    }


    public Movie findMovieById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new MovieNotFoundException(id)
                );
    }

    public Movie findByUserId(Long user_id) {
        return repository.findByUserId(user_id);
    }

    public List<Movie> findMoviesByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public Movie updateMovie(Long id, Movie movie) {
        Movie updatedMovie = repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        if (movie.getName() != null) {
            updatedMovie.setName(movie.getName());
        }
        if (movie.getDescription() != null) {
            updatedMovie.setDescription(movie.getDescription());
        }
        if (movie.getActors() != null) {
            updatedMovie.setActors(movie.getActors());
        }

        return repository.save(updatedMovie);
    }

    public void deleteMovie(Long id) {
        repository.findById(id).orElseThrow(
                () -> new MovieNotFoundException(id)
        );
        repository.deleteById(id);
    }

    public void uploadPhoto(Long id, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        Path destinationPath = Path.of("src/main/resources/static/photos");
        createDirectoryIfNotExists(destinationPath);

        Path destinationFile = destinationPath
                .resolve(System.currentTimeMillis() + "_" + file.getOriginalFilename())
                .normalize()
                .toAbsolutePath();

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, destinationFile);
            String photoUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/movies/photo/")
                    .path(destinationFile.getFileName().toString())
                    .toUriString();

            Movie movie = findMovieById(id);
            movie.setPhoto(photoUrl);
            repository.save(movie);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }

    private void createDirectoryIfNotExists(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for photos", e);
        }
    }

    public ResponseEntity<Resource> getPhoto(String name) {
        Path path = Paths.get("src/main/resources/static/photos/" + name);
        Resource file = UrlResource.from(path.toUri());

        MediaType mediaType = determineMediaType(name);
        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

    private MediaType determineMediaType(String fileName) {
        String fileExtension = getFileExtension(fileName).toLowerCase();
        return switch (fileExtension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "bmp" -> MediaType.valueOf("image/bmp");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? "" : fileName.substring(lastIndexOfDot + 1);
    }

}
