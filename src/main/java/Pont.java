import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Imports with maven dependecies */
import org.json.*;

public abstract class Pont {

    protected char forme; /* I, T, L */
    protected char orientation; /* N, E, S, O */
    protected boolean[] sorties; /* [N, E, S, O] */
    protected boolean eau;
    protected String spe; /* entree, sortie, immobile */

    static BufferedImage transp = chargeImage("transp.png");

    public Pont(JSONArray json) {
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = (!json.isNull(2))?  json.getString(2).toLowerCase() : null;
        this.eau = this.isEntree();
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

    public void setOrientation(char c) {
        this.orientation = c;
        this.castAndCalculateSorties();
    }

    private void castAndCalculateSorties() {
        if (this instanceof PontI) this.sorties = ((PontI) this).calculSorties();
        else if (this instanceof PontL) this.sorties = ((PontL) this).calculSorties();
        else if (this instanceof PontT) this.sorties = ((PontT) this).calculSorties();
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

    public boolean isMovable() {
        return this.spe == null;
    }

    public boolean isEntree() {
        return this.spe != null && this.spe.equals("entree");
    }

    public boolean[] getSorties() {
        return this.sorties;
    }
    public void setEau(boolean eau) {
        this.eau = eau;
    }

    public static char getNextOrientation(char c) {
        switch (c) {
            case 'N' : return 'E';
            case 'E' : return 'S';
            case 'S' : return 'O';
            case 'O' : return 'N';
        }
        throw new RuntimeException("Calcul nouvelle orientation incorrect, Orientation = " + c);
    }

    public boolean isAccessibleFrom(char c) {
        switch (c) {
            case 'N' : return this.sorties[2]; /* accessible depuis le nord de l'autre pont donc le sud de ce pont etc... */
            case 'E' : return this.sorties[3];
            case 'S' : return this.sorties[0];
            case 'O' : return this.sorties[1];
        }
        throw new RuntimeException("char Sortie incorrect : " + c);
    }

}
