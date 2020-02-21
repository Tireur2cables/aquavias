import javax.swing.*;
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
    public void repaint(){
        this.fenetre.pack();
        this.fenetre.repaint();
    }

    public void affichePont(BufferedImage image) {
        EventQueue.invokeLater(() -> new Fenetre("Pont", image));
    }

    public void initPlateau(int hauteur, int largeur) {
        this.plateau = new Plateau(hauteur, largeur);
    }

    public void afficheNiveau() {
        EventQueue.invokeLater(() -> {
            this.fenetre.setContentPane(this.plateau);
        });
    }

    public void addToPlateau(BufferedImage image) {
        EventQueue.invokeLater(() -> {
           /* JPanel tmp = new JPanel();
            tmp.setPreferredSize(new Dimension(200,200));
            tmp.setBackground(new Color((int) (Math.random()*100), 125, 5));
            this.plateau.add(tmp);*/
            this.plateau.add(new ImagePane(image));
        });
    }
}
