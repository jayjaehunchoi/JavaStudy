package mockito;

public class Game {

    private GameLevel gameLevel;
    private GameNumGen gameNumGen;

    public Game(GameNumGen gameNumGen){
        this.gameNumGen = gameNumGen;
    }

    public void init(GameLevel level){
        gameNumGen.generate(level);
    }
}
