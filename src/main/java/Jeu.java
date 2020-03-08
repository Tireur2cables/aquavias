import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/* Imports with maven dependecies */
import org.apache.commons.io.FileUtils;
import org.json.*;

public class Jeu {

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

    private Case[][] plateau;
    private Controleur controleur;
    private int xEntree;
    private int yEntree;

    public Jeu(Controleur controleur) {
        this.controleur = controleur;
    }

    /**
     * Avec cette méthode d'affichage les colonnes sont affichées en premières pour chaque lignes
     * donc on échange les indices i et j pour les afficher correctement
     * */
    public void afficher() {
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

    /* FIXME: chemin en parametre static ? */
    public void initNiveau(int number) {
        String chemin = "resources/niveaux/niveau" + number + ".json";
        JSONObject json = readJSON(chemin);
        int hauteur = json.getInt("hauteur");
        int longueur = json.getInt("longueur");
        JSONArray niveau = json.getJSONArray("niveau");
        this.initPlateau(longueur, hauteur, niveau);
        this.chercheEntree();
        this.parcourchemin();
    }

    /* FIXME: à factoriser */
    public void exportNiveau(int number){
        String chemin = "resources/export/niveau" + number + ".json";
        JSONObject fic = new JSONObject();
        fic.put("hauteur", this.getHauteur());
        fic.put("longueur", this.getLargeur());
        JSONArray niveau = new JSONArray();
        for(int i = 0; i < this.getLargeur(); i++){
            JSONArray ligne = new JSONArray();
            for(int j = 0; j < this.getHauteur(); j++){
                Pont modPont = this.getPont(j,i);
                if(modPont != null){
                    JSONArray pont = new JSONArray();
                    pont.put(((char)modPont.forme + ""));
                    pont.put((char)modPont.orientation + "");
                    pont.put(modPont.spe);
                    ligne.put(pont);
                }else{
                    ligne.put((Collection<?>) null);
                }
            }
            niveau.put(ligne);
        }
        fic.put("niveau", niveau);
        try{
            FileWriter fichier = new FileWriter(chemin);
            fichier.write(fic.toString());
            fichier.close();
        }catch (IOException e){
            throw new RuntimeException("Impossible de sauvegarder le niveau en cours.");
        }
    }

    private void initPlateau(int longueur, int hauteur, JSONArray niveau) {
        this.plateau = new Case[longueur][hauteur];
        for (int i = 0; i < longueur; i++) {
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
                    this.xEntree = j;
                    this.yEntree = i;
                    return;
                }
            }
        }
    }

    private static JSONObject readJSON(String chemin) {
        try {
            File f = new File(chemin);
            return new JSONObject(FileUtils.readFileToString(f, "utf-8"));
        } catch (NullPointerException n) {
            System.out.println("Impossible de trouver le fichier de niveau à l'adresse : " + chemin);
        } catch (IOException i) {
            System.out.println("Impossible de lire le fichier niveau à l'adresse : " + chemin);
        }
        throw new RuntimeException("Le chargement du fichier de niveau a échoué!");
    }

    public Pont getPont(int hauteur, int largeur) {
        return this.plateau[largeur][hauteur].pont;
    }

    public int getHauteur(){
        return this.plateau[0].length;
    }

    public int getLargeur(){
        return this.plateau.length;
    }

    /**
     * On suppose que l'on tourne les ponts uniquement de 90° ici
     * */
    public void refreshSorties(int x, int y) {
        Pont p = this.plateau[y][x].pont;
        char newOrientation = Pont.getNextOrientation(p.orientation);
        p.setOrientation(newOrientation);
    }

    /**
     * On parcours toutes les sorties d'un premier morceau de pont (x,y) et on suit le chemin selon ses sorties
     * */
    public void detectAdjacents(int x, int y) {
        Pont p = this.plateau[y][x].pont;
        boolean[] sortiesP = p.getSorties();
        for (int i = 0; i < sortiesP.length; i++) {
            if (sortiesP[i]){
                this.afficheAdja(i, x, y);
            }
        }
    }

    /**
     *  X = hauteur et Y = largeur
     *  Selon l'entier i donné (0-NORD - 1-EST - 2-SUD - 3-OUEST) on vérifie le voisin dans la direction i
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
        if (x-1 >= 0) {
            char sortie = 'N';
            Pont p = this.plateau[y][x-1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x-1, y);
                }
            }
        }
    }

    private void checkAdjaEst(int x, int y) {
        if (y+1 < this.getLargeur()) {
            char sortie = 'E';
            Pont p = this.plateau[y+1][x].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x, y+1);
                }
            }
        }
    }

    private void checkAdjaSud(int x, int y) {
        if (x+1 < this.getHauteur()) {
            char sortie = 'S';
            Pont p = this.plateau[y][x+1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x+1, y);
                }
            }
        }
    }

    private void checkAdjaOuest(int x, int y) {
        if (y-1 >= 0) {
            char sortie = 'O';
            Pont p = this.plateau[y-1][x].pont;
            if (p != null && p.isAccessibleFrom(sortie)) {
                if (!p.getEau()) {
                    p.setEau(true);
                    this.detectAdjacents(x, y-1);
                }
            }

        }
    }

    /**
     * Parcours récursif de chaque chemin complet
     * */
    void parcourchemin() {
        int x = this.xEntree;
        int y = this.yEntree;
        this.detectAdjacents(x, y);
    }

     void resetWater() {
        for(int i = 0; i < this.getLargeur(); i++) {
            for (int j = 0; j < this.getHauteur(); j++ ) {
                Pont p = this.plateau[i][j].pont;
                if (p != null && !p.isEntree())
                    p.setEau(false);
            }
        }
    }

}
