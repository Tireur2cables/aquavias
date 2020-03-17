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
        this.jeu.initNiveau(5);
        this.graph.afficheNiveau();
        System.out.println("Le jeu se lance!");
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
        if (this.jeu.getMode().equals("compteur") && !this.jeu.calculVictoire())
            this.decrementeCompteur();
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
    }

    void initTimer() {
        this.jeu.initTimer();
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

    /**
     * WIP
     */
    void retry() {
        System.out.println("Réessayer");
    }

    /**
     * WIP
     */
    void backMenu() {
        System.out.println("Retour Menu");
    }

    /**
     * WIP
     */
    void nextLevel() {
        System.out.println("Niveau suivant");
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

    void setEau(int x, int y, boolean eau) {
        this.graph.setEau(x, y ,eau);
    }

    void exportNiveau(int number, boolean nouveauNiveau) {
        this.jeu.exportNiveau(number, nouveauNiveau);
    }

}
