package me.goodjwon.springrestapistudy.accounts;

import junit.framework.TestCase;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.regex.Matcher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException exceptedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName() {
        //Given
        String userName = "goodjwon";
        String password = "password1!";
        String email = "goodjwon@gmail.com";

        Account account = Account.builder()
                .userName(userName)
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountRepository.save(account);

        //When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        //Then
        assertThat(userDetails.getPassword()).isEqualTo(password);

    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUserName_Fail_TypeA(){
        String userNmae = "asdfaasdf";
        accountService.loadUserByUsername(userNmae);
    }

    @Test
    public void findByUserName_Fail_TypeB(){
        String userNmae = "asdfaasdf";
        try {
            accountService.loadUserByUsername(userNmae);
            fail("supposed to be failed");  // 실패.
        } catch (UsernameNotFoundException e){
            assertThat(e.getMessage()).containsSequence(userNmae);
        }
    }

    @Test
    public void findByUserName_Fail_TypeC(){
        // Expected (먼저 예측을 해야 한다.)
        String userNmae = "asdfaasdf";
        exceptedException.expect(UsernameNotFoundException.class);
        exceptedException.expectMessage(Matchers.containsString(userNmae));

        // When
        accountService.loadUserByUsername(userNmae);
    }
}