package br.com.fiap.theMovie.services;

import br.com.fiap.theMovie.exception.MovieNotFoundException;
import br.com.fiap.theMovie.models.Movie;
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

    public Movie updateMovie(Long id, Movie movie, MultipartFile file) {
        Movie updatedMovie = repository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        // Atualiza os dados primários do filme
        if (movie.getName() != null) {
            updatedMovie.setName(movie.getName());
        }
        if (movie.getDescription() != null) {
            updatedMovie.setDescription(movie.getDescription());
        }
        if (movie.getActors() != null) {
            updatedMovie.setActors(movie.getActors());
        }

        // Verifica se há um novo arquivo de foto foi enviado
        if (file != null && !file.isEmpty()) {
            uploadPhoto(id, updatedMovie.getName(), file);
        }

        return repository.save(updatedMovie);
    }

    public void deleteMovie(Long id) {
        repository.findById(id).orElseThrow(
                () -> new MovieNotFoundException(id)
        );
        repository.deleteById(id);
    }

    public void uploadPhoto(Long id, String name, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }

        System.out.println("Uploading file: " + file.getOriginalFilename());

        Path destinationPath = Path.of("src/main/resources/static/photos");

        // Certifique-se de que o diretório existe
        try {
            if (!Files.exists(destinationPath)) {
                Files.createDirectories(destinationPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create directory for photos", e);
        }

        Path destinationFile = destinationPath
                .resolve(System.currentTimeMillis() + "_" + file.getOriginalFilename())
                .normalize()
                .toAbsolutePath();

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, destinationFile);
            System.out.println("Arquivo salvo em: " + destinationFile);

            var movie = repository.findById(id)
                    .orElseThrow(() -> new MovieNotFoundException(id));

            var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            var photoUrl = baseUrl + "/movies/photo/" + destinationFile.getFileName().toString();
            System.out.println("Photo URL: " + photoUrl);

            movie.setPhoto(photoUrl);
            repository.save(movie);
        } catch (IOException e) {
            e.printStackTrace(); // Mostra a stack trace
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<Resource> getPhoto(String name) {
        Path path = Paths.get("src/main/resources/static/photos/" + name);
        Resource file = UrlResource.from(path.toUri());

        // Verificar a extensão do arquivo para definir o tipo de mídia
        String fileExtension = getFileExtension(name);
        MediaType mediaType = switch (fileExtension.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "bmp" -> MediaType.valueOf("image/bmp");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(file);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? "" : fileName.substring(lastIndexOfDot + 1);
    }

}
