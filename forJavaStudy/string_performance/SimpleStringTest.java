package string_performance;

public class SimpleStringTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String a = "ab";
        a += "c";
        long end = System.currentTimeMillis();
        System.out.println("string operation : " + (end - start) + " ms");


        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("ab").append("c");
        end = System.currentTimeMillis();
        System.out.println("stringBuilder append : " + (end - start) + " ms");
    }
}
