import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Fenetre extends JFrame {

    public Fenetre(String titre, BufferedImage image) {
        /* Fenetre pour l'affichage unitaire de test */
        super();
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle(titre);
            this.setContentPane(new ImagePane(image));
            this.pack();
            this.setVisible(true);
        });
    }

    public Fenetre() {
        /* Fenetre pour l'affichage du jeu */
        super();
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle("Aquavias");
        });
    }

}

class Plateau extends JPanel {

    public Plateau(int hauteur, int largeur) {
        super();
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
