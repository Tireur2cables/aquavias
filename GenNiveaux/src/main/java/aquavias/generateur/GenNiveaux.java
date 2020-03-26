package aquavias.generateur;

import aquavias.jeu.Jeu;

import java.util.concurrent.ThreadLocalRandom;

class GenNiveaux {

    public static void main(String[] args) {
        System.out.println("je suis gen niveau");
    }

    private void exportNiveau(int largeur, int hauteur, int numNiveau) {
        Plateau p = new Plateau(largeur, hauteur);
        String mode = this.chooseMode();
        int limite = this.chooseLimite();
        Jeu jeu = new Jeu(p.getPlateau(), numNiveau, mode, limite);
        jeu.exportNiveau();
    }

    private String chooseMode() {
        return (ThreadLocalRandom.current().nextBoolean())? "compteur" : "fuite";
    }

    private int chooseLimite() {
        return 100;
    }

}
