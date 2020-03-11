import java.awt.*;
import java.awt.image.BufferedImage;

public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        this.jeu = new Jeu(this);
        this.jeu.initNiveau(3);
        this.afficheNiveau();
        System.out.println("Le jeu se lance!");
    }

    private void affichePont(char c, boolean eau, double rotation) {
        BufferedImage image = getImage(c, eau);
        this.graph.affichePont(VueGraphique.rotate(image, rotation));
    }

    /* FIXME: a factoriser */
    private void afficheNiveau() {
        int hauteur = this.jeu.getHauteur();
        int largeur = this.jeu.getLargeur();
        this.graph.initPlateau(largeur, hauteur);
        this.graph.afficheNiveau();
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                Pont p = this.jeu.getPont(i,j);
                boolean movable = (p != null) && p.isMovable();
                this.graph.addToPlateau(this.getImageFromPont(p, i, j), movable, i, j);
            }
        }
        this.graph.repaint();
        this.graph.setVisible();
    }

    private BufferedImage getImageFromPont(Pont p, int x, int y) {
        if (p == null) return Pont.transp;
        char c = p.getForme();
        boolean eau = p.getEau();
        char orientation = p.getOrientation();
        double rotation = getRotation(orientation);
        BufferedImage image = getImage(c, eau);
        return VueGraphique.rotate(image, rotation);
    }

    private static double getRotation(char orientation) {
        double rotation = 0;
        switch (orientation) {
            case 'N' :
                rotation = 0;
                break;
            case 'E' :
                rotation = 90;
                break;
            case 'S' :
                rotation = 180;;
                break;
            case 'O' :
                rotation = 270;;
                break;
        }
        return rotation;
    }

    private static BufferedImage getImage(char c, boolean eau) {
        BufferedImage image = null;
        switch (c) {
            case 'I' :
                image = (eau)? PontI.pontIEau : PontI.pontI;
                break;
            case 'T' :
                image = (eau)? PontT.pontTEau : PontT.pontT;
                break;
            case 'L' :
                image = (eau)? PontL.pontLEau : PontL.pontL;
                break;
        }
        if (image == null) throw new RuntimeException("Affichage du pont incorrect!");
        return image;
    }

    public void refreshSorties(int x, int y) {
        /* change les sorties du pont */
        this.jeu.refreshSorties(x, y);

        /* change l'attribut eau des ponts */
        this.detectAdjacents();
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
                this.graph.actualiseImage(this.actualiseImage(j,i),j,i);
            }
        }
    }

    public void exportNiveau(int number, boolean nouveauNiveau){
        this.jeu.exportNiveau(number, nouveauNiveau);
    }

    void isVictoire(){
        if (this.jeu.calculVictoire()) {
            this.graph.victoire();
        }
    }

}
