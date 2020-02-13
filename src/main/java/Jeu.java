import java.io.File;
import java.io.IOException;

/* Imports with maven dependecies */
import org.apache.commons.io.FileUtils;
import org.json.*;

public class Jeu {

    private class Case{

        private Pont pont;

        private Case(int ligne, JSONArray json){
            JSONArray tab = ((JSONArray) json.get(ligne));
            this.pont = (tab.length() <= 0)? null : new Pont(tab);
        }

    }

    private Case[][] plateau;
    private Controller controller;


    public Jeu(Controller controller){
        this.controller = controller;
        this.initNiveau();
        this.afficher();
    }

    /* Avec cette méthode d'affichage les colonnes sont affichées en premières pour chaque lignes donc on échange les indices pour les affichées correctement */
    public void afficher(){
        if (this.plateau.length <= 0) return;
        for (int i = 0; i < this.plateau[0].length; i++) {
            for (int j = 0; j < this.plateau.length; j++) {
                if (this.plateau[j][i].pont == null) System.out.print("- ");
                else System.out.print(this.plateau[j][i].pont.getOrientation() + " ");
            }
            System.out.println();
        }
    }

    private void initNiveau(){
        String chemin = "resources/niveau1.json";
        JSONObject json = readJSON(chemin);
        int hauteur = json.getInt("hauteur");
        int longueur = json.getInt("longueur");
        JSONArray niveau = json.getJSONArray("niveau");
        this.initPlateau(longueur, hauteur, niveau);
    }

    private void initPlateau(int longueur, int hauteur, JSONArray niveau){
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

}
