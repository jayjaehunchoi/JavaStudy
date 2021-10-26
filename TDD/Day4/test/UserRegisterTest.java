package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegisterTest {

    private UserRegister userRegister;
    private StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();
    private MemoryUserRepository fakeUserRepository = new MemoryUserRepository();
    private SpyEmailNotifier spyEmailNotifier = new SpyEmailNotifier();

    @BeforeEach
    void setUp(){
        userRegister = new UserRegister(stubWeakPasswordChecker, fakeUserRepository, spyEmailNotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword(){
        stubWeakPasswordChecker.setWeak(true);
        assertThrows(WeakPasswordException.class, ()->userRegister.register("id","pw","email"));
    }

    @DisplayName("중복회원 저장 실패")
    @Test
    void dupIdExists(){
        fakeUserRepository.save(new User("id","pw1","email@email.com"));
        assertThrows(DupIdException.class, ()->userRegister.register("id","pw","email"));
    }

    @DisplayName("저장 성공")
    @Test
    void dupIdNotExists_Success(){
        userRegister.register("id","pw","email");
        User findUser = fakeUserRepository.findById("id");

        assertEquals("id",findUser.getId());
    }

    @DisplayName("가입하면 메일 전송")
    @Test
    void whenRegisterSendEmail(){
        userRegister.register("id","pw","email@email.com");

        assertTrue(spyEmailNotifier.isCalled());
        assertEquals("email@email.com",spyEmailNotifier.getEmail());
    }
}
