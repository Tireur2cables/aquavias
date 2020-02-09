import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class View extends JFrame {

    BufferedImage image;

    public View(String titre, BufferedImage image){
        super();
        this.image = image;
        this.setTitle(titre);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setContentPane(new ImagePane());
        this.setVisible(true);
    }

    class ImagePane extends JPanel {

        ImagePane() {
            super();
            this.setPreferredSize(new Dimension(200, 200));

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

}
