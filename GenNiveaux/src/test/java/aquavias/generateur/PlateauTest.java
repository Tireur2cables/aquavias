package aquavias.generateur;

import aquavias.jeu.Jeu;
import org.junit.Test;

public class PlateauTest{
    @Test
    public void testIfGeneratingMakesFeasibleLevels() {
        try {
            for (int i = 0; i < 100; i++) {
                String mode = "compteur"; //arbitraire
                int numNiveau = 16; //arbitraire
                new Plateau(6, 6, true, numNiveau, mode, false);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
