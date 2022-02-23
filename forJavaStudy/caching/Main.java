package caching;

public class Main {

    public static void main(String[] args) {
        getLottoNumberByConstructor();
        //getLottoNumberByStaticFactoryMethod();
    }

    private static void getLottoNumberByStaticFactoryMethod() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            for (int j = 1; j < 46; j++) {
                LottoNumber.valueOf(j);
            }
        }
        long end = System.currentTimeMillis();
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("정적 팩토리 메소드 = "+ usedMemory + " bytes");
        System.out.println("정적 팩토리 메소드 = "+ (end - start) + " ms");
    }

    private static void getLottoNumberByConstructor() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            for (int j = 1; j < 46; j++) {
                new LottoNumber2(j);
            }
        }
        long end = System.currentTimeMillis();
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("생성자 = "+usedMemory + " bytes");
        System.out.println("생성자 = "+ (end - start) + " ms");
    }

}
