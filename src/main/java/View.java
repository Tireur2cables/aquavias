import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

class Fenetre extends JFrame {

    public Fenetre(String titre, BufferedImage image) {
        /* Fenetre pour l'affichage unitaire de test */
        super();
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle(titre);
            this.setContentPane(new ImagePane(image, true));
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
            this.setVisible(false);
        });
    }

}

class Plateau extends JPanel {

    public Plateau(int hauteur, int largeur) {
        super();
        EventQueue.invokeLater(() -> {
            this.setLayout(new GridLayout(largeur, hauteur));
            this.setPreferredSize(new Dimension(hauteur*200,largeur*200));
        });
    }
}

class ImagePane extends JPanel {

     private BufferedImage image;
     private int width;
     private int height;
     private boolean movable;

    ImagePane(BufferedImage image, boolean movable) {
        super();
        EventQueue.invokeLater(() -> {
            this.image = image;
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.setPreferredSize(new Dimension(this.width, this.height));
        });

        EventQueue.invokeLater(() -> {
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (movable) {
                        rotateImage();
                        repaint();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) { }

                @Override
                public void mouseReleased(MouseEvent e) { }

                @Override
                public void mouseEntered(MouseEvent e) { }

                @Override
                public void mouseExited(MouseEvent e) { }
            });
        });

    }

    private void rotateImage() {
        this.image = Controleur.rotate(this.image, 90);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this);
    }
}
