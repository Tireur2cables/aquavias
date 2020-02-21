import java.awt.*;
import java.awt.image.BufferedImage;

public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        System.out.println("Test de l'affichage d'un pont");
        this.affichePont('I', true, 0);
        System.out.println("Test affichage de niveau");
        this.jeu = new Jeu(this);
        this.jeu.initNiveau(1);
        this.afficheNiveau();
        System.out.println("Le jeu se lance!");
    }

    private void affichePont(char c, boolean eau, double rotation){
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
        this.graph.affichePont(rotate(image, rotation));
    }

    private void afficheNiveau() {
        int hauteur = this.jeu.getHauteur();
        int largeur = this.jeu.getLargeur();
        this.graph.initPlateau(hauteur, largeur);
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                this.graph.addToPlateau(this.getImage(this.jeu.getPont(i,j)));
            }
        }
        this.graph.afficheNiveau();
    }

    /* FIXME: Fonction à factoriser */
    private BufferedImage getImage(Pont p) {
        if (p == null) return PontI.pontI; /* FIXME: A remplacer par une image transparente vide de 200*200 */
        char c = p.getForme();
        boolean eau = p.getEau();
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
        double rotation = 0;
        char orientation = p.getOrientation();
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
        return rotate(image, rotation);
    }

    public static BufferedImage rotate(BufferedImage bimg, double angle) {
        /**fixme renvoit une nouvelle BufferedImage à chaque rotation -> Danger niveau mémoire ? */
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(bimg, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

}
