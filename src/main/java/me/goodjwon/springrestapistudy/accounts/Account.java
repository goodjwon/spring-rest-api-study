package me.goodjwon.springrestapistudy.accounts;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
