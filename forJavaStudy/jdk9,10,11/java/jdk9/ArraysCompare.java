package jdk9;

import java.util.Arrays;

public class ArraysCompare {

    public static void main(String[] args) {
        int[] a = {1,2,3,4};
        int[] b = {1,2,4,5};

        int compare = Arrays.compare(a, b); // a가 앞서면 음수, 같으면 양수, b가 앞서면 양수
        int mismatch = Arrays.mismatch(a, b); // 처음으로 다른 위치 찾기, 같으면 -1 return

        System.out.println("compare = " + compare);
        System.out.println("mismatch = " + mismatch);
    }
}
