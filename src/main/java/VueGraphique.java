import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class VueGraphique {

    private Controleur controleur;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
        affichePont('I');
    }

    public static void affichePont(char c){
        String chemin = "resources/" + cheminPont(c);
        BufferedImage image = chargeImage(chemin);
        View v = new View("Pont", image);
    }

    private static String cheminPont(char c) {
        switch (c){
            case 'I':  return "InoO.png";
            case 'L': return "LnoO.png";
            case 'T': return "TnoO.png";
            default:
                throw new RuntimeException("Aucun pont correspondant à ce character");
        }
    }

    private static BufferedImage chargeImage(String chemin) {
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
