package enum_posting;

import java.util.function.Consumer;

public class LegacyEnumeration {

    public static int legacySelection(String input) {
        if ("Yes".equals(input)) {
            return 1;
        } else if ("No".equals(input)) {
            return 0;
        }
        return -1;
    }

    public static void main(String[] args) {
        String userInput = "Yes";
        int selection = legacySelection(userInput);
        if (selection == 1) {
            System.out.println("네!");
        } else if (selection == 0) {
            System.out.println("아니오!");
        } else {
            System.out.println("다시 선택해주세요!");
        }
    }

    enum Selection {
        YES(1){
            @Override
            public void accept(String name) {
                System.out.println(name + "YES");
            }
        },
        NO(0){
            @Override
            public void accept(String name) {
                System.out.println(name + "NO");
            }
        },
        ELSE(-1){
            @Override
            public void accept(String name) {
                System.out.println(name + "Select Please");
            }
        };

        private final int selectionNumber;

        Selection(final int selectionNumber) {
            this.selectionNumber = selectionNumber;
        }

        public int getSelectionNumber() {
            return selectionNumber;
        }

        public abstract void accept(String name);

    }

}
