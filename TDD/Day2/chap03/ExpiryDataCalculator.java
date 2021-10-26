package chap03;

import java.time.LocalDate;

public class ExpiryDataCalculator {

    public LocalDate calculatePayDay(LocalDate payDay, int price){
        validatePaidPrice(price);
        long addMonth = calculateAddMonth(price);
        LocalDate endDate = payDay.plusMonths(addMonth);
        return endDate;
    }

    private void validatePaidPrice(int price){
        if(price % 10000 != 0){
            throw new RuntimeException("만원 단위로 지불해주세요");
        }
    }

    private long calculateAddMonth(int price){
        long addMonth = price/10000;
        if(addMonth >= 10){
            return addMonth+2;
        }
        return addMonth;
    }

}
