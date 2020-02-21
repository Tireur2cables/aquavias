import java.awt.image.BufferedImage;
public class VueGraphique {

    private Controleur controleur;
    private Plateau plateau;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
    }

    public void affichePont(BufferedImage image) {
        View v = new View("Pont", image);
    }

    public void initPlateau(int hauteur, int largeur) {
        this.plateau = new Plateau(hauteur, largeur);
    }

    public void addToPlateau(BufferedImage image) {
        this.plateau.add(new ImagePane(image));
    }

}
