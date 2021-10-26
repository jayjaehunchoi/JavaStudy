package chap02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordStrengthMeterTest {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();

    @Test
    void meetsAllCriteria_STRONG(){
        PasswordStrength result = meter.meter("ab123!@Z");
        Assertions.assertEquals(PasswordStrength.STRONG, result);
    }

    // 길이 8이상, 0~9 사이 숫자 포함
    @Test
    void meetsFirstAndSecondCondition_NORMAL(){
        PasswordStrength result = meter.meter("ab123456789");
        Assertions.assertEquals(PasswordStrength.NORMAL, result);
    }

    // 길이 8이상, 대문자 포함
    @Test
    void meetsFirstAndThirdCondition_NORMAL(){
        PasswordStrength result = meter.meter("abcsdfaAA");
        Assertions.assertEquals(PasswordStrength.NORMAL, result);
    }

    // 0~9 사이 숫자, 대문자 포함
    @Test
    void meetsSecondAndThirdCondition_NORMAL(){
        PasswordStrength result = meter.meter("1234AV");
        Assertions.assertEquals(PasswordStrength.NORMAL, result);
    }

    @Test
    void inputPasswordIsNull(){
        PasswordStrength result = meter.meter(null);
        Assertions.assertEquals(PasswordStrength.INVALID, result);
    }

    @Test
    void inputPasswordIsEmpty(){
        PasswordStrength result = meter.meter("");
        Assertions.assertEquals(PasswordStrength.INVALID, result);
    }

    // 8자 이상
    @Test
    void meetsFirstCondition_WEAK(){
        PasswordStrength result = meter.meter("aaaaaaaaaaa");
        Assertions.assertEquals(PasswordStrength.WEAK, result);
    }

    // 0~9사이 숫자 포함
    @Test
    void meetsSecondCondition_WEAK(){
        PasswordStrength result = meter.meter("1");
        Assertions.assertEquals(PasswordStrength.WEAK, result);
    }

    // 대문자 포함
    @Test
    void meetsThirdCondition_WEAK(){
        PasswordStrength result = meter.meter("A");
        Assertions.assertEquals(PasswordStrength.WEAK, result);
    }
}
