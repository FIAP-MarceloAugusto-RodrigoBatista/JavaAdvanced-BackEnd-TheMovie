package br.com.fiap.theMovie.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "T_TMOVIE_MOVIE")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String actors;
    private String photo;
    private Long userId;
}
