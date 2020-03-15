import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

abstract class PontGraph {

    static BufferedImage transp = chargeImage("transp.png");
    protected int orientation;
    protected boolean eau;

    PontGraph(char orientation, boolean eau) {
        this.orientation = getOrientationFromChar(orientation);
        this.eau = eau;
    }

    private static int getOrientationFromChar(char orientation) {
        switch (orientation) {
            case 'N': return 0;
            case 'E': return 1;
            case 'S': return 2;
            case 'O': return 3;
        }
        throw new RuntimeException("Orientation inconnue : " + orientation);
    }

    void incrementeOrientation() {
        this.orientation = (++this.orientation)%4;
    }

    static PontGraph getPontGraph(Pont p) {
        PontGraph newP = null;
        if (p == null) return newP;
        else {
            switch (p.getForme()) {
                case 'I' : newP = new PontIGraph(p.orientation, p.eau);
                    break;
                case 'L' : newP = new PontLGraph(p.orientation, p.eau);
                    break;
                case 'T' : newP = new PontTGraph(p.orientation, p.eau);
                    break;
            }
        }
        return newP;
    }

    abstract BufferedImage getImage();

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

    static BufferedImage rotate(BufferedImage bimg, double angle) {
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
