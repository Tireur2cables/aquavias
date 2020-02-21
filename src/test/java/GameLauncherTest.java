import static org.junit.Assert.*;

import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class GameLauncherTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @Test
    public void mainShouldDislayLaunchMessage() {
        GameLauncher.main(new String[]{"test"});
        assertEquals("- - - \nI I I \n- - - \nLe jeu se lance!\nTest de l'affichage d'un pont\n", outContent.toString());
    }
}
