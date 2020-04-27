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
        System.out.println("X entree - Y entree" + xEntree + " - " + yEntree);
        int oldY = yEntree;
        for (int i = xEntree+1; i < this.getLargeur()-1; i++) {
            int newY = this.placePillierSuivant(i-1, oldY); //this.placeAleaPont(i, 0, this.getHauteur());
            this.traitementMur(i, newY);
            //Si le pont est en L (sortie dans la direction du chemin a complété, on complete le chemin décalé d'une colonne vers la droite ? Je ne pense plus ça nécéssaire maintenant
            this.completeChemin(i-1, oldY, i, newY);
            oldY = newY;
        }
        completeChemin(xSortie-1, oldY, xSortie, ySortie);

    }
    /**
     * ALGO PART
      */

    private void completeChemin(int x, int y, int newX, int newY) {
        System.out.println("newY : " + newY);
        /*
         * On part du pont en (x y) et on veut créer un chemin jusqu'au pont en (x+1 y)
         * fixme : il faut traiter tous les cas non triviaux
         * */
        int[][] acces = this.getAcces(x, y);
        for (int[] sortie : acces) {
            if (sortie[0] != x || sortie[1] != y) {
                int i = sortie[0];
                int j = sortie[1];
                while (i != newX || j != newY) {
                    if (this.plateau[i][j] == null) {
                        System.out.println("Completion du chemin x - y : " + x + " - " + i);
                        this.plateau[i][j] = createPont('O', null);
                        this.traitementMur(i, j);
                    }else break;

                    this.completeChemin(i,j, newX, newY);

                    if (ThreadLocalRandom.current().nextBoolean()) {
                        if (i > newX) i--;
                        else if (i < newX) i++;
                        else {
                            if (j > newY) j--;
                            else j++;
                        }
                    }else {
                        if (j > newY) j--;
                        else if (j < newY) j++;
                        else {
                            if (i > newX) i--;
                            else i++;
                        }
                    }
                }
            }
        }
    }

    private void traitementMur(int x, int y){
        int g = 0;
        while(!verifMur(x, y)){
            g++;
            char nextOrientation = Pont.getNextOrientation(plateau[x][y].getOrientation());
            plateau[x][y].setOrientation(nextOrientation);
            if(g == 4){ //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : pont en T dans un angle
                plateau[x][y] = createPont('L', null);
                traitementMur(x, y);
            }

        }
    }
    /**
     * fixme : WIP
     * */
    private void verifCompletChemin(int x1, int y1, int x2, int y2){
        /* on suppose pour le moment que (x1 y1) (x2 y2) sont à coté l'un de l'autre*/
        int[][] acces = getAcces(x1, y1);
    }

    private int placePillierSuivant(int x, int y) {
        int newY;
        switch (this.traitementPontPrecedent(x, y)) {
            case 0: newY = this.placeAleaPont(x+1, 0, this.getHauteur());
                break;
            case 1: newY = this.placeAleaPont(x+1, 0, y);
                break;
            case 2: newY = this.placeAleaPont(x+1, y+1, this.getHauteur());
                break;
            case 3 : System.out.println("debug, y-1 = " + (y-1));
                newY = this.placeAleaPont(x+1,0, y-1);
                break;
            case 4 : System.out.println("debug, y+2 = " + (y+2));
                newY = this.placeAleaPont(x+1,y+2, this.getHauteur());
                break;
            default: throw new RuntimeException("traitementEntree retourne une valeure différente de 0, 1 ou 2");
        }
        this.traitementMur(x+1, newY);
        this.completeChemin(x, y, x+1, newY);
        return newY;
    }
    private int traitementPontPrecedent(int x, int y) {
        /*
         * vocation a disparaitre un jour
         * 0 -> pas de traitement spécial
         * 1 -> pont precedent est dirigée vers le Nord
         * 2 -> pont precedent est dirigée vers le Sud
         * 3 -> pont suivant ne doit pas etre generer un cran en dessous (ou un cran au dessus) et un cran a droite donc sera generer au dessous (tiré aleatoirement (si possible) entre cas 3 et 4)
         * 4 -> pont suivant ne doit pas etre generer un cran en dessous (ou un cran au dessus) et un cran a droite donc sera generer au dessus (tiré aleatoirement (si possible) entre cas 3 et 4)
         * */
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

    /**
     * Fonction simple
     * */

    private int placeAleaPont(int x, int borneMinY, int borneMaxY) {
        int newY = ThreadLocalRandom.current().nextInt(borneMinY, borneMaxY);
        System.out.println("Nouveau y : " + newY);
        plateau[x][newY] = createPont('O', null);
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
        int[][] acces = getAcces(x, y);
        for(int i = 0; i < acces.length; i++){
            if(acces[i][0] < 0 || acces[i][1] < 0 || acces[i][0] >= this.getLargeur() || acces[i][1] >= this.getHauteur()) return false;
        }
        return true;
    }

    /**
     * GETTEUR PART
     */
    int[][] getAcces(int x, int y) {
        boolean[] sorties = plateau[x][y].calculSorties();
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

}
