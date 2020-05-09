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
        this.importListeNiveauTermine();
        if (!this.jeu.niveauDejaTermine(-1))
            this.tutoriel(-1);
        else if (!this.jeu.niveauDejaTermine(0))
            this.tutoriel(0);
        else
            this.mainMenu();
    }

    /**
     * EXIT PART
     */

    void exit() {
        this.stopTimer();
        System.exit(0);
    }

    /**
     * REQUETTE PART
     * */

    void tournePont(int x, int y) {
        /* change les sorties du pont et l'orientation */
        this.jeu.tournePont(x, y);
    }

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

    void endGame() {
        this.stopTimer();
        //FIXME : Ajouter le générique ici! a la place du menu
        this.mainMenu();
    }

    private void defaite() {
        this.jeu.stopTimer();
        this.graph.defaite();
    }

    void retry() {
        int numNiveau = this.jeu.getNumNiveau();
        this.chargeNiveau(numNiveau);
    }

    void tutoriel(int numTuto) {
        this.chargeNiveau(numTuto);
    }

    void mainMenu() {
        this.stopTimer();
        this.graph.chargeMenu();
    }

    void chargeNiveau(int num) {
        this.stopTimer();
        this.jeu.initNiveau(num, false);
        if (num > 0)
            this.graph.afficheNiveau();
        else
            this.graph.afficheTuto(num);
        if(this.jeu.niveauDejaTermine(num)) {
            this.graph.infoOk("Vous avez déja terminé ce niveau !");
        }
    }

    void chargeNiveau() {
        this.stopTimer();
        this.jeu.initNiveau(Jeu.getNumSauvegarde(), true);
        this.graph.afficheNiveau();
    }

    void nextLevel() {
        int numNiveau = this.jeu.getNumNiveau();
        if(this.existeNiveauSuivant())
            this.chargeNiveau(numNiveau + 1);
        else
            this.endGame();
    }

    boolean existeNiveauSuivant() {
        return this.jeu.getNumNiveau() < this.getNombreNiveaux();
    }

    int getNumNiveau() {
        return this.jeu.getNumNiveau();
    }

    void ajoutListeNiveauTermine() {
        this.jeu.ajoutListeNiveauTermine();
    }

    void clearListeNiveauTermine() {
        this.jeu.clearListeNiveauTermine();
    }

    void saveListeNiveauTermine() {
        this.jeu.saveListeNiveauTermine();
    }

    void importListeNiveauTermine() {
        this.jeu.importListeNiveauTermine();
    }

    boolean niveauDejaTermine(int num) {
        return this.jeu.niveauDejaTermine(num);
    }

    int getNombreNiveaux() {
        return Jeu.getListNiveau().size();
    }

    int getNiveauCourant(){
        return this.jeu.getNumNiveau();
    }

    static ArrayList<File> getListNiveau() {
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

    void exportNiveauSuivant(int numNiveau) { //Cas ou on doit copier le niveau suivant
        this.jeu.exportNiveauSuivant(numNiveau);
    }

    boolean existeUneSauvegarde() {
        return Jeu.existeUneSauvegarde();
    }

    void supprimerSauvegarde() {
        if(Jeu.existeUneSauvegarde()) {
            this.jeu.supprimerSauvegarde();
        }
    }

    static void setPlayable(boolean playable) {
        Jeu.playable = playable;
    }

}
