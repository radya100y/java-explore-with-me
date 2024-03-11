package ru.practicum.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 256)
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Size(max = 512)
    @Column(nullable = false)
    private String email;

    @Column(name = "rating")
    private Long rating;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.rating = 0L;
    }
}
