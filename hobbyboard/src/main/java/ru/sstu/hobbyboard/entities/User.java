package ru.sstu.hobbyboard.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sstu.hobbyboard.entities.dto.UserDTO;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 30)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(unique = true)
    private String nickName;

    @NotNull
    private String avatar;

    @NotNull
    @Size(min = 11, max = 12) //TODO: regex?
    private String phone;

    private Boolean sex; //false = male, true = female

    private Date birth = Date.valueOf(LocalDate.now());

    @NotNull
    @Size(min = 1, max = 30)
    @Column(unique = true) //TODO: regex?
    private String email;

    private Date activity = Date.valueOf(LocalDate.now());
    ;

    @NotNull
    @Size(min = 4, max = 30)
    private String password;

    @Min(0)
    @Max(100)
    private Byte cashBack = 1;

    @Min(0)
    private Float bonus = 0F;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Purchase> purchases;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> userRoles;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Basket> baskets = new HashSet<>();

    public User(UserDTO userDto) {
        firstName = userDto.getFirstName();
        lastName = userDto.getLastName();
        nickName = userDto.getNickName();
        email = userDto.getEmail();
        password = userDto.getPassword();
        birth = userDto.getBirth();
        phone = userDto.getPhone();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public String getUsername() {
        return email;
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
