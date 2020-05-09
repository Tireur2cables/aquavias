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

    Plateau(int largeur, int hauteur, boolean melange, Jeu jeu, String mode, boolean saveSolution) {
        this.plateau = new Pont[largeur][hauteur];
        this.jeu = jeu;
        do {
            for (int i = 0; i < largeur; i++)
                for (int j = 0; j < hauteur; j++)
                    this.plateau[i][j] = null;
            this.placerEntreeSortie();
            this.genererChemin(this.xEntree, this.yEntree);
            this.genererChemin(this.xSortie, this.ySortie);
            this.jeu.setPlateau(this.plateau);
        } while (!this.jeu.isVictoire());
        if (saveSolution)
            this.exportNiveau();
        int limite = 0;
        if (melange) {
            do {
                limite = this.melange();
                this.jeu.setPlateau(this.plateau);
            }while (this.jeu.isVictoire());
        }else
           limite = 100; //totalement arbitraire

        String difficulte = "";
        if (mode.equals("compteur")) {
            this.jeu.setLimite(limite);
            difficulte = (limite < 30)? "Facile" : (limite < 80)? "Moyen" : "Difficile";
        }else if (mode.equals("fuite")) {
            this.jeu.setLimite(limite*2);
            difficulte = (limite*2 < 30)? "Facile" : (limite*2 < 80)? "Moyen" : "Difficile";
        }
        this.jeu.setDifficulte(difficulte);
    }

    /**
     * Algo Part
     * */

    private void genererChemin(int x, int y) {
        boolean[] sorties = this.plateau[x][y].reCalculSorties();
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
                        boolean[] oldSorties = this.plateau[newX][newY].reCalculSorties();
                        if (this.shouldBeX(newX, newY)) {
                            if (this.plateau[newX][newY].getStringSpe() != null && this.plateau[newX][newY].getStringSpe().equals("entree"))
                                throw new RuntimeException("Tentative de transformation du pont d'entrée en pont en X");
                            this.plateau[newX][newY] = Pont.createPont('X', this.plateau[newX][newY].getStringSpe());
                        }else if (this.shouldBeL(newX, newY)) {
                            this.plateau[newX][newY] = Pont.createPont('L', this.plateau[newX][newY].getStringSpe());
                            this.satisfaitSortiesPontIO(newX, newY, x, y, oldSorties);
                        }else { //so shouldBeT
                            if (this.plateau[newX][newY].getStringSpe() == null) {
                                this.plateau[newX][newY] = Pont.createPont('T', null);
                                this.satisfaitSortiesPont(newX, newY, x, y, oldSorties);
                            }else if (this.plateau[newX][newY].getStringSpe().equals("entree") || this.plateau[newX][newY].getStringSpe().equals("sortie")) {
                                this.plateau[newX][newY] = this.creerEntreeSortie('T', (this.plateau[newX][newY].getStringSpe().equals("entree")), newX, newY);
                                this.satisfaitSortiesPontIO(newX, newY, x, y, oldSorties);
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

    private boolean shouldBeL(int x, int y) {
        Pont p = this.plateau[x][y];
        int nbconnex = 0;
        if (x-1 >= 0) {
            Pont p2 = this.plateau[x-1][y];
            if (p2 != null && p2.reCalculSorties()[1]) nbconnex++;
        }
        if (x+1 < this.getLargeur()) {
            Pont p2 = this.plateau[x+1][y];
            if (p2 != null && p2.reCalculSorties()[3]) nbconnex++;
        }
        if (y-1 >= 0) {
            Pont p2 = this.plateau[x][y-1];
            if (p2 != null && p2.reCalculSorties()[2]) nbconnex++;
        }
        if (y+1 < this.getHauteur()) {
            Pont p2 = this.plateau[x][y+1];
            if (p2 != null && p2.reCalculSorties()[0]) nbconnex++;
        }
        return p.getCharForme() == 'I' && p.getStringSpe() != null && p.getStringSpe().equals("entree") && nbconnex == 2;
    }

    private boolean shouldBeX(int x, int y) {
        Pont p = this.plateau[x][y];
        if (p.getCharForme() == 'T') return true;
        else {
            int nbconnex = 0;
            if (x-1 >= 0) {
                Pont p2 = this.plateau[x-1][y];
                if (p2 != null && p2.reCalculSorties()[1]) nbconnex++;
            }
            if (x+1 < this.getLargeur()) {
                Pont p2 = this.plateau[x+1][y];
                if (p2 != null && p2.reCalculSorties()[3]) nbconnex++;
            }
            if (y-1 >= 0) {
                Pont p2 = this.plateau[x][y-1];
                if (p2 != null && p2.reCalculSorties()[2]) nbconnex++;
            }
            if (y+1 < this.getHauteur()) {
                Pont p2 = this.plateau[x][y+1];
                if (p2 != null && p2.reCalculSorties()[0]) nbconnex++;
            }
            return nbconnex == 4;
        }
    }

    private void ajouterPontBiaiser(int x, int y, int oldX, int oldY) {
        this.plateau[x][y] = Pont.createPont('O', null);
        this.lierPontWith(x, y, oldX, oldY);
        boolean[] sorties = this.plateau[x][y].reCalculSorties();
        int[][] acces = this.getAcces(x, y);
        if (sorties[3]) {
            if (acces[3][0] != oldX || acces[3][1] != oldY) {
                int random = ThreadLocalRandom.current().nextInt(3); //probabilité 1/3 de revenir sur soit meme
                if (random != 1) {
                    if (this.plateau[x][y].getCharForme() == 'T') {
                        while (sorties[3]) {
                            char nextOrientation = Pont.getCharNextOrientation(this.plateau[x][y].getCharOrientation());
                            this.plateau[x][y].setCharOrientation(nextOrientation);
                            sorties = this.plateau[x][y].reCalculSorties();
                        }
                    }else if (this.plateau[x][y].getCharForme() == 'L') {
                        while (sorties[3] || !this.isConnected(x, y, oldX, oldY)) {
                            char nextOrientation = Pont.getCharNextOrientation(this.plateau[x][y].getCharOrientation());
                            this.plateau[x][y].setCharOrientation(nextOrientation);
                            sorties = this.plateau[x][y].reCalculSorties();
                        }
                    }
                }
            }
        }
        this.lierPontWith(x, y, oldX, oldY);
    }

    private boolean isConnected(int x, int y, int newX, int newY) {
        //Test si les ponts en x-y et newX-newY partage chacun une sortie -> ils sont interconnectés
        int[][] acces = this.getAcces(x,y);
        boolean[] sorties = this.plateau[x][y].reCalculSorties();
        int[][] newAcces = this.getAcces(newX, newY);
        boolean[] newSorties = this.plateau[newX][newY].reCalculSorties();
        for(int i = 0; i < sorties.length; i++){
            for(int j = 0; j < newSorties.length; j++){
                if(sorties[i] && newSorties[j]){
                    if(acces[i][0] == newX && acces[i][1] == newY && newAcces[j][0] == x && newAcces[j][1] == y){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int placerPontInutile() {
        int compteur = 0;
        //On parcourt le tableau, et pour chaque cases vides, on a 1 chances sur 3 de placer un pont inutile à cette position
        for (int i = 0; i < this.getLargeur(); i++) {
            for (int j = 0; j < this.getHauteur(); j++) {
                if (this.plateau[i][j] == null) {
                    if(ThreadLocalRandom.current().nextInt(0, 3) == 0) {
                        this.plateau[i][j] = Pont.createPont('O', null);
                        compteur += 4;
                    }
                }
            }
        }
        return compteur;
    }

    private int rotateAleaPont(){
        //On parcourt le tableau, et pour chaque cases contenant un pont, on lui donne une nouvelle orientation aléatoire
        int total = 0;
        for(int i = 0; i < this.getLargeur(); i++){
            for(int j = 0; j < this.getHauteur(); j++){
                if(this.plateau[i][j] != null && !this.plateau[i][j].isPontEntree() && !this.plateau[i][j].isPontSortie()){
                    char orientation = this.plateau[i][j].getCharOrientation();
                    char nextOrientation = Pont.getRandomOrientation();
                    total += this.calculDistanceRotation(orientation, nextOrientation) + 1;
                    this.plateau[i][j].setCharOrientation(nextOrientation);
                }
            }
        }
        return total;
    }

    private int calculDistanceRotation(char orientation, char nextOrientation){
        int compteur = 0;
        if(orientation == nextOrientation) return compteur;
        while(orientation != nextOrientation){
            orientation = Pont.getCharNextOrientation(orientation);
            compteur++;
        }
        return compteur;
    }

    private int melange(){
        int total = this.rotateAleaPont();
        total += this.placerPontInutile();
        return total;
    }

    private boolean conserveConnexion(int x, int y, boolean[] oldConnex) {
        boolean[] sortie = this.plateau[x][y].reCalculSorties();
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
            char newOrientation = Pont.getCharNextOrientation(pont.getCharOrientation());
            pont.setCharOrientation(newOrientation);
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
            char newOrientation = Pont.getCharNextOrientation(pont.getCharOrientation());
            pont.setCharOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : C'est normalement impossible
                throw new RuntimeException("Erreur les sorties ne peuvent pas etre toutes satisfaites");
            }
        }
    }

    private boolean sortiesSatisfaites(int x, int y) {
        boolean[] sorties = this.plateau[x][y].reCalculSorties();
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
                }else if (!this.plateau[newX][newY].isPontAccessibleFrom(sortie)) {
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
        while (!pont.isPontAccessibleFrom(sortie) || !verifMur(x, y)) {
            compteur++;
            char newOrientation = Pont.getCharNextOrientation(pont.getCharOrientation());
            pont.setCharOrientation(newOrientation);
            if (compteur == 4) { //On a fait les 4 orientations possibles et le pont n'en satisfait aucune : pont en T ou en I dans un angle
                this.plateau[x][y] = Pont.createPont('L', null);
                compteur = 0;
            }
        }
    }
    
    private void placerEntreeSortie() {
        int i = 0;
        int j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
        this.xEntree = i;
        this.yEntree = j;
        Pont entree = this.creerEntreeSortie('O', true, xEntree, yEntree);
        this.plateau[i][j] = entree;

        i = this.getLargeur()-1;
        j = ThreadLocalRandom.current().nextInt(0, this.getHauteur());
        this.xSortie = i;
        this.ySortie = j;
        Pont sortie = this.creerEntreeSortie('O', false, xSortie, ySortie);
        this.plateau[i][j] = sortie;
    }

    private Pont creerEntreeSortie(char forme, boolean entree, int x, int y) {
        Pont p;
        do {
            p = Pont.createPont(forme, (entree)? "entree" : "sortie");
        }while (!p.isOrientationCorrecteEntreeSortie() || !this.verifMur(x, y, p));
        return p;
    }

    void exportNiveau() {
        this.jeu.niveauToJSON(false);
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

    private boolean verifMur(int x, int y, Pont p) {
        int[][] acces = this.getAcces(x, y, p);
        for (int i = 0; i < acces.length; i++) {
            if(acces[i][0] < 0 || acces[i][1] < 0 || acces[i][0] >= this.getLargeur() || acces[i][1] >= this.getHauteur()) return false;
        }
        return true;
    }

    /**
     * GETTEUR PART
     */

    private int[][] getAcces(int x, int y) {
        boolean[] sorties = this.plateau[x][y].reCalculSorties();
        int[] nord = {x, y - ((sorties[0])? 1 : 0)};
        int[] est = {x + ((sorties[1])? 1 : 0), y};
        int[] sud = {x, y + ((sorties[2])? 1 : 0)};
        int[] ouest = {x - ((sorties[3])? 1 : 0), y};
        return new int[][]{nord, est, sud, ouest};
    }

    private int[][] getAcces(int x, int y, Pont p){
        boolean[] sorties = p.reCalculSorties();
        int[] nord = {x, y - ((sorties[0])? 1 : 0)};
        int[] est = {x + ((sorties[1])? 1 : 0), y};
        int[] sud = {x, y + ((sorties[2])? 1 : 0)};
        int[] ouest = {x - ((sorties[3])? 1 : 0), y};
        return new int[][]{nord, est, sud, ouest};
    }

    private int getLargeur() {
        return this.plateau.length;
    }

    private int getHauteur() {
        return this.plateau[0].length;
    }


}
