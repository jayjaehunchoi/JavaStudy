package string_performance;

public class StringBuilderTest {

    private final static String ADDER = "abc";

    public void stringOperation() {
        String a = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            a += ADDER;
        }
        long end = System.currentTimeMillis();
        System.out.println("string operation : " + (end - start) + " ms");
    }

    public void stringBuilderAppend() {
        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            sb.append(ADDER);
        }
        long end = System.currentTimeMillis();
        System.out.println("stringBuilder append : " + (end - start) + " ms");
    }

    public static void main(String[] args) {
        StringBuilderTest stringBuilderTest = new StringBuilderTest();
        stringBuilderTest.stringBuilderAppend();
        stringBuilderTest.stringOperation();

    }


}
