import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueGraphique {

    private Controleur controleur;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
    }

    public void affichePont(BufferedImage image, double rotation) {
        View v = new View("Pont", rotate(image, rotation));
    }

    public void affichePlateau() {
        int hauteur = this.controleur.getJeu().getHauteur();
        int largeur = this.controleur.getJeu().getLargeur();
        View v = new View(hauteur, largeur);
    }


    private static String cheminPont(char c, boolean eau) {
        switch (c){
            case 'I':
                return (eau)?("IwO.png"):("InoO.png");
            case 'L':
                return (eau)?("LwO.png"):("LnoO.png");
            case 'T':
                return (eau)?("TwO.png"):("TnoO.png");
            default:
                throw new RuntimeException("Aucun pont correspondant à ce character");
        }
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
