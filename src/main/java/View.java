import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class View extends JFrame {

    public View(String titre, BufferedImage image){
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(titre);

        this.setContentPane(new ImagePane(image));
        this.pack();
        this.setVisible(true);
    }

}

class ImagePane extends JPanel {

     private BufferedImage image;
     private int width;
     private int height;

    ImagePane(BufferedImage image) {
        super();
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setPreferredSize(new Dimension(this.width, this.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this);
    }
}
