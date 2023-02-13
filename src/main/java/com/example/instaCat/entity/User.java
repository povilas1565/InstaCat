package com.example.instaCat.entity;

import com.example.instaCat.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class User implements UserDetails { //Postgres: class Users а также можем использовать аннотацию Table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, updatable = false)
    private String username; //ник, логин

    @Column(length = 3000)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "text")
    private String info;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

//    @OneToOne(optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "passport_id")
//    private Passport passport;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> roles = new HashSet<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities; //Добавим поле коллекции GrantedAuthority — это интерфейс для доступов пользователя.
    // Одна из его имплементаций — SimpleGrantedAuthority в которую можно добавить только имя роли, и Spring будет давать доступ если имя роли совпадает с тем что ему переадают

    public User() {

    }

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    // Security
    public User(Long id, String username, String password, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
