package mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class GameGenMockTest {

    @Test
    void mockTest(){
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(GameLevel.EASY)).willReturn("123");
        String num = genMock.generate(GameLevel.EASY);

        assertEquals("123",num);
    }
    @Test
    void mockThrowTest(){
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(GameLevel.HARD)).willThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> genMock.generate(GameLevel.HARD));
    }
    @Test
    void init(){
        GameNumGen genMock = mock(GameNumGen.class);
        Game game = new Game(genMock);
        game.init(GameLevel.EASY);

        then(genMock).should().generate(GameLevel.EASY);
    }
}
