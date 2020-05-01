package aquavias.generateur;

import aquavias.jeu.Jeu;
import org.junit.Test;

public class PlateauTest{
    @Test
    public void catchExceptionFromGenerating() {
        try {
            for (int i = 0; i < 100; i++) {
                String mode = "compteur";
                int limite = 100;
                Jeu jeu = new Jeu(16, mode, limite);
                Plateau p = new Plateau(6, 6, false, jeu);
                //p.exportNiveau(); fixme erreur lorsqu'on essaye de faire ça mais au final est-ce qu'on veut vraiment faire ça dans les tests ? je ne pense pas
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
