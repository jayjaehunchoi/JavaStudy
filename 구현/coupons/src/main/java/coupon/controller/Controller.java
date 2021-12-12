package coupon.controller;

import coupon.View;
import coupon.domain.User;

public class Controller {

    public void run(){
        String username = View.inputUserName();
        User user = new User(username);
        MainFeature selection = null;

        while (selection != MainFeature.QUIT) {
            try {
                String input = View.inputMenu();
                selection = MainFeature.getMenu(input);

                selection.accept(user);
            }catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
