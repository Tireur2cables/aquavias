package aquavias.generateur;

import aquavias.jeu.Jeu;

import java.util.concurrent.ThreadLocalRandom;

public class GenNiveaux {

    public static void main(String[] args) {
        int nbNiveaux;
        if (args.length == 1) {
            try {
                nbNiveaux = Integer.parseInt(args[0]);
            }catch (NumberFormatException e) {
                nbNiveaux = 1;
            }
        }else {
            nbNiveaux = 1;
        }
        int numNiveau = Jeu.getNbNiveaux();
        for (int i = 0; i < nbNiveaux; i++) {
            createNiveau(chooseDim(), chooseDim(), ++numNiveau);
        }
    }

    private static void createNiveau(int largeur, int hauteur, int numNiveau) {
        String mode = chooseMode();
        Plateau p = new Plateau(largeur, hauteur, true, numNiveau, mode, true);
        p.exportNiveau();
    }

    private static String chooseMode() {
        return (ThreadLocalRandom.current().nextBoolean())? "compteur" : "fuite";
    }

    private static int chooseDim(){
        return ThreadLocalRandom.current().nextInt(3, 11);
    }

}
