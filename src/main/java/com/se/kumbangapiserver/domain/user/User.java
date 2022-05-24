package com.se.kumbangapiserver.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.kumbangapiserver.domain.board.WishList;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String address;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<WishList> wishLists;

    public static User fromDTO(UserDTO user) {
        return User.builder()
                .id(Long.valueOf(user.getId()))
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .removedAt(user.getRemovedAt())
                .roles(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

//    @Column
//    @Enumerated(EnumType.STRING)
//    private UserRole role = UserRole.ROLE_NOT_PERMITTED;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Long getId() {
        return id;
    }

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public void setNewInfo(UserDTO user) {
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return String.valueOf(this.id);
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }


    public UserDTO toDTO() {

        return UserDTO.builder()
                .id(String.valueOf(this.id))
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .removedAt(this.removedAt)
                .role(this.roles)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .removedAt(this.removedAt)
                .build();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setDeletedAt(LocalDateTime now) {
        this.removedAt = now;
    }

    public boolean getRemovedAt() {
        return this.removedAt != null;
    }
}
