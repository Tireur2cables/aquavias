package aquavias.generateur;

import aquavias.jeu.Jeu;
import org.junit.Test;

public class PlateauTest{
    @Test
    public void testIfGeneratingMakesFeasibleLevels() {
        try {
            for (int i = 0; i < 100; i++) {
                String mode = "compteur";
                int limite = 100;
                Jeu jeu = new Jeu(16, mode, limite);
                Plateau p = new Plateau(6, 6, false, jeu);
                jeu.setPlateau(p.getPlateau());
                jeu.calculVictoire();
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
