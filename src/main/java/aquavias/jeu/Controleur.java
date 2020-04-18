package aquavias.jeu;

import java.io.File;
import java.util.ArrayList;

class Controleur {

    private final Jeu jeu;
    private final VueGraphique graph;

    /**
     * INIT PART
     */
    Controleur() {
        this.jeu = new Jeu(this);
        this.graph = new VueGraphique(this);
    }

    void launch() {
        this.mainMenu();
        System.out.println("Le jeu se lance!");
    }

    /**
     * EXIT PART
     */

    void exit() {
        this.stopTimer();
        System.out.println("Le jeu s'arrète!");
        System.exit(0);
    }

    /**
     * REQUETTE PART
     * */

    void tournePont(int x, int y) {
        /* change les sorties du pont et l'orientation */
        this.jeu.tournePont(x, y);

        /* change l'attribut eau des ponts */
        this.detectEauAdjacents();

        /* en mode compteur incrémente le compteur */
        if (this.jeu.getMode().equals("compteur"))
            this.decrementeCompteur();

        /* verifie si c'est gagné */
        if (!this.isVictoire()) {
            /* en mode compteur verifie la defaite à chaque pont tourné */
            if (this.jeu.getMode().equals("compteur") && this.jeu.getCompteur() <= 0)
                this.defaite();
        }
    }

    void detectEauAdjacents() {
        this.jeu.resetWater();
        this.jeu.parcourchemin();
    }

    void decrementeCompteur() {
        this.jeu.decrementeCompteur();
        if (this.jeu.getMode().equals("compteur"))
            this.graph.decrementeCompteur();
        else if (this.jeu.getMode().equals("fuite"))
            this.graph.decrementeProgressBar();
        /* en mode fuite vérifie la défaite à chaque décrémentation */
        if (this.jeu.getMode().equals("fuite") && this.jeu.getCompteur() <= 0)
            this.defaite();
    }

    void initTimer() {
        this.jeu.initTimer();
    }

    private void stopTimer() {
        this.jeu.stopTimer();
    }

    private boolean isVictoire() {
        if (this.jeu.calculVictoire()) {
            this.stopTimer();
            this.graph.victoire();
            return true;
        }
        return false;
    }

    private void endGame() {
        //Ajouter le générique ici!
        this.stopTimer();
        this.graph.infoRetourMenu("Vous êtes arrivé au dernier niveau ! Bien joué !");
    }

    private void defaite() {
        this.jeu.stopTimer();
        this.graph.defaite();
    }

    void retry() {
        int numNiveau = this.jeu.getNumNiveau();
        this.chargeNiveau(numNiveau);
    }

    void mainMenu(){
        this.graph.chargeMenu();
        this.stopTimer();
    }

    void chargeNiveau(int num) {
        this.stopTimer();
        this.jeu.initNiveau(num);
        this.graph.afficheNiveau();
    }

    void nextLevel() {
        int numNiveau = this.jeu.getNumNiveau();
        if(numNiveau < getNombreNiveaux())
            this.chargeNiveau(numNiveau + 1);
        else
            this.endGame();
    }

    int getNombreNiveaux() {
        return Jeu.getListNiveau().size();
    }

    static ArrayList<File> getListNiveau(){
       return Jeu.getListNiveau();
    }

    Pont getPont(int x, int y) {
        return this.jeu.getPont(x, y);
    }

    int getHauteur() {
        return this.jeu.getHauteur();
    }

    int getLargeur() {
        return this.jeu.getLargeur();
    }

    boolean isMovable(int x, int y) {
        return this.jeu.isMovable(x, y);
    }

    String getMode() {
        return this.jeu.getMode();
    }

    int getLimite() {
        return this.jeu.getLimite();
    }

    double getCompteur() {
        return this.jeu.getCompteur();
    }

    double getDebit() {
        return this.jeu.getDebit();
    }

    void exportNiveau() {
        this.jeu.exportNiveau();
    }

}
