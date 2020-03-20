import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/* Imports with maven dependecies */
import org.apache.commons.io.FileUtils;
import org.json.*;

class Jeu {

    /* FIXME: vraiment utile ? */
    private class Case {

        private Pont pont;

        private Case(int ligne, JSONArray json) {
            JSONArray tab = ((JSONArray) json.get(ligne));
            this.pont = (tab.length() <= 0)? null : initPont(tab);
        }

        private Pont initPont(JSONArray tab) {
            switch(tab.getString(0).toUpperCase().charAt(0)) {
                case 'I' :
                    return new PontI(tab);
                case 'L' :
                    return new PontL(tab);
                case 'T' :
                    return new PontT(tab);
            }
            throw new RuntimeException("char du pont inconnu");
        }

    }

    private Controleur controleur;
    private Case[][] plateau;
    private int numNiveau;
    private int xEntree;
    private int yEntree;
    private int xSortie;
    private int ySortie;
    private String mode;
    private int limite;
    private double compteur;
    private double debit;

    /**
     * AFFICHAGE PART
     *
     * Avec cette méthode d'affichage les colonnes sont affichées en premières pour chaque lignes
     * donc on échange les indices i et j pour les afficher correctement
     * */
    private void afficher() {
        System.out.println("Test affichage terminal du niveau");
        if (this.plateau.length <= 0) return;
        for (int i = 0; i < this.plateau[0].length; i++) {
            for (Case[] cases : this.plateau) {
                if (cases[i].pont == null) System.out.print("- ");
                else System.out.print(cases[i].pont.getEau() + " ");
            }
            System.out.println();
        }
    }

    /**
     * INIT PART
     * */

    Jeu(Controleur controleur) {
        this.controleur = controleur;
    }

    private static String niveauxDir = "resources/niveaux/niveau";

    void initNiveau(int number) {
        this.numNiveau = number;
        String chemin = niveauxDir + this.numNiveau + ".json";
        JSONObject json = readJSON(chemin);
        int hauteur = json.getInt("hauteur");
        int largeur = json.getInt("largeur");
        this.mode = json.getString("mode");
        this.limite = json.getInt("limite");
        this.compteur = json.getDouble("compteur");
        JSONArray niveau = json.getJSONArray("niveau");
        this.initPlateau(largeur, hauteur, niveau);
        this.chercheEntree();
        this.chercheSortie();
        this.parcourchemin();
        this.initDebit();
    }

    private static JSONObject readJSON(String chemin) {
        try {
            File f = new File(chemin);
            return new JSONObject(FileUtils.readFileToString(f, "utf-8"));
        }catch (NullPointerException n) {
            throw new RuntimeException("Impossible de trouver le fichier de niveau à l'adresse : " + chemin);
        }catch (IOException i) {
            throw new RuntimeException("Impossible de lire le fichier niveau à l'adresse : " + chemin);
        }
    }

    private void initPlateau(int largeur, int hauteur, JSONArray niveau) {
        this.plateau = new Case[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            JSONArray colonne = ((JSONArray) niveau.get(i));
            for (int j = 0; j < hauteur; j++) {
                this.plateau[i][j] = new Case(j, colonne);
            }
        }
    }

    private void chercheEntree() {
        for (int i = 0; i < this.getLargeur(); i++) {
            for (int j = 0; j < this.getHauteur(); j++) {
                if (this.plateau[i][j].pont != null && this.plateau[i][j].pont.isEntree()) {
                    this.xEntree = i;
                    this.yEntree = j;
                    return;
                }
            }
        }
    }

    private void chercheSortie() {
        for (int i = 0; i < this.getLargeur(); i++) {
            for (int j = 0; j < this.getHauteur(); j++) {
                if (this.plateau[i][j].pont != null && this.plateau[i][j].pont.isSortie()) {
                    this.xSortie = i;
                    this.ySortie = j;
                    return;
                }
            }
        }
    }

    private void initDebit() {
        switch (this.mode) {
            case "fuite" : this.isEtanche();
                break;
            case "compteur" : this.debit = 1;
                break;
        }
    }

    /**
     * ACTUALISATION EAU PART
     *
     * FIXME: A refactor c'est très laid (trop long et decoupé)
     * */
    void parcourchemin() {
        int x = this.xEntree;
        int y = this.yEntree;
        this.detectAdjacents(x, y);
    }

    /**
     * On parcours toutes les sorties d'un premier morceau de pont (x,y) et on suit le chemin selon ses sorties
     * */
    private void detectAdjacents(int x, int y) {
        Pont p = this.plateau[x][y].pont;
        boolean[] sortiesP = p.getSorties();
        for (int i = 0; i < sortiesP.length; i++) {
            if (sortiesP[i]) {
                this.afficheAdja(i, x, y);
            }
        }
    }

    /**
     *  X = largeur et Y = hauteur
     *  Selon l'entier i donné (0-NORD - 1-EST - 2-SUD - 3-OUEST) on vérifie le voisin dans la direction i
     *
     *  */
    private void afficheAdja(int i, int x, int y) {
        switch (i) {
            case 0 : this.checkAdjaNord(x, y);
                break;
            case 1 : this.checkAdjaEst(x, y);
                break;
            case 2 : this.checkAdjaSud(x, y);
                break;
            case 3 : this.checkAdjaOuest(x, y);
                break;
        }
    }

    private void checkAdjaNord(int x, int y) {
        if (y-1 >= 0) {
            char sortie = 'N';
            Pont p = this.plateau[x][y-1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x, y-1);
                }
            }
        }
    }

    private void checkAdjaEst(int x, int y) {
        if (x+1 < this.getLargeur()) {
            char sortie = 'E';
            Pont p = this.plateau[x+1][y].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x+1, y);
                }
            }
        }
    }

    private void checkAdjaSud(int x, int y) {
        if (y+1 < this.getHauteur()) {
            char sortie = 'S';
            Pont p = this.plateau[x][y+1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x, y+1);
                }
            }
        }
    }

    private void checkAdjaOuest(int x, int y) {
        if (x-1 >= 0) {
            char sortie = 'O';
            Pont p = this.plateau[x-1][y].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x-1, y);
                }
            }
        }
    }

    /**
     * TIMER PART
     */

    private static ScheduledExecutorService timer;
    private static ScheduledFuture<?> tache;
    private static boolean flag; /* décrémente le compteur une dernière fois si le chemin est étanche */

    void initTimer() {
        if (this.controleur.getMode().equals("fuite")) {
            timer = newScheduledThreadPool(1);
            Runnable compteSeconde = new Runnable() {
                @Override
                public void run() {
                    if(debit != 0) {
                        controleur.decrementeCompteur();
                        flag = true;
                    }
                    else if (flag) {
                        controleur.decrementeCompteur();
                        flag = false;
                    }
                }
            };
            tache = timer.scheduleAtFixedRate(compteSeconde, 0,1, TimeUnit.SECONDS);
        }
    }

    void stopTimer() {
        if(timer != null && !timer.isShutdown()) {
            tache.cancel(true);
            timer.shutdown();
        }
    }

    /**
     * GETTER / INFORMATION PART
     * */

    int getHauteur(){
        return this.plateau[0].length;
    }

    int getLargeur(){
        return this.plateau.length;
    }

    String getMode() {
        return this.mode;
    }

    int getLimite() {
        return this.limite;
    }

    double getDebit() {
        return this.debit;
    }

    double getCompteur() {
        return this.compteur;
    }

    Pont getPont(int largeur, int hauteur) {
        return this.plateau[largeur][hauteur].pont;
    }

    boolean isMovable(int x,  int y) {
        Pont p = this.plateau[x][y].pont;
        return (p != null) && p.isMovable();
    }

    private boolean isSortie(int x, int y) {
        return x == this.xSortie && y == this.ySortie;
    }

    private boolean isEntree(int x, int y) {
        return x == this.xEntree && y == this.yEntree;
    }

    /**
     * ACTUALISATION PART
     */

    void decrementeCompteur() {
        this.compteur-=this.debit;
    }

    /**
     * On suppose que l'on tourne les ponts uniquement de 90° ici
     * */
    void tournePont(int x, int y) {
        Pont p = this.plateau[x][y].pont;
        char newOrientation = Pont.getNextOrientation(p.orientation);
        p.setOrientation(newOrientation);
    }

    void resetWater() {
        for(int i = 0; i < this.getLargeur(); i++) {
            for (int j = 0; j < this.getHauteur(); j++ ) {
                Pont p = this.plateau[i][j].pont;
                if (p != null && !p.isEntree()) {
                    p.setEau(false);
                    this.controleur.setEau(i, j, false);
                }
            }
        }
    }

    private void setDebit(double trous) {
        if (trous == 0)
            this.debit = 0;
        else if (this.mode.equals("compteur")) this.debit = 1;
        else {
            if (this.plateau[xSortie][ySortie].pont.getEau())
                this.debit = (Math.pow(2, trous) - 1)/Math.pow(2,trous);
            else
                this.debit = 1;
        }
    }

    /**
     * PARCOURS VICTOIRE PART
     * */

    private static boolean[][] passage;

    private static void createPassage(int largeur, int hauteur) {
        passage = new boolean[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                passage[i][j] = false;
            }
        }
    }

    boolean calculVictoire(){
        int trous = this.isEtanche();
        if(this.getPont(this.xSortie, this.ySortie).getEau())
            return trous == 0;
        else
            return false;
    }

    /**
     * CALCUL ETANCHEITE PART
     *
     * A vérifié: semble marché en fait voir niveau 4
     * Suppose que Sortie est une ligne droite
     * (ne possède que une sortie connectable avec des ponts)
     * cf. checkEtanche... le else de fin
     * */
    private int isEtanche() {
        createPassage(this.getLargeur(), this.getHauteur());
        int x = this.xEntree;
        int y = this.yEntree;
        int trous = this.detectEtancheAdjacents(x, y);
        this.setDebit(trous);
        return trous;
    }

    /**
     * FIXME: A refactor c'est très laid (trop long et decoupé)
     * */
    private int detectEtancheAdjacents(int x, int y) {
        Pont p = this.plateau[x][y].pont;
        boolean[] sortiesP = p.getSorties();
        passage[x][y] = true;
        int sortieEtanche = 0;
        for (int i = 0; i < sortiesP.length; i++) {
            if (sortiesP[i]) {
                sortieEtanche += this.getAdjacentDirection(i, x, y);
            }
        }
        return sortieEtanche;
    }

    private int getAdjacentDirection(int i, int x, int y) {
        switch (i) {
            case 0 : return this.checkEtancheNord(x, y);
            case 1 : return this.checkEtancheEst(x, y);
            case 2 : return this.checkEtancheSud(x, y);
            case 3 : return this.checkEtancheOuest(x, y);
        }
        throw new RuntimeException("Sortie de Pont Inconnue");
    }

    private int checkEtancheNord(int x, int y) {
        if (y-1 >= 0) {
            char sortie = 'N';
            Pont p = this.plateau[x][y-1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (passage[x][y-1]) return 0;
                return this.detectEtancheAdjacents(x, y-1);
            } else
                return 1;
        } else
            return (isSortie(x, y) || isEntree(x, y))? 0 : 1 ;
    }

    private int checkEtancheEst(int x, int y) {
        if (x+1 < this.getLargeur()) {
            char sortie = 'E';
            Pont p = this.plateau[x+1][y].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (passage[x+1][y]) return 0;
                return this.detectEtancheAdjacents(x+1, y);
            } else
                return 1;
        } else
            return (isSortie(x, y) || isEntree(x, y))? 0 : 1 ;
    }

    private int checkEtancheSud(int x, int y) {
        if (y+1 < this.getHauteur()) {
            char sortie = 'S';
            Pont p = this.plateau[x][y+1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (passage[x][y+1]) return 0;
                return this.detectEtancheAdjacents(x, y+1);
            } else
                return 1;
        } else
            return (isSortie(x, y) || isEntree(x, y))? 0 : 1 ;
    }

    private int checkEtancheOuest(int x, int y) {
        if (x-1 >= 0) {
            char sortie = 'O';
            Pont p = this.plateau[x-1][y].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (passage[x-1][y]) return 0;
                return this.detectEtancheAdjacents(x-1, y);
            } else
                return 1;
        } else
            return (isSortie(x, y) || isEntree(x, y))? 0 : 1 ;
    }

    /**
     * EXPORT PART
     * */

    private static String exportDir = "resources/export/niveau";

    void exportNiveau() {
        String chemin = exportDir + this.numNiveau + ".json";
        JSONObject fic = this.createJSON();
        writeFile(fic, chemin);
    }

    private JSONObject createJSON() {
        JSONObject fic = new JSONObject();
        fic.put("num", this.numNiveau);
        fic.put("hauteur", this.getHauteur());
        fic.put("largeur", this.getLargeur());
        fic.put("mode", this.mode);
        fic.put("limite", this.limite);
        fic.put("compteur", this.compteur);
        JSONArray niveau = new JSONArray();
        for(int i = 0; i < this.getLargeur(); i++){
            JSONArray ligne = new JSONArray();
            for(int j = 0; j < this.getHauteur(); j++){
                Pont modPont = this.getPont(i,j);
                if(modPont != null){
                    JSONArray pont = new JSONArray();
                    pont.put((modPont.forme + ""));
                    pont.put(modPont.orientation + "");
                    pont.put(modPont.spe);
                    ligne.put(pont);
                }else{
                    ligne.put((Collection<?>) null);
                }
            }
            niveau.put(ligne);
        }
        fic.put("niveau", niveau);
        return fic;
    }

    private static void writeFile(JSONObject file, String chemin) {
        try {
            FileWriter fichier = new FileWriter(chemin);
            fichier.write(file.toString());
            fichier.close();
        }catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture du fichier exporté");
        }
    }

}
