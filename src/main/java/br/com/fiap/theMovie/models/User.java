package br.com.fiap.theMovie.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "T_TMOVIE_USER")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
}
