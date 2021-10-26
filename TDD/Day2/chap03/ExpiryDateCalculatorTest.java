package chap03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpiryDateCalculatorTest {

    @Test
    void 구독료_1만원_지불시_종료일자가_한달뒤(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate endDate = cal.calculatePayDay(LocalDate.of(2021, 10, 1), 10000);
        assertEquals(LocalDate.of(2021,11,1),endDate);
    }

    @Test
    void 구독료_1만원_지불시_종료일자가_다른경우(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate endDate = cal.calculatePayDay(LocalDate.of(2021, 1, 31), 10000);
        LocalDate endDate2 = cal.calculatePayDay(LocalDate.of(2021, 3, 31), 10000);
        assertEquals(LocalDate.of(2021,2,28),endDate);
        assertEquals(LocalDate.of(2021,4,30),endDate2);
    }

    @Test
    void 납부_2개월_이상(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate endDate = cal.calculatePayDay(LocalDate.of(2021, 1, 31), 20000);
        assertEquals(LocalDate.of(2021,3,31),endDate);
    }

    @Test
    void 납부_금액_단위_만원_아닐때(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        assertThrows(RuntimeException.class,()->cal.calculatePayDay(LocalDate.of(2021, 1, 31), 15500));
    }

    @Test
    void 납부_금액_십만원(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate endDate = cal.calculatePayDay(LocalDate.of(2021, 1, 31), 100000);
        assertEquals(LocalDate.of(2022,1,31),endDate);
    }

    @Test
    void 납부_금액_십만원_이상(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate endDate = cal.calculatePayDay(LocalDate.of(2021, 1, 31), 150000);
        assertEquals(LocalDate.of(2022,6,30),endDate);
    }

    @Test
    void 납부_금액_십만원_나눠서_낼때(){
        ExpiryDataCalculator cal = new ExpiryDataCalculator();
        LocalDate firstPayDate = cal.calculatePayDay(LocalDate.of(2021, 1, 31), 80000);
        LocalDate endDate = cal.calculatePayDay(firstPayDate,20000);
        assertEquals(LocalDate.of(2021,11,30),endDate);
    }
}
