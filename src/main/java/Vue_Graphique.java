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

    public void showPont(char c){
        String chemin = "resources/";
        switch (c){
            case 'I': chemin += "InoO.png";
                break;
            case 'C': chemin += "CnoO.png";
                break;
            case 'T': chemin += "TnoO.png";
                break;
            default:
                System.out.println("Aucun pont correspondant à ce character");
        }
        try{
            BufferedImage image = ImageIO.read(new File(chemin));
        }catch (IOException e){
            System.out.println("Impossible de charger l'image");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }catch (NullPointerException e){
            System.out.println("Impossible de charger l'image");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
