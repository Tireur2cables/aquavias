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
        this.importListeNiveauTermine();
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
    //FIXME: ici
    void tournePont(int x, int y) {
        /* change les sorties du pont et l'orientation */
        this.jeu.tournePont(x, y);
    }

    //FIXME: ici
    void decrementeCompteur() {
        boolean victory = true;
        this.jeu.decrementeCompteur();
        if (this.jeu.getMode().equals("compteur")) {
            this.graph.decrementeCompteur();
            victory = this.isVictoire();
        }else if (this.jeu.getMode().equals("fuite"))
            this.graph.decrementeProgressBar();
        if (!victory) this.isDefaite();
    }

    void initTimer() {
        this.jeu.initTimer();
    }

    private void stopTimer() {
        this.jeu.stopTimer();
    }

    private void isDefaite() {
        if (this.jeu.getCompteur() <= 0)
            this.defaite();
    }

    boolean isVictoire() {
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
        if(this.jeu.niveauDejaTermine(num)){
            this.graph.infoOk("Vous avez déja terminé ce niveau !");
        }
        this.graph.afficheNiveau();
    }

    void nextLevel() {
        int numNiveau = this.jeu.getNumNiveau();
        if(numNiveau < getNombreNiveaux())
            this.chargeNiveau(numNiveau + 1);
        else
            this.endGame();
    }

    void ajoutListeNiveauTermine(){
        this.jeu.ajoutListeNiveauTermine();
    }

    void clearListeNiveauTermine(){
        this.jeu.clearListeNiveauTermine();
    }

    void saveListeNiveauTermine(){
        this.jeu.saveListeNiveauTermine();
    }

    void importListeNiveauTermine(){
        this.jeu.importListeNiveauTermine();
    }

    int getNombreNiveaux() {
        return Jeu.getListNiveau().size();
    }

    int getNiveauCourant(){ return this.jeu.getNumNiveau(); }

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

    void exportNiveau(boolean isSave) {
        this.jeu.exportNiveau(isSave);
    }

}
