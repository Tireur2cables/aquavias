import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PontGraph{

    static BufferedImage transp = chargeImage("transp.png");
    private int orientation;

    PontGraph(char orientation) {
        this.orientation = getOrientationFromChar(orientation);
    }

    private static int getOrientationFromChar(char orientation) {
        switch (orientation){
            case 'N': return 0;
            case 'E': return 1;
            case 'S': return 2;
            case 'O': return 3;
        }
        throw new RuntimeException("Orientation inconnue : " + orientation);
    }

    private void getImage() {
        if (this instanceof PontIGraph) this.sorties = ((PontI) this).calculSorties();
        else if (this instanceof PontLGraph) this.sorties = ((PontL) this).calculSorties();
        else if (this instanceof PontTGraph) this.sorties = ((PontT) this).calculSorties();
    }

    static BufferedImage[] chargeImages(String chemin) {
        BufferedImage imageN = chargeImage(chemin);
        BufferedImage imageE = rotate(imageN, 90);
        BufferedImage imageS = rotate(imageE, 90);
        BufferedImage imageO = rotate(imageS, 90);
        BufferedImage[] images = new BufferedImage[4];
        images[0] = imageN;
        images[1] = imageE;
        images[2] = imageS;
        images[3] = imageO;
        return images;
    }

     static BufferedImage chargeImage(String chemin) {
         String dossierImages = "resources/img/";
         chemin = dossierImages + chemin;
         try {
             return ImageIO.read(new File(chemin));
         }catch (IOException e) {
             throw new RuntimeException("Impossible de charger l'image de chemin : " + chemin);
         }catch (NullPointerException e) {
             throw new RuntimeException("Impossible de trouver l'image correspondant au chemin : " + chemin);
         }
    }

    static BufferedImage rotate(BufferedImage bimg, double angle){
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
