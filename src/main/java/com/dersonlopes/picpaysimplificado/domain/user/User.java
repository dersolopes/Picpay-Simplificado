package com.dersonlopes.picpaysimplificado.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String document;
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private BigDecimal balance; // Saldo da carteira

    @Enumerated(EnumType.STRING)
    private UserType userType;

}
