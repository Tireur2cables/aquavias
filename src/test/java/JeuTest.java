import static org.junit.Assert.*;

import org.junit.*;

public class JeuTest {

    @Test
    public void initNiveauIsWorkingGood(){
        Jeu testJeu = new Jeu(new Controleur());
        testJeu.initNiveau(2);
        assertEquals(testJeu.getHauteur(), 3);
        assertEquals(testJeu.getLargeur(), 4);
        assertEquals(testJeu.getMode(), "compteur");
        assertEquals(testJeu.getLimite(), 10);
        assert(testJeu.getDebit() == 1);
        assertEquals(testJeu.getCompteur(), 10);
        assert(testJeu.getPont(0, 1).isEntree());
        assert(testJeu.getPont(3, 1).isSortie());
        assert(!testJeu.getPont(0, 1).isMovable());
        assert(!testJeu.getPont(3, 1).isMovable());
    }
}
