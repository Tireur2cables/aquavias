import java.io.File;
import java.io.IOException;

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

    public Jeu(Controleur controleur) {
        this.controleur = controleur;
    }

    /* Avec cette méthode d'affichage les colonnes sont affichées en premières pour chaque lignes donc on échange les indices pour les afficher correctement */
    public void afficher() {
        System.out.println("Test affichage terminal du niveau");
        if (this.plateau.length <= 0) return;
        for (int i = 0; i < this.plateau[0].length; i++) {
            for (Case[] cases : this.plateau) {
                if (cases[i].pont == null) System.out.print("- ");
                else System.out.print(cases[i].pont.getForme() + " ");
            }
            System.out.println();
        }
    }

    public void initNiveau(int number) {
        String chemin = "resources/niveaux/niveau" + number + ".json";
        JSONObject json = readJSON(chemin);
        int hauteur = json.getInt("hauteur");
        int longueur = json.getInt("longueur");
        JSONArray niveau = json.getJSONArray("niveau");
        this.initPlateau(longueur, hauteur, niveau);
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

    public Case[][] getPlateau() {
        return this.plateau;
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

    /* On suppose que l'on tourne les ponts uniquement de 90° ici */
    public void refreshSorties(int x, int y) {
        Pont p = this.plateau[y][x].pont;
        char newOrientation = Pont.getNextOrientation(p.orientation);
        p.setOrientation(newOrientation);
    }

    public void detectAdjacents(int x, int y) {
        Pont p = this.plateau[y][x].pont;
        boolean[] sortiesP = p.getSorties();
        for (int i = 0; i < sortiesP.length; i++) {
            if (sortiesP[i]){
                this.afficheAdja(i, x, y);
            }
        }
    }

    /* X = hauteur et Y = largeur */
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
            if (p != null && p.isAccessibleFrom(sortie))
                System.out.println(p.forme + " : " + sortie);
        }
    }

    private void checkAdjaEst(int x, int y) {
        if (y+1 < this.getLargeur()) {
            char sortie = 'E';
            Pont p = this.plateau[y+1][x].pont;
            if (p != null && p.isAccessibleFrom(sortie)) System.out.println(p.forme + " : " + sortie);
        }
    }

    private void checkAdjaSud(int x, int y) {
        if (x+1 < this.getHauteur()) {
            char sortie = 'S';
            Pont p = this.plateau[y][x+1].pont;
            if (p != null && p.isAccessibleFrom(sortie)) System.out.println(p.forme + " : " + sortie);
        }
    }

    private void checkAdjaOuest(int x, int y) {
        if (y-1 >= 0) {
            char sortie = 'O';
            Pont p = this.plateau[y-1][x].pont;
            if (p != null && p.isAccessibleFrom(sortie)) System.out.println(p.forme + " : " + sortie);
        }
    }

}
