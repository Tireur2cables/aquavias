import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Vue_Graphique {

    private Controller controller;

    public Vue_Graphique(Controller controller) {
        this.controller = controller;
    }

    public static void showPont(char c){
        String chemin = "resources/" + cheminPont(c);
        BufferedImage image = takeImage(chemin);
        new View("Pont", image);
    }

    private static String cheminPont(char c) {
        switch (c){
            case 'I':  return "InoO.png";
            case 'C': return "CnoO.png";
            case 'T': return "TnoO.png";
            default:
                throw new RuntimeException("Aucun pont correspondant à ce character");
        }
    }

    private static BufferedImage takeImage(String chemin) {
        try{
            return ImageIO.read(new File(chemin));
        }catch (IOException e){
            System.out.println("Impossible de charger l'image de chemin : " + chemin);
        }catch (NullPointerException e){
            System.out.println("Impossible de trouver l'image correspondant au chemin : " + chemin);
        }
        throw new RuntimeException("Erreur de chargement de l'image");
    }

}
