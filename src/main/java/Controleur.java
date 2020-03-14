import java.awt.image.BufferedImage;

public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.jeu = new Jeu(this);
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        this.jeu.initNiveau(4);
        this.graph.afficheNiveau();
        System.out.println("Le jeu se lance!");
    }

    private void affichePont(char c, boolean eau, int num) {
        BufferedImage image = getImage(c, eau, num);
        this.graph.affichePont(image);
    }

    /* FIXME: A deplacer dans une autre classe */
    private BufferedImage getImageFromPont(Pont p, int x, int y) {
        if (p == null) return PontGraph.transp;
        char c = p.getForme();
        boolean eau = p.getEau();
        char orientation = p.getOrientation();
        int num = getNumFromOrientation(orientation);
        return getImage(c, eau, num);
    }

    private static int getNumFromOrientation(char orientation) {
        switch (orientation) {
            case 'N' : return 0;
            case 'E' : return 1;
            case 'S' : return 2;
            case 'O' : return 3;
        }
        throw new RuntimeException("Orientation inconnue");
    }

    private static BufferedImage getImage(char c, boolean eau, int num) {
        BufferedImage image = null;
        switch (c) {
            case 'I' :
                image = (eau)? PontIGraph.pontIEau[num] : PontIGraph.pontI[num];
                break;
            case 'T' :
                image = (eau)? PontTGraph.pontTEau[num] : PontTGraph.pontT[num];
                break;
            case 'L' :
                image = (eau)? PontLGraph.pontLEau[num] : PontLGraph.pontL[num];
                break;
        }
        if (image == null) throw new RuntimeException("Affichage du pont incorrect!");
        return image;
    }

    Pont getPont(int x, int y){
        return this.jeu.getPont(x, y);
    }

    int getHauteur(){
        return this.jeu.getHauteur();
    }

    int getLargeur(){
        return this.jeu.getLargeur();
    }

    public void refreshSorties(int x, int y) {
        /* change les sorties du pont */
        this.jeu.refreshSorties(x, y);

        /* change l'attribut eau des ponts */
        this.detectAdjacents();

        /* en mode compteur incrémente le compteur */
        if (this.jeu.getMode().equals("compteur") && !this.jeu.calculVictoire())
            this.decrementeCompteur();
    }

    void decrementeCompteur() {
        this.jeu.decrementeCompteur();
        if (this.jeu.getMode().equals("compteur"))
            this.graph.decrementeCompteur();
        else
            this.graph.decrementeProgressBar();
    }

    boolean isMovable(int x, int y){
        return this.jeu.isMovable(x, y);
    }

    public void detectAdjacents() {
        this.jeu.resetWater();
        this.jeu.parcourchemin();
    }

    private BufferedImage actualiseImage(int x, int y) {
        Pont p = this.jeu.getPont(x, y);
        return this.getImageFromPont(p, x, y);
    }

    public void actualiseAllImages() {
        for (int i = 0; i < this.jeu.getLargeur(); i++) {
            for (int j = 0; j < this.jeu.getHauteur(); j++) {
                this.graph.actualiseImage(this.actualiseImage(i,j),j,i);
            }
        }
    }

    public void exportNiveau(int number, boolean nouveauNiveau) {
        this.jeu.exportNiveau(number, nouveauNiveau);
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
        System.out.println("Defaite!");
        this.graph.defaite();
        this.jeu.stopTimer();
    }

    String getMode() {
        return this.jeu.getMode();
    }

    int getLimite() {
        return this.jeu.getLimite();
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

}
