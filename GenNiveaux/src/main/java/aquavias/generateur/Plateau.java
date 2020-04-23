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
            while(!verifMur(i, newY)){
                char nextOrientation = Pont.getNextOrientation(plateau[i][newY].getOrientation());
                plateau[i][newY].setOrientation(nextOrientation);
            }
            completeChemin(i-1, oldY, newY);
            oldY = newY;

        }
        completeChemin(xSortie-1,oldY, ySortie);

    }

    private boolean verifMur(int x, int y){
        boolean[] sorties = plateau[x][y].calculSorties();
        int nord = y - ((sorties[0])?1:0);
        int est = x + ((sorties[1])?1:0);
        int sud = y + ((sorties[2])?1:0);
        int ouest = x - ((sorties[3])?1:0);
        System.out.println("pont en " + x + " - " + y + " a pour coord sorties " + nord + " " + est + " " + sud + " " + ouest);
        int[] coordSorties = {nord, est, sud, ouest};
        for(int i = 0; i < coordSorties.length; i++){
            if(coordSorties[i] < 0) return false;
            if(i%2 == 0 && coordSorties[i] >= this.getHauteur()) return false;
            if(i%2 == 1 && coordSorties[i] >= this.getLargeur()) return false;
        }
        return true;
    }

    private void completeChemin(int x, int y, int newY){
        int i = y;
        System.out.println("newY : " + newY);
        /*
         * On part du pont en (x y) et on veut créer un chemin jusqu'au pont en (x+1 y)
         * fixme : il faut traiter tous les cas non triviaux
         * */
        while(i != newY){
            System.out.println("Completion du chemin x - y : " + x + " - " + i);
            if ((i > newY)) i--;
            else i++;
            //WIP : il faut traiter les ponts !
            plateau[x][i] = createPont(null);
            /*
            * On fait en sorte que le pont ne dirige pas une de ses sorties vers le bord du plateau
            * */
            int g = 0;
            while(!verifMur(x, i)){
                g++;
                char nextOrientation = Pont.getNextOrientation(plateau[x][i].getOrientation());
                plateau[x][i].setOrientation(nextOrientation);
                if(g > 5){
                    return;
                }
            }
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
        /*
         * fixme : pour les ponts I -> Les tournes dans le mauvais sens car ils ont deux sorties : Place donc la sortie "cachée" comme entrée
         * */
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
