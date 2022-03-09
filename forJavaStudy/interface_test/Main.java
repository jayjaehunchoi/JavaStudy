package interface_test;

import interface_test.my_abstract.GameUser;
import interface_test.my_abstract.Gamer;
import interface_test.my_abstract.User;
import interface_test.my_interface.Me;
import interface_test.my_interface.Player;
import interface_test.my_interface.You;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();

        players.add(new Me());
        players.add(new You());

        for (Player player : players) {
            player.hi();
            player.bye();
        }

        List<Gamer> gamers = new ArrayList<>();

        User user = new User();
        user.addCard("1");
        user.addCard("2");
        gamers.add(user);

        GameUser gameUser = new GameUser();
        gameUser.addCard("1");
        gamers.add(gameUser);

        for (Gamer gamer : gamers) {
            System.out.println(gamer.isReceivable());
        }
    }
}
