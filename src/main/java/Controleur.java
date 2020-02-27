import java.awt.*;
import java.awt.image.BufferedImage;

public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        System.out.println("Test affichage de niveau");
        this.jeu = new Jeu(this);
        this.jeu.initNiveau(3);
        this.afficheNiveau();
        System.out.println("Le jeu se lance!");
    }

    private void affichePont(char c, boolean eau, double rotation){
        BufferedImage image = getImage(c, eau);
        this.graph.affichePont(this.rotate(image, rotation, 0, 0, true));
    }

    private void afficheNiveau() {
        int hauteur = this.jeu.getHauteur();
        int largeur = this.jeu.getLargeur();
        this.graph.initPlateau(largeur, hauteur);
        this.graph.afficheNiveau();
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                Pont p = this.jeu.getPont(i,j);
                boolean movable = (p != null) && p.isMovable();
                this.graph.addToPlateau(this.getImageFromPont(p, i, j, false), movable, i, j);
            }
        }
        this.graph.repaint();
        this.graph.setVisible();
    }

    private BufferedImage getImageFromPont(Pont p, int x, int y, boolean refresh) {
        if (p == null) return Pont.transp;
        char c = p.getForme();
        boolean eau = p.getEau();
        char orientation = p.getOrientation();
        double rotation = getRotation(orientation);
        BufferedImage image = getImage(c, eau);
        return this.rotate(image, rotation, x, y, refresh);
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

    public BufferedImage rotate(BufferedImage bimg, double angle, int x, int y, boolean refresh /* True lorsqu'on tourne le pont */) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(bimg, null, 0, 0);
        graphic.dispose();

        /* tentative de libération de la mémoire */
        bimg = null;
        System.gc();

        /* Actualisation des sorties du pont */
        if (refresh) this.jeu.refreshSorties(x, y);

        return rotated;
    }

    public void detectAdjacents(int x, int y) {
        this.jeu.detectAdjacents(x, y, false);
    }

    public BufferedImage actualiseImage(int x, int y) {
        Pont p = this.jeu.getPont(x, y);
        return this.getImageFromPont(p, x, y, false);
    }

}
