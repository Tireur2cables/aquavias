package aquavias.generateur;

import aquavias.jeu.*;

import java.util.concurrent.ThreadLocalRandom;

class Plateau {

    private final Pont[][] plateau;
    private int xEntree;
    private int yEntree;
    private int xSortie;
    private int ySortie;
    private final Jeu jeu;


    /**
     * INIT PART
     * */

    Plateau(int largeur, int hauteur, boolean melange, Jeu jeu) {
        this.plateau = new Pont[largeur][hauteur];
        this.jeu = jeu;
        do {
            for (int i = 0; i < largeur; i++)
                for (int j = 0; j < hauteur; j++)
                    this.plateau[i][j] = null;
            this.placerEntreeSortie();
            System.out.println("entree en " + this.plateau[this.xEntree][this.yEntree].getForme() + " orientation " + this.plateau[this.xEntree][this.yEntree].getOrientation() + " en " + this.xEntree + " " + this.yEntree);
            System.out.println("sortie en " + this.plateau[this.xSortie][this.ySortie].getForme() + " orientation " + this.plateau[this.xSortie][this.ySortie].getOrientation() + " en " + this.xSortie + " " + this.ySortie);
            this.genererChemin(this.xEntree, this.yEntree);
            this.genererChemin(this.xSortie, this.ySortie);
            this.jeu.setPlateau(this.plateau);
        }while (!this.jeu.calculVictoire());

        if(melange){
           this.melange();
        }

    }

    /**
     * Algo Part
     * */

    private void genererChemin(int x, int y) {
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        for (int i = 0; i < sorties.length; i++) {
            if (sorties[i]) {
                int newX = acces[i][0];
                int newY = acces[i][1];
                if (this.plateau[newX][newY] == null) {
                    this.ajouterPontBiaiser(newX, newY, x, y);
                    System.out.println("ajout d'un pont en " + this.plateau[newX][newY].getForme() + " orientation " + this.plateau[newX][newY].getOrientation() + " en " + newX + " " + newY);
                    this.genererChemin(newX, newY);
                }else {
                    if (!this.isConnected(x, y, newX, newY)) {
                        boolean[] oldSorties = this.plateau[newX][newY].calculSorties();
                        if (this.shouldBeX(newX, newY)) {
                            if (this.plateau[newX][newY].getSpe() != null && this.plateau[newX][newY].getSpe().equals("entree"))
                                throw new RuntimeException("Tentative de transformation du pont d'entrée en pont en X");
                            this.plateau[newX][newY] = this.createPont('X', this.plateau[newX][newY].getSpe());
                        }else if (this.shouldBeL(newX, newY)) {
                            this.plateau[newX][newY] = this.createPont('L', this.plateau[newX][newY].getSpe());
                            this.satisfaitSortiesPontIO(newX, newY, x, y, oldSorties);
                        }else { //so shouldBeT
                            if (this.plateau[newX][newY].getSpe() == null) {
                                this.plateau[newX][newY] = this.createPont('T', null);
                                this.satisfaitSortiesPont(newX, newY, x, y, oldSorties);
                            }else if (this.plateau[newX][newY].getSpe().equals("entree") || this.plateau[newX][newY].getSpe().equals("sortie")) {
                                this.plateau[newX][newY] = this.creerEntreeSortie('T', (this.plateau[newX][newY].getSpe().equals("entree")));
                                this.satisfaitSortiesPontIO(newX, newY, x, y, oldSorties);
                            }
                        }
                        System.out.println("ajout d'un pont en " + this.plateau[newX][newY].getForme() + " orientation " + this.plateau[newX][newY].getOrientation() + " en " + newX + " " + newY);
                    }
                }
            }
        }
    }

    private void rotateAleaPont(){
        for(int i = 0; i < this.getLargeur(); i++){
            for(int j = 0; j < this.getHauteur(); j++){
                if(this.plateau[i][j] != null && !this.plateau[i][j].isEntree() && !this.plateau[i][j].isSortie()){
                    this.plateau[i][j].setOrientation(this.getRandomOrientation());
                }
            }
        }
    }

    /**
     * Fonction simple
     * */

    private boolean shouldBeL(int x, int y) {
        Pont p = this.plateau[x][y];
        int nbconnex = 0;
        if (x-1 >= 0) {
            Pont p2 = this.plateau[x-1][y];
            if (p2 != null && p2.calculSorties()[1]) nbconnex++;
        }
        if (x+1 < this.getLargeur()) {
            Pont p2 = this.plateau[x+1][y];
            if (p2 != null && p2.calculSorties()[3]) nbconnex++;
        }
        if (y-1 >= 0) {
            Pont p2 = this.plateau[x][y-1];
            if (p2 != null && p2.calculSorties()[2]) nbconnex++;
        }
        if (y+1 < this.getHauteur()) {
            Pont p2 = this.plateau[x][y+1];
            if (p2 != null && p2.calculSorties()[0]) nbconnex++;
        }
        return p.getForme() == 'I' && p.getSpe() != null && p.getSpe().equals("entree") && nbconnex == 2;
    }

    private boolean shouldBeX(int x, int y) {
        Pont p = this.plateau[x][y];
        if (p.getForme() == 'T') return true;
        else {
            int nbconnex = 0;
            if (x-1 >= 0) {
                Pont p2 = this.plateau[x-1][y];
                if (p2 != null && p2.calculSorties()[1]) nbconnex++;
            }
            if (x+1 < this.getLargeur()) {
                Pont p2 = this.plateau[x+1][y];
                if (p2 != null && p2.calculSorties()[3]) nbconnex++;
            }
            if (y-1 >= 0) {
                Pont p2 = this.plateau[x][y-1];
                if (p2 != null && p2.calculSorties()[2]) nbconnex++;
            }
            if (y+1 < this.getHauteur()) {
                Pont p2 = this.plateau[x][y+1];
                if (p2 != null && p2.calculSorties()[0]) nbconnex++;
            }
            return nbconnex == 4;
        }
    }

    private void ajouterPontBiaiser(int x, int y, int oldX, int oldY) {
        this.plateau[x][y] = this.createPont('O', null);
        this.lierPontWith(x, y, oldX, oldY);
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        if (sorties[3]) {
            if (acces[3][0] != oldX || acces[3][1] != oldY) {
                int random = ThreadLocalRandom.current().nextInt(2); // probabilité 1/2
                if (random == 1) {
                    if (this.plateau[x][y].getForme() == 'T') {
                        while (sorties[3]) {
                            char nextOrientation = Pont.getNextOrientation(this.plateau[x][y].getOrientation());
                            this.plateau[x][y].setOrientation(nextOrientation);
                            sorties = this.plateau[x][y].calculSorties();
                        }
                    }else if (this.plateau[x][y].getForme() == 'L') {
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
            for(int j = 0; j < newSorties.length; j++){
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

    private void placerPontInutile() { //fixme random pourrait etre mieux ? pas trop compris
        int random = ThreadLocalRandom.current().nextInt(1, this.getHauteur()*2);
        for (int i = 0; i < random; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, this.getLargeur()-1);
            int y = ThreadLocalRandom.current().nextInt(0, this.getHauteur()-1);
            if (this.plateau[x][y] == null) {
                this.plateau[x][y] = createPont('O', null);
            }
        }
    }

    private void melange(){
        this.rotateAleaPont();
        this.placerPontInutile();
    }

    private boolean conserveConnexion(int x, int y, boolean[] oldConnex) {
        boolean[] sortie = this.plateau[x][y].calculSorties();
        for (int i = 0; i < oldConnex.length; i++) {
            if (oldConnex[i] && !sortie[i]) return false;
        }
        return true;
    }

    private void satisfaitSortiesPont(int x, int y, int mustX, int mustY, boolean[] sorties) {
        Pont pont = this.plateau[x][y];
        int compteur = 0;
        while (!this.sortiesSatisfaites(x, y) || !this.conserveConnexion(x, y, sorties) || ! this.isConnected(x, y, mustX, mustY)) {
            compteur++;
            char newOrientation = Pont.getNextOrientation(pont.getOrientation());
            pont.setOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : C'est normalement impossible
                throw new RuntimeException("Erreur les sorties ne peuvent pas etre toutes satisfaites");
            }
        }
    }

    private void satisfaitSortiesPontIO(int x, int y, int mustX, int mustY, boolean[] sorties) {
        Pont pont = this.plateau[x][y];
        int compteur = 0;
        while (!this.conserveConnexion(x, y, sorties) || ! this.isConnected(x, y, mustX, mustY) || !pont.isOrientationCorrecteEntreeSortie()) {
            compteur++;
            char newOrientation = Pont.getNextOrientation(pont.getOrientation());
            pont.setOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : C'est normalement impossible
                throw new RuntimeException("Erreur les sorties ne peuvent pas etre toutes satisfaites");
            }
        }
    }

    private boolean sortiesSatisfaites(int x, int y) {
        boolean[] sorties = this.plateau[x][y].calculSorties();
        int[][] acces = this.getAcces(x, y);
        for (int i = 0; i < sorties.length; i++) {
            if (sorties[i]) {
                int newX = acces[i][0];
                int newY = acces[i][1];
                char sortie = (i == 0)? 'N' : (i == 1)? 'E' : (i == 2)? 'S' : 'O';
                if (newX < 0 || newX >= this.getLargeur() || newY < 0 || newY >= this.getHauteur()) {
                    return false;
                }
                if (this.plateau[newX][newY] == null) {
                    return false;
                }else if (!this.plateau[newX][newY].isAccessibleFrom(sortie)) {
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

    //FIXME: pourrait faire partie de l'api aquavias
    private char getRandomOrientation(){
        int random = ThreadLocalRandom.current().nextInt(0, 4);
        switch (random){
            case 0 : return 'N';
            case 1 : return 'E';
            case 2 : return 'S';
            case 3 : return 'O';
        }
        throw new RuntimeException("Valeur aléatoire d'orientation inconnue");
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
            case 'X' :
                return new PontX(orientation, spe);
        }
        throw new RuntimeException("char du pont inconnu");
    }

    //fixme devrait etre dans aquavias ?
    private char chooseForme() {
        int random = ThreadLocalRandom.current().nextInt(0, 5);
        switch (random) {
            case 0: case 1:
                return 'I';
            case 2: case 3:
                return 'L';
            case 4 : //moins de probabilité d'avoir un T que d'avoir les autres
                return 'T';
        } //les ponts en X nes sont pas pris en compte pour en avoir seulement lorsqu'on en a besoin
        throw new RuntimeException("Random int out of bounds");
    }

    //fixme pourrait etre dans aquavias
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

    void exportNiveau() {
        this.jeu.exportNiveau();
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

    public Pont[][] getPlateau() {
        return this.plateau;
    }

    private int getLargeur() {
        return this.plateau.length;
    }

    private int getHauteur() {
        return this.plateau[0].length;
    }


}
