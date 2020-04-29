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

    private static int[][] nbConnex;

    /**
     * INIT PART
     * */

    Plateau(int largeur, int hauteur) {
        nbConnex = new int[largeur][hauteur];
        for (int i = 0; i < largeur ; i++)
            for (int j = 0; j < hauteur; j++)
                nbConnex[i][j] = 0;

        this.plateau = new Pont[largeur][hauteur];
        for (int i = 0; i < largeur; i++)
            for (int j = 0; j < hauteur; j++)
                this.plateau[i][j] = null;

        this.placerEntreeSortie();
        this.generateChemin();
    }

    /**
     * Algo Part
     * */

    private void generateChemin() {
        boolean[] sorties = this.plateau[xEntree][yEntree].calculSorties();
        int[][] acces = getAcces(xEntree, yEntree);
        for(int i = 0; i < sorties.length; i++){
            if(sorties[i]){
                //traitement
            }
        }
    }

    /**
     * Fonction simple
     * */

    private void lierPontWith(int x, int y, int oldX, int oldY) {
        Pont pont = this.plateau[x][y];
        char sortie;
        if (x == oldX+1) {
            sortie = 'O';
        }else if (x == oldX-1) {
            sortie = 'E';
        }else if (y == oldY+1) {
            sortie = 'S';
        }else if (y == oldY-1) {
            sortie = 'N';
        }else {
            throw new RuntimeException("Les ponts (" + oldX + ", " + oldY + ") et (" + x + ", " + y + ") ne peuvent pas etre lier aussi simplement");
        }
        int compteur = 0;
        while (!pont.isAccessibleFrom(sortie) || !verifMur(x, y)) {
            compteur++;
            char newOrientation = Pont.getNextOrientation(pont.getOrientation());
            pont.setOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : pont en T ou en I dans un angle
                this.plateau[x][y] = this.createPont('L', null);
                compteur = 0;
            }
        }
    }

    private int placeAleaPont(int x, int borneMinY, int borneMaxY) {
        int newY = ThreadLocalRandom.current().nextInt(borneMinY, borneMaxY);
        System.out.println("Nouveau y : " + newY);
        this.plateau[x][newY] = this.createPont('O', null);
        return newY;
    }

    private Pont createPont(char forme, String spe) {
        if(forme == 'O') {
            forme = this.chooseForme();
        }
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
        int random = ThreadLocalRandom.current().nextInt(0, 5);
        switch (random) {
            case 0: case 1:
                return 'I';
            case 2: case 3:
                return 'L';
            case 4 : //moins de probabilité d'avoir un T que d'avoir les autres
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
        Pont entree = this.creerEntreeSortie(true);
        this.plateau[i][j] = entree;
        this.xEntree = i;
        this.yEntree = j;

        i = this.getLargeur()-1;
        /*j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());*/
        j = 1;
        Pont sortie = this.creerEntreeSortie(false);
        this.plateau[i][j] = sortie;
        this.xSortie = i;
        this.ySortie = j;
    }

    private Pont creerEntreeSortie(boolean entree) {
        Pont p;
        do {
            p = this.createPont('O', (entree)? "entree" : "sortie");
        }while (!p.isOrientationCorrecteEntreeSortie());
        return p;
    }

    /**
     * VERIF PART
     */

    private boolean verifMur(int x, int y) {
        int[][] acces = this.getAcces(x, y);
        for (int i = 0; i < acces.length; i++) {
            if(acces[i][0] < 0 || acces[i][1] < 0 || acces[i][0] >= this.getLargeur() || acces[i][1] >= this.getHauteur()) return false;
        }
        return true;
    }

    /**
     * GETTEUR PART
     */
    int[][] getAcces(int x, int y) {
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[] nord = {x, y - ((sorties[0])? 1 : 0)};
        int[] est = {x + ((sorties[1])? 1 : 0), y};
        int[] sud = {x, y + ((sorties[2])? 1 : 0)};
        int[] ouest = {x - ((sorties[3])? 1 : 0), y};
        //System.out.println("pont en " + x + " - " + y + " a pour coord acces " + nord + " " + est + " " + sud + " " + ouest);
        return new int[][]{nord, est, sud, ouest};
    }

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
        /*
    private void setCheminPont(){
        Pont entree = this.getEntree();
        Pont sortie = this.getSortie();
        //On considére point le moment uniquement l'entrée et la sortie aux points xEntree, yEntree, xSortie, ySortie
        //On génère un nombre aléatoire pour prendre le y du pont en coordonné xEntree +1
        //On complete le chemin entre le pont d'entree et le nouveau pont


         * TODO :
         *  -Il faut considérer les différents points d'entrées (forme du pont d'entrée)
         *  -Il faut considérer la liaisons avec les ponts créés pendant la compléttion du chemin
         *
         *


         * TODO
         *  - SI : pont entrée à sortie y+1 et newPont est placé en newY <= y ==> il faut faire une courbe
         *  - SI : pont entrée à sortie y-1 et newPont placé en newY <= y => simple
         *          same si sortie = y+1 et newY => y
         *  - SI : pont entrée = y / x+1 et entrée newY = x-1 ==> il faut faire une courbe
         *          sinon simple

        System.out.println("X entree - Y entree" + this.xEntree + " - " + this.yEntree);
        int oldY = this.yEntree;
        for (int i = this.xEntree+1; i < this.getLargeur()-1; i++) {
            oldY = this.placePillierSuivant(i-1, oldY);
        }
        this.completeChemin(this.xSortie-1, oldY, this.xSortie, this.ySortie);
        this.cheminInverse(this.xSortie, this.ySortie);
    }


     * ALGO PART


    private int placePillierSuivant(int x, int y) {
        int newY;
        switch (this.traitementPontPrecedent(x, y)) {
            case 0: newY = this.placeAleaPont(x+1, 0, this.getHauteur());
                break;
            case 1: newY = this.placeAleaPont(x+1, 0, y);
                break;
            case 2: newY = this.placeAleaPont(x+1, y+1, this.getHauteur());
                break;
            case 3 : newY = this.placeAleaPont(x+1,0, y-1);
                break;
            case 4 : newY = this.placeAleaPont(x+1,y+2, this.getHauteur());
                break;
            default: throw new RuntimeException("traitementEntree retourne une valeure différente de 0, 1 ou 2");
        }
        this.traitementMur(x+1, newY);
        this.completeChemin(x, y, x+1, newY);
        return newY;
    }

    private int traitementPontPrecedent(int x, int y) {

         * vocation a disparaitre un jour
         * 0 -> pas de traitement spécial
         * 1 -> pont precedent est dirigée vers le Nord
         * 2 -> pont precedent est dirigée vers le Sud
         * 3 -> pont suivant ne doit pas etre generer un cran en dessous (ou un cran au dessus) et un cran a droite donc sera generer au dessous (tiré aleatoirement (si possible) entre cas 3 et 4)
         * 4 -> pont suivant ne doit pas etre generer un cran en dessous (ou un cran au dessus) et un cran a droite donc sera generer au dessus (tiré aleatoirement (si possible) entre cas 3 et 4)
         *
        Pont precedent = this.plateau[x][y];
        int[][] acces = this.getAcces(x, y);
        boolean[] sorties = precedent.calculSorties();
        if (precedent.getForme() == 'L') {
            for (int i = 0; i < sorties.length; i++) {
                if (sorties[i]) {
                    int[] coord = acces[i];
                    if (coord[1] != y) //savoir si le pont doit etre generer au dessus ou en dessous du pont d'avant
                        return (coord[1] < y)? 1 : 2;
                }
            }
        }else if (precedent.getForme() == 'I') {
            if (precedent.getOrientation() == 'O' || precedent.getOrientation() == 'E') {
                if (y+2 >= this.getHauteur() && y-1 > 0)
                    return 3;
                else if (y-1 <= 0 && y+2 < this.getHauteur())
                    return 4;
                else if (y-1 > 0 && y+2 < this.getHauteur())
                    return ThreadLocalRandom.current().nextBoolean() ? 3 : 4;
                else
                    throw new RuntimeException("ALERTE O GOGOL JE NE SAIS PAS QUOI FAIRE SI ON ARRIVE ICI");
            }
        }
        return 0;
    }

    private void traitementMur(int x, int y) {
        int compteur = 0;
        while (!verifMur(x, y)) {
            compteur++;
            char nextOrientation = Pont.getNextOrientation(this.plateau[x][y].getOrientation());
            this.plateau[x][y].setOrientation(nextOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : pont en T ou en I dans un angle
                this.plateau[x][y] = this.createPont('L', null);
                compteur = 0;
            }
        }
    }

    private void completeChemin(int x, int y, int newX, int newY) {
        System.out.println("newY : " + newY);
        /*
         * On part du pont en (x y) et on veut créer un chemin jusqu'au pont en (x+1 y)
         * fixme : il faut traiter tous les cas non triviaux
         *
        int oldX = x;
        int oldY = y;
        int[][] acces = this.getAcces(x, y);
        for (int[] sortie : acces) {
            if (sortie[0] != x || sortie[1] != y) {
                int i = sortie[0];
                int j = sortie[1];
                while (i < newX || j != newY) {
                    //visiblement oldx et  old y gere mal la recursion
                    if ((Math.abs(x-i) < Math.abs(oldX-i) && x != i) || oldX == i) oldX = x;
                    if ((Math.abs(y-j) < Math.abs(oldY-j) && y != j) || oldY == j) oldY = y;

                    if (this.plateau[i][j] == null) {
                        System.out.println("Completion du chemin oldX - oldY -> i - j: " + oldX + " - " + oldY + " -> " + i + " - " + j);
                        this.plateau[i][j] = this.createPont('O', null);
                        this.lierPontWith(i, j, oldX, oldY);
                    }else break;

                    this.completeChemin(i, j, newX, newY);

                    oldX = i;
                    oldY = j;
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        if (i < newX) i++;
                        else {
                            if (j > newY) j--;
                            else j++;
                        }
                    }else {
                        if (j > newY) j--;
                        else if (j < newY) j++;
                        else i++;
                    }
                }
            }
        }
    }

    private void cheminInverse(int x, int y) {
//FIXME: wip
    }


     * fixme : WIP
     *
    private void verifCompletChemin(int x1, int y1, int x2, int y2){
         on suppose pour le moment que (x1 y1) (x2 y2) sont à coté l'un de l'autre
        int[][] acces = this.getAcces(x1, y1);
    }

    */

}
