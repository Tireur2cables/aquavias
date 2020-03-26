package aquavias.generateur;

import aquavias.jeu.Pont;
import aquavias.jeu.PontI;
import aquavias.jeu.PontL;
import aquavias.jeu.PontT;

import java.util.concurrent.ThreadLocalRandom;

class Plateau {

    private Pont[][] plateau;

    Plateau(int largeur, int hauteur) {
        this.plateau = new Pont[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                this.plateau[i][j] = this.createPont();
            }
        }
    }

    private Pont createPont() {
        char forme = this.chooseOrientation();
        switch(forme) {
            case 'I' :
                return new PontI();
            case 'L' :
                return new PontL();
            case 'T' :
                return new PontT();
        }
        throw new RuntimeException("char du pont inconnu");
    }

    private char chooseOrientation() {
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

}
