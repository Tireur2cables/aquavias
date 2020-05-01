package aquavias.generateur;

import aquavias.jeu.Jeu;

import java.util.concurrent.ThreadLocalRandom;

class GenNiveaux {

    public static void main(String[] args) {
        System.out.println("gen niveau -> WIP cf. branche GenererNiveaux");
        System.out.println("Pour le moment, le niveau est entièrement générer au hasard");
        exportNiveau(chooseDim(), chooseDim(), 15);

    }

    static void exportNiveau(int largeur, int hauteur, int numNiveau) {
        String mode = chooseMode(true);
        int limite = chooseLimite();
        Jeu jeu = new Jeu(numNiveau, mode, limite);
        Plateau p = new Plateau(largeur, hauteur, true, jeu);
        p.exportNiveau();
    }

    private static String chooseMode(boolean flag) {
        return (flag)?"compteur":(ThreadLocalRandom.current().nextBoolean())? "compteur" : "fuite";
    }

    private static int chooseLimite() {
        return 100;
    }

    private static int chooseDim(){
        return ThreadLocalRandom.current().nextInt(3, 10);
    }

}
