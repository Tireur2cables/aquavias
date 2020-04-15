package aquavias.generateur;

import aquavias.jeu.Pont;
import aquavias.jeu.PontI;
import aquavias.jeu.PontL;
import aquavias.jeu.PontT;

import java.util.concurrent.ThreadLocalRandom;

class Plateau {

    private Pont[][] plateau;
    private int xEntree;
    private int yEntree;
    private int xSortie;
    private int ySortie;

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
        this.getCheminPont();
    }

    private void getCheminPont(){
        Pont entree = this.getEntree();
        Pont sortie = this.getSortie();
        //On considére point le moment uniquement l'entrée et la sortie aux points xEntree, yEntree, xSortie, ySortie
        //On génère un nombre aléatoire pour prendre le y du pont en coordonné xEntree +1
        //On complete le chemin entre le pont d'entree et le nouveau pont

        /**
         * TODO :
         *  -Il faut considérer les différents points d'entrées (forme du pont d'entrée)
         *  -Il faut considérer la liaisons avec les ponts créés pendant la compléttion du chemin
         *
         * */

        //int newY = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
        int newY = 0;
        System.out.println("xEntree, yEntree : " + xEntree + " " + yEntree);
        System.out.println("Nouveau y : " + newY);
        plateau[xEntree+1][newY] = createPont(null);
        completeChemin(xEntree, yEntree, newY);
        /*newY = ThreadLocalRandom.current().nextInt(0, this.getHauteur());*/
        newY = 3;
        System.out.println("xEntree, yEntree : " + xEntree + " " + yEntree);
        System.out.println("Nouveau y : " + newY);
        plateau[xEntree+2][newY] = createPont(null);
        completeChemin(xEntree+1,0, newY);

    }

    private void completeChemin(int x, int y, int newY){
        int i = y;
        System.out.println("newY : " + newY);
        while(i != newY){
            System.out.println("Completion du chemin x - y : " + x + " - " + i);
            if ((i > newY)) i--;
            else i++;
            //WIP : il faut traiter les ponts !
            plateau[x][i] = createPont(null);
        }
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
        /*int j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());*/
        int j = 2;
        this.plateau[i][j] = this.createPont("entree");
        this.makePontAccessibleFrom(i, j, 'E');
        this.xEntree = i;
        this.yEntree = j;

        i = this.getLargeur()-1;
        /*j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());*/
        j = 1;
        this.plateau[i][j] = this.createPont("sortie");
        this.makePontAccessibleFrom(i, j, 'O');
        this.xSortie = i;
        this.ySortie = j;
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

    private Pont getEntree(){
        return plateau[yEntree][xEntree];
    }

    private Pont getSortie(){
        return plateau[ySortie][xSortie];
    }

}
