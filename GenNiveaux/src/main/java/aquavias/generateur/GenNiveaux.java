package aquavias.generateur;

import aquavias.jeu.Jeu;
import aquavias.jeu.Pont;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/* Import with maven dependencies */
import org.json.JSONArray;
import org.json.JSONObject;

class GenNiveaux {

    public static void main(String[] args) {
        System.out.println("je suis gen niveau");
    }

    private Jeu createJeu(int largeur, int hauteur) {
        Plateau p = new Plateau(largeur, hauteur);
        return new Jeu();
    }

}
