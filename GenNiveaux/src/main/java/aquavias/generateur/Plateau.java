package aquavias.generateur;

import aquavias.jeu.Pont;
import aquavias.jeu.PontI;
import aquavias.jeu.PontL;
import aquavias.jeu.PontT;

import java.util.concurrent.ThreadLocalRandom;

class Plateau {

    private Pont[][] plateau;

    /**
     * INIT PART
     * */

    Plateau(int largeur, int hauteur) {
        this.plateau = new Pont[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                this.plateau[i][j] = null;
            }
        }
        this.placerEntreeSortie();
    }

    private Pont createPont(String spe) {
        char forme = this.chooseForme();
        char orientation = this.chooseOrientation();
        switch(forme) {
            case 'I' :
                return new PontI(orientation, spe);
            case 'L' :
                return new PontL(orientation, spe);
            case 'T' :
                return new PontT(orientation, spe);
        }
        throw new RuntimeException("char du pont inconnu");
    }

    private char chooseForme() {
        int random = ThreadLocalRandom.current().nextInt(0, 3);
        switch (random) {
            case 0 :
                return 'I';
            case 1 :
                return 'L';
            case 2 :
                return 'T';
        }
        throw new RuntimeException("Random int out of bounds");
    }

    private char chooseOrientation() {
        int random = ThreadLocalRandom.current().nextInt(0, 4);
        switch (random) {
            case 0 :
                return 'N';
            case 1 :
                return 'E';
            case 2 :
                return 'S';
            case 3 :
                return 'O';
        }
        throw new RuntimeException("Random int out of bounds");
    }
    
    private void placerEntreeSortie() {
        int i = 0;
        int j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
        this.plateau[i][j] = this.createPont("entree");
        this.makePontAccessibleFrom(i, j, 'E');

        i = this.getLargeur()-1;
        j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
        this.plateau[i][j] = this.createPont("sortie");
        this.makePontAccessibleFrom(i, j, 'O');
    }

    /**
     * VERIF PART
     */

    private void makePontAccessibleFrom(int i, int j, char sortie) {
        Pont p = this.plateau[i][j];
        while (!p.isAccessibleFrom(sortie)) {
            char nextOrientation = Pont.getNextOrientation(p.getOrientation());
            p.setOrientation(nextOrientation);
        }
    }

    /**
     * GETTEUR PART
     */

    Pont[][] getPlateau() {
        return this.plateau;
    }

    private int getLargeur() {
        return this.plateau.length;
    }

    private int getHauteur() {
        return this.plateau[0].length;
    }

}
