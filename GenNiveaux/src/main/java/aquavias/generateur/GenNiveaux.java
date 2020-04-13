package aquavias.generateur;

import aquavias.jeu.Jeu;

import java.util.concurrent.ThreadLocalRandom;

class GenNiveaux {

    public static void main(String[] args) {
        System.out.println("gen niveau -> WIP cf. branche GenererNiveaux");
        System.out.println("Pour le moment, le niveau est entièrement générer au hasard");
        exportNiveau(chooseDim(), chooseDim(), 15);

    }

    private static void exportNiveau(int largeur, int hauteur, int numNiveau) {
        Plateau p = new Plateau(largeur, hauteur);
        String mode = chooseMode(true);
        int limite = chooseLimite();
        Jeu jeu = new Jeu(p.getPlateau(), numNiveau, mode, limite);
        jeu.exportNiveau();
    }

    private static String chooseMode(boolean flag) {
        return (flag)?"compteur":(ThreadLocalRandom.current().nextBoolean())? "compteur" : "fuite";
    }

    private static int chooseLimite() {
        return 100;
    }

    private static int chooseDim(){
        return 4;
        //return ThreadLocalRandom.current().nextInt(3, 10);
    }

}
