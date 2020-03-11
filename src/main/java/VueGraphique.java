import java.awt.*;
import java.awt.image.BufferedImage;
public class VueGraphique {

    private Controleur controleur;
    private Fenetre fenetre;
    private Plateau plateau;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.fenetre = new Fenetre(controleur);
    }

    static BufferedImage rotate(BufferedImage bimg, double angle){
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(bimg, null, 0, 0);
        graphic.dispose();

        /* tentative de libération de la mémoire */
        bimg = null;
        System.gc();
        return rotated;
    }

    public void setVisible() {
        this.fenetre.setVisible(true);
    }

    public void repaint(){
        this.fenetre.repaint();
        this.fenetre.pack();
    }

    public void affichePont(BufferedImage image) {
        EventQueue.invokeLater(() -> new Fenetre("Pont", image, this.controleur));
    }

    public void initPlateau(int hauteur, int largeur) {
        this.plateau = new Plateau(hauteur, largeur);
    }

    public void afficheNiveau() {
        EventQueue.invokeLater(() -> {
            this.fenetre.setContentPane(this.plateau);
            if (this.controleur.getMode().equals("compteur"))
                this.fenetre.addCompteur();
        });
    }

    public void addToPlateau(BufferedImage image, boolean movable, int x, int y) {
        EventQueue.invokeLater(() -> {
            this.plateau.add(new ImagePane(image, movable, this.controleur, x, y));
        });
    }

    public void actualiseImage(BufferedImage image, int x, int y) {
        int largeur = ((GridLayout) this.plateau.getLayout()).getColumns();
        int indice = y+x*largeur;
        ((ImagePane) this.plateau.getComponents()[indice]).setImage(image);
    }

    void victoire() {
        this.fenetre.victoire();
    }
    
    void defaite() {
        this.fenetre.defaite();
    }

    void decrementeCompteur() {
        this.fenetre.decrementeCompteur();
    }

}
