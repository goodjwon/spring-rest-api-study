package me.goodjwon.springrestapistudy.accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccountRepository extends CrudRepository <Account, Long> {
    Optional<Account>  findByUserName(String userName);
}
