package aquavias.generateur;

import aquavias.jeu.Jeu;
import org.junit.Test;

public class PlateauTest{
    @Test
    public void testIfGeneratingMakesFeasibleLevels() {
        try {
            for (int i = 0; i < 100; i++) {
                String mode = "compteur";
                Jeu jeu = new Jeu(16, mode);
                new Plateau(6, 6, true, jeu, mode, false);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
