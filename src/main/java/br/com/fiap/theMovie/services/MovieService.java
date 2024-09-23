package br.com.fiap.theMovie.services;

import br.com.fiap.theMovie.controllers.dtos.MovieProfileResponse;
import br.com.fiap.theMovie.models.Movie;
import br.com.fiap.theMovie.repositories.MovieRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository repository;

    @Autowired
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<Movie> findAll(){
        return repository.findAll();
    }

    public Movie create(Movie movie){
        movie.setName(movie.getName());
        movie.setPhoto("https://avatar.iran.liara.run/username?username=" + movie.getName());
        movie.setDescription(movie.getDescription());
        movie.setActors(movie.getActors());
        return repository.save(movie);
    }

    public MovieProfileResponse getMovie(String name) {
        return repository.findByName(name)
                .map(MovieProfileResponse::new)
                .orElseThrow(() -> new UsernameNotFoundException("Movie not found"));
    }

    public void uploadPhoto(String name, MultipartFile file) {

        // verificar se o arquivo existe
        if (file.isEmpty()){
            throw new RuntimeException("File is required");
        }

        // salvar o arquivo no disco
        Path destinationPath = Path.of("src/main/resources/static/photo");
        Path destinationFile = destinationPath
                .resolve(  System.currentTimeMillis() + name + "_" + file.getOriginalFilename())
                .normalize()
                .toAbsolutePath();

        try (InputStream is = file.getInputStream()){
            Files.copy(is, destinationFile);
            System.out.println("Arquivo salvo");

            var movie = repository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Movie not found"));

            var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            var photoUrl =  baseUrl + "/movies/photo/" + destinationFile.getFileName();
            movie.setPhoto(photoUrl);
            repository.save(movie);

        }catch (Exception e){
            System.out.println("Erro ao salvar. " + e.getCause());
        }



    }

    public ResponseEntity<Resource> getPhoto(String name) {
        Path path = Paths.get("src/main/resources/static/photos/" + name);
        Resource file = UrlResource.from(path.toUri());

        // Verificar a extensão do arquivo para definir o tipo de mídia
        String fileExtension = getFileExtension(name);
        MediaType mediaType;

        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "bmp":
                mediaType = MediaType.valueOf("image/bmp");
                break;
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(file);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? "" : fileName.substring(lastIndexOfDot + 1);
    }

    public List<Movie> findByUserId(Long user_id) {
        return repository.findByUserId(user_id);
    }

}
