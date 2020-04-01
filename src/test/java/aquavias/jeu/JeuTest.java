package aquavias.jeu;

import static org.junit.Assert.*;

import org.junit.*;

public class JeuTest {

    @Test
    public void initNiveauIsWorkingGood(){
        Jeu testJeu = new Jeu(new Controleur());
        testJeu.initNiveau(1);
        assertEquals(testJeu.getHauteur(), 3);
        assertEquals(testJeu.getLargeur(), 3);
        assertEquals(testJeu.getMode(), "compteur");
        assertEquals(testJeu.getLimite(), 10);
        assertEquals(testJeu.getDebit(), 1, 0);
        assertEquals(testJeu.getCompteur(), 10, 0);
        assert(testJeu.isEntree(0,1));
        assert(testJeu.isSortie(2,1));
        assert(!testJeu.isMovable(0,1));
        assert(!testJeu.isMovable(2,1));
        assert(testJeu.getPont(0, 1).isEntree());
        assert(testJeu.getPont(2, 1).isSortie());
        assert(!testJeu.getPont(0, 1).isMovable());
        assert(!testJeu.getPont(2, 1).isMovable());

        assertNull(testJeu.getPont(0,0));
        assertNull(testJeu.getPont(0,2));
        assertNull(testJeu.getPont(1,0));
        assertNull(testJeu.getPont(1,2));
        assertNull(testJeu.getPont(2,0));
        assertNull(testJeu.getPont(2,2));
        assertEquals(testJeu.getPont(1,1).getForme(), 'I');
        assertEquals(testJeu.getPont(1,1).getOrientation(), 'N');
        assertNull(testJeu.getPont(1,1).spe);
        assertFalse(testJeu.getPont(1,1).eau);
    }
}
