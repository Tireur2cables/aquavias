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

        /**
         * TODO
         *  - SI : pont entrée à sortie y+1 et newPont est placé en newY <= y ==> il faut faire une courbe
         *  - SI : pont entrée à sortie y-1 et newPont placé en newY <= y => simple
         *          same si sortie = y+1 et newY => y
         *  - SI : pont entrée = y / x+1 et entrée newY = x-1 ==> il faut faire une courbe
         *          sinon simple
         */
        int newY = 0;
        int oldY = yEntree;
        for(int i = xEntree+1; i < this.getLargeur()-1; i++){
            newY = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
            System.out.println("xEntree, yEntree : " + xEntree + " " + yEntree);
            System.out.println("Nouveau y : " + newY);
            plateau[i][newY] = createPont(null);
            completeChemin(xEntree, oldY, newY);
            oldY = newY;

        }
        completeChemin(xSortie-1,oldY, ySortie);

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
