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
    private static boolean[][] passage;

    /**
     * INIT PART
     * */

    Plateau(int largeur, int hauteur) {
        nbConnex = new int[largeur][hauteur];
        for (int i = 0; i < largeur ; i++)
            for (int j = 0; j < hauteur; j++)
                nbConnex[i][j] = 0;

        passage = new boolean[largeur][hauteur];
        for (int i = 0; i < largeur ; i++)
            for (int j = 0; j < hauteur; j++)
                passage[i][j] = false;

        this.plateau = new Pont[largeur][hauteur];
        for (int i = 0; i < largeur; i++)
            for (int j = 0; j < hauteur; j++)
                this.plateau[i][j] = null;

        this.placerEntreeSortie();
        this.genererChemin(this.xEntree, this.yEntree);
        this.genererChemin(this.xSortie, this.ySortie);

    }

    /**
     * Algo Part
     * */

    //fixme: il faut encore diriger si possible la création de pont vers la sortie et regler le probleme de rotation
    private void genererChemin(int x, int y) {
        passage[x][y] = true;
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        for (int i = 0; i < sorties.length; i++) {
            if (sorties[i]) {
                int newX = acces[i][0];
                int newY = acces[i][1];
                if (this.plateau[newX][newY] == null) {
                    this.ajouterPontBiaiser(newX, newY, x, y);
                    this.genererChemin(newX, newY);
                }else {
                    if (!this.isConnected(x, y, newX, newY)) {
                        if (this.plateau[newX][newY].getForme() == 'T') {
                            System.out.println("on mettra un + en : " + newX + " " + newY);
                        }else {
                            System.out.println("on mettra un T en : " + newX + " " + newY);
                            if (this.plateau[newX][newY].getSpe() == null) {
                                this.plateau[newX][newY] = this.createPont('T', null);
                                this.satisfaitSortiesPont(newX, newY);
                            }else if (this.plateau[newX][newY].getSpe().equals("entree") || this.plateau[newX][newY].getSpe().equals("sortie")) {
                                this.plateau[newX][newY] = this.creerEntreeSortie('T', (this.plateau[newX][newY].getSpe().equals("entree")));
                                this.satisfaitEntreeSorties(newX, newY);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Fonction simple
     * */

    private void ajouterPontBiaiser(int x, int y, int oldX, int oldY) {
        this.plateau[x][y] = this.createPont('O', null);
        this.lierPontWith(x, y, oldX, oldY);
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        if (sorties[3]) {
            if (acces[3][0] != oldX || acces[3][1] != oldY) {
                int random = ThreadLocalRandom.current().nextInt(2);
                if (random == 1) {
                    if (this.plateau[x][y].getForme() == 'T') {
                        while (sorties[3]) {
                            char nextOrientation = Pont.getNextOrientation(this.plateau[x][y].getOrientation());
                            this.plateau[x][y].setOrientation(nextOrientation);
                            sorties = this.plateau[x][y].calculSorties();
                        }
                    }else if (this.plateau[x][y].getForme() == 'L') {
                        int old = -1;
                        for (int i = 0; i < acces.length; i++) {
                            if (acces[i][0] == oldX && acces[i][1] == oldY)
                                old = i;
                        }
                        if (old == -1) throw new RuntimeException("le pont en L ne peut pas etre retourné");
                        while (sorties[3] || !this.isConnected(x, y, oldX, oldY)) {
                            char nextOrientation = Pont.getNextOrientation(this.plateau[x][y].getOrientation());
                            this.plateau[x][y].setOrientation(nextOrientation);
                            sorties = this.plateau[x][y].calculSorties();
                        }
                    }
                }
            }
        }
        this.lierPontWith(x, y, oldX, oldY);
    }

    private boolean isConnected(int x, int y, int newX, int newY) {
        int[][] acces = this.getAcces(x,y);
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] newAcces = this.getAcces(newX, newY);
        boolean[] newSorties = this.plateau[newX][newY].calculSorties();
        for(int i = 0; i < sorties.length; i++){
            for(int j = 0; j < sorties.length; j++){
                if(sorties[i] && newSorties[j]){
                    if(acces[i][0] == newX && acces[i][1] == newY && newAcces[j][0] == x && newAcces[j][1] == y){
                        System.out.println("is Connected : " + x + " " + y + " " + newX + " " + newY + " true");
                        return true;
                    }
                }
            }
        }
        System.out.println("is Connected : " + x + " " + y + " " + newX + " " + newY + " false");
        return false;
    }

    private int nombreSorties(int x, int y){
        int compteur = 0;
        boolean[] sorties = plateau[x][y].calculSorties();
        for(int i = 0; i < sorties.length; i++){
            if(sorties[i]){
                compteur++;
            }
        }
        return compteur;
    }

    private int nombrePontVoisin(int x, int y){
        int[][] acces = this.getAcces(x, y);
        int compteur = 0;
        for(int i = 0; i < acces.length; i++){
            int newX = acces[i][0];
            int newY = acces[i][1];
            if(newX != x &&  newY != y){
                if(estDansPlateau(newX, newY) && plateau[newX][newY] != null){
                    compteur++;
                }
            }
        }
        return compteur;
    }

    private boolean estDansPlateau(int x, int y){
        return x >= 0 && x < this.getLargeur() && y >= 0 && y < this.getHauteur();
    }

    private void satisfaitSortiesPont(int x, int y) {
        Pont pont = this.plateau[x][y];
        int compteur = 0;
        while (!this.sortiesSatisfaites(x, y, false)) {
            compteur++;
            char newOrientation = Pont.getNextOrientation(pont.getOrientation());
            pont.setOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : C'est normalement impossible
                throw new RuntimeException("Erreur les sorties ne peuvent pas etre toutes satisfaites");
            }
        }
    }

    private void satisfaitEntreeSorties(int x, int y) {
        //fixme : wip
        Pont pont = this.plateau[x][y];
        boolean isEnCours = false;
        if (pont.getSpe().equals("entree") || pont.getSpe().equals("sortie")) {
            boolean[] sorties = pont.calculSorties();
            int[][] acces = this.getAcces(x, y);
            for (int i = 0; i < sorties.length; i++) {
                if (sorties[i]) {
                    if (this.plateau[acces[i][0]][acces[i][1]] == null)
                        isEnCours = true;
                }
            }
        }
        int compteur = 0;
        while (!this.sortiesSatisfaites(x, y, isEnCours)) {
            compteur++;
            char newOrientation = Pont.getNextOrientation(pont.getOrientation());
            pont.setOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : C'est normalement impossible
                throw new RuntimeException("Erreur les sorties ne peuvent pas etre toutes satisfaites");
            }
        }
    }

    private boolean sortiesSatisfaites(int x, int y, boolean entree) {
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        for (int i = 0; i < sorties.length; i++) {
            if (sorties[i]) {
                int newX = acces[i][0];
                int newY = acces[i][1];
                if (newX < 0 || newX >= this.getLargeur() || newY < 0 || newY >= this.getHauteur()) {
                    return false;
                }
                if (this.plateau[newX][newY] == null && !entree) {
                    return false;
                }
            }
        }
        return true;
    }

    private void lierPontWith(int x, int y, int oldX, int oldY) {
        Pont pont = this.plateau[x][y];
        char sortie;
        if (x == oldX+1) {
            sortie = 'E';
        }else if (x == oldX-1) {
            sortie = 'O';
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

    //FIXME : devrait etre dans aquavias
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

    private Pont createPont(char forme, String spe, char orientation) {
        if(forme == 'O') {
            forme = this.chooseForme();
        }
        Pont p;
        switch(forme) {
            case 'I' :
                p = new PontI(orientation, spe);
                break;
            case 'L' :
                p = new PontL(orientation, spe);
                break;
            case 'T' :
                p = new PontT(orientation, spe);
                break;
            default :
                throw new RuntimeException("char du pont inconnu");
        }
        p.setOrientation(orientation);
        return p;
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
        Pont entree = this.creerEntreeSortie('O', true);
        this.plateau[i][j] = entree;
        this.xEntree = i;
        this.yEntree = j;

        i = this.getLargeur()-1;
        /*j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());*/
        j = 1;
        Pont sortie = this.creerEntreeSortie('O', false);
        this.plateau[i][j] = sortie;
        this.xSortie = i;
        this.ySortie = j;
    }

    private Pont creerEntreeSortie(char forme, boolean entree) {
        Pont p;
        do {
            p = this.createPont(forme, (entree)? "entree" : "sortie");
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

    private int[][] getAcces(int x, int y) {
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
