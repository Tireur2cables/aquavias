import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class View extends JFrame {

    public View(String titre, BufferedImage image) {
        /*Interface de l'affichage des exemples de ponts*/
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(titre);

        this.setContentPane(new ImagePane(image));
        this.pack();
        this.setVisible(true);
    }

    public View(int hauteur, int largeur) {
        /*Interface du jeu*/
        /**
         * Taille du plateau
         * Entree / Sortie
         *
         * */
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Aquavias");
        this.add(new Plateau(hauteur, largeur));
        this.pack();
        this.setVisible(true);

    }

}

class Plateau extends JPanel {

    public Plateau(int hauteur, int largeur) {
        EventQueue.invokeLater(() -> {
            this.setLayout(new GridLayout(largeur, hauteur));
        });
    }
}

class ImagePane extends JPanel {

     private BufferedImage image;
     private int width;
     private int height;

    ImagePane(BufferedImage image) {
        super();
        EventQueue.invokeLater(() -> {
            this.image = image;
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.setPreferredSize(new Dimension(this.width, this.height));
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        EventQueue.invokeLater(() -> {
            super.paintComponent(g);
            g.drawImage(this.image, 0, 0, this);
        });
    }
}
