import java.awt.*;
import java.awt.image.BufferedImage;
public class VueGraphique {

    private Controleur controleur;
    private Fenetre fenetre;
    private Plateau plateau;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.fenetre = new Fenetre();

    }

    public void setVisible() {
        this.fenetre.setVisible(true);
    }

    public void affichePont(BufferedImage image) {
        EventQueue.invokeLater(() -> new Fenetre("Pont", image));
    }

    public void initPlateau(int hauteur, int largeur) {
        this.plateau = new Plateau(hauteur, largeur);
    }

    public void addToPlateau(BufferedImage image) {
        this.plateau.add(new ImagePane(image));
    }

    public void afficheNiveau() {
        EventQueue.invokeLater(() -> {
            this.fenetre.setContentPane(this.plateau);
            this.fenetre.pack();
        });
    }

}
