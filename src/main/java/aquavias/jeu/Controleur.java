package aquavias.jeu;

class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

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

    void mainMenu(){
        this.graph.chargeMenu();
    }

    void chargeNiveau(int num){
        this.stopTimer();
        this.jeu.initNiveau(num);
        this.graph.afficheNiveau();
        System.out.println("Le niveau " + num + " se lance!");
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
        this.isVictoire();

        /* en mode compteur verifie la defaite à chaque pont tourné */
        if (this.jeu.getMode().equals("compteur") && this.jeu.getCompteur() <= 0)
            this.defaite();
    }

    void detectEauAdjacents() {
        this.jeu.resetWater();
        this.jeu.parcourchemin();
    }

    void decrementeCompteur() {
        this.jeu.decrementeCompteur();
        if (this.jeu.getMode().equals("compteur"))
            this.graph.decrementeCompteur();
        else
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

    void isVictoire() {
        if (this.jeu.calculVictoire()) {
            this.graph.victoire();
            this.jeu.stopTimer();
        }
    }

    void defaite() {
        this.graph.defaite();
        this.jeu.stopTimer();
    }

    void retry() {
        int numNiveau = this.jeu.getNumNiveau();
        this.chargeNiveau(numNiveau);
    }

    void backMenu() {
        this.graph.chargeMenu();
    }

    void nextLevel() {
        int numNiveau = this.jeu.getNumNiveau();
        if(numNiveau < getNombreNiveaux()) {
            this.chargeNiveau(numNiveau + 1);
            this.graph.niveauCharge(String.valueOf(numNiveau+1)); /* utile ? */
        }else {
            this.graph.infoRetourMenu("Vous êtes arrivé au dernier niveau ! Bien joué !");
        }
    }

    int getNombreNiveaux() {
        return Accueil.getListNiveau().size();
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

    void setEau(int x, int y, boolean eau) {
        this.graph.setEau(x, y ,eau);
    }

    void exportNiveau() {
        this.jeu.exportNiveau();
    }

}
