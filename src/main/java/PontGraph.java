import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface PontGraph{

    /* FIXMe dossier image en static ? */
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
	
}
