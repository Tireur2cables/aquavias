import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Imports with maven dependecies */
import org.json.*;

public abstract class Pont {

    protected char forme; /* I, T, L */
    protected char orientation; /* N, E, S, O */
    protected boolean[] sorties;
    protected boolean eau;
    protected String spe; /* entree, sortie, immobile */

    public Pont(JSONArray json) {
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = (!json.isNull(2))?  json.getString(2).toLowerCase() : null;
        this.eau = false;
    }

    protected static BufferedImage chargeImage(String chemin) {
        String dossierImages = "resources/img/";
        chemin = dossierImages + chemin;
        try {
            return ImageIO.read(new File(chemin));
        }catch (IOException e) {
            System.out.println("Impossible de charger l'image de chemin : " + chemin);
        }catch (NullPointerException e) {
            System.out.println("Impossible de trouver l'image correspondant au chemin : " + chemin);
        }
        throw new RuntimeException("Erreur de chargement de l'image");
    }

    public void setSorties(boolean[] sorties) {
        this.sorties = sorties;
    }

    public char getForme(){
        return this.forme;
    }

    public boolean getEau() {
        return this.eau;
    }

    public char getOrientation(){
        return this.orientation;
    }

}
