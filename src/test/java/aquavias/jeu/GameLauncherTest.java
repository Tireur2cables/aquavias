package aquavias.jeu;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/* Import with maven dependencies */
import org.junit.*;

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
        //assertEquals("Le jeu se lance!\n", outContent.toString()); test plus nécéssaire car le jeu n'affiche plus rien sur le terminal
    }

}
