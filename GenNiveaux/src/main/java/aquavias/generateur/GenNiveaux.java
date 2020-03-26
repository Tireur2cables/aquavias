package aquavias.generateur;

import aquavias.jeu.Jeu;
import aquavias.jeu.Pont;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/* Import with maven dependencies */
import org.json.JSONArray;
import org.json.JSONObject;

class GenNiveaux {

    public static void main(String[] args) {
        System.out.println("je suis gen niveau");
    }

    private Jeu createJeu(int largeur, int hauteur, int numNiveau) {
        Plateau p = new Plateau(largeur, hauteur);
        String mode = this.chooseMode();
        int limite = this.chooseLimite();
        return new Jeu(p.getPlateau(), numNiveau, mode, limite);
    }

    private String chooseMode() {
        return (ThreadLocalRandom.current().nextBoolean())? "compteur" : "fuite";
    }

    private int chooseLimite() {
        return 100;
    }

}
