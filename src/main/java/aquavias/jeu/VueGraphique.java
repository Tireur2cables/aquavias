package aquavias.jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class VueGraphique {

    private Controleur controleur;
    private Fenetre fenetre;
    private Niveau niveau;
    private PontGraph[][] plateau;
    private int imageW;
    private int imageH;

    /**
     *  Fonction pour affichage de test unitaire
     */
    void affichePont(BufferedImage image) {
        EventQueue.invokeLater(() -> new Fenetre("aquavias.jeu.Pont", image, this));
    }

    /**
     * INIT PART
     * */

    VueGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.fenetre = new Fenetre(controleur);
    }

    /**
     * remplit le JPanel Niveau avec chaque Pont du plateau de Jeu et entraine l'affichage de la fenêtre
     * */
    void afficheNiveau() {
        int hauteur = this.controleur.getHauteur();
        int largeur = this.controleur.getLargeur();
        this.initNiveau(largeur, hauteur);
        fenetre.setMenuBar(true);
        this.setNiveau();
        for (int j = 0; j < hauteur; j++) {
            for (int i = 0; i < largeur; i++) {
                boolean movable = this.controleur.isMovable(i, j);
                this.addToNiveau(this.getImage(i, j), movable, i, j);
            }
        }
        this.repaint();
        this.fenetre.setVisible(true);
    }

    private void initNiveau(int largeur, int hauteur) {
        this.niveau = new Niveau(largeur, hauteur);
        this.initPlateau(largeur, hauteur);
        this.calculImageSize(largeur, hauteur);
    }

    /**
     * Initialise la matrice plateau avec les pontsGraphiques dépendants du modèle
     * */
    private void initPlateau(int largeur, int hauteur) {
        this.plateau = new PontGraph[largeur][hauteur];
        this.imageW = 0;
        this.imageH = 0;
        for(int i = 0; i < largeur; i++){
            for(int j = 0; j < hauteur; j++){
                this.plateau[i][j] = this.getPontGraphique(i, j);
            }
        }
    }

    /**
     * Suppose que toutes les images sont de la même taille que l'image de pont transparente
     * */
    private void calculImageSize(int largeur, int hauteur) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        int width = dim.width - (insets.left + insets.right);
        int height = dim.height - (insets.bottom + insets.top);
        this.imageW = PontGraph.transp.getWidth();
        this.imageH = PontGraph.transp.getHeight();

        this.imageW = this.imageW * largeur;
        int diff = Math.abs(this.imageW - width)/largeur;
        this.imageW = this.imageW / largeur;
        this.imageW = (this.imageW > width)? this.imageW-diff : this.imageW+diff;

        this.imageH = this.imageH * hauteur;
        diff = Math.abs(this.imageH - height)/hauteur;
        this.imageH = this.imageH / hauteur;
        this.imageH = (this.imageH > height)? this.imageH-diff : this.imageH+diff;
    }

    /**
     * Recupère le plateau Graphique et l'affiche, ainsi que les différents modes de jeu
     * */
    private void setNiveau() {
        EventQueue.invokeLater(() -> {
            this.fenetre.getContentPane().removeAll();
            this.fenetre.setContentPane(this.niveau);
            if (this.controleur.getMode().equals("compteur"))
                this.fenetre.addCompteur();
            else if (this.controleur.getMode().equals("fuite")) {
                this.fenetre.addProgressBar();
                this.controleur.initTimer();
            }
        });
    }

    /**
     *  Ajoute une imagePane avec les paramètres récupérés du model :
     *	-image selon la forme et l'orientation du pont,
     *	-movable si le pont peut être tourné
     * */
    private void addToNiveau(BufferedImage image, boolean movable, int x, int y) {
        EventQueue.invokeLater(() -> {
            this.niveau.add(new ImagePane(image, movable, this, x, y));
        });
    }

    /**
     * MENU PART
     */

    void chargeMenu() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        this.imageW = dim.width - (insets.left + insets.right);
        this.imageH = dim.height - (insets.bottom + insets.top);
        EventQueue.invokeLater(() -> {
            this.fenetre.setContentPane(new Accueil(this.imageW, this.imageH));
            this.fenetre.setMenuBar(false);
            this.fenetre.pack();
            this.fenetre.repaint();
            this.fenetre.changeSize(this.imageW,this.imageH);
            //this.fenetre.setLocation(dim.width/2-this.fenetre.getSize().width/2, dim.height/2-this.fenetre.getSize().height/2);
            this.fenetre.setVisible(true);
        });
    }

    /**
     * SETTER PART
     */

    private void repaint() {
        this.fenetre.repaint();
        this.fenetre.changeSize(this.controleur.getLargeur()*this.imageW, this.controleur.getHauteur()*this.imageH);
    }


    void decrementeCompteur() {
        this.fenetre.decrementeCompteur();
    }

    void decrementeProgressBar() {
        this.fenetre.decrementeProgressBar();
    }

    static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * DISPLAY POPUP PART
     */

    void victoire() {
        this.fenetre.victoire();
    }

    void defaite() {
        this.fenetre.defaite();
    }

    void infoRetourMenu(String info) { this.fenetre.infoRetourMenu(info); }

    /**
     * GETTER PART
     * */

    BufferedImage getImage(int x, int y) {
        BufferedImage image = (this.plateau[x][y] == null)? PontGraph.transp : this.plateau[x][y].getImage();
        return resizeImage(image, this.imageW, this.imageH);
    }

    /**
     * renvoit un PontGraphique en fonction du pont aux coordonnées i, j dans le plateau de jeu
     * */
    private PontGraph getPontGraphique(int i, int j) {
        Pont p = this.controleur.getPont(i, j);
        return PontGraph.getPontGraph(p);
    }

    /**
     * ACTUALISATION PART
     * */

    void rotate(int x, int y) {
        this.controleur.tournePont(x,y);
        this.actualiseAllImages();
    }

    private void actualiseAllImages() {
        for (int i = 0; i < this.controleur.getLargeur(); i++) {
            for (int j = 0; j < this.controleur.getHauteur(); j++) {
                this.actualiseImage(this.getImage(i, j), i, j);
            }
        }
    }

	/**
	*   Met à jour l'image a la position x,y avec la nouvelle image `image`
	* */
    private void actualiseImage(BufferedImage image, int x, int y) {
        int largeur = ((GridLayout) this.niveau.getLayout()).getColumns();
        int indice = x+y*largeur;
        ((ImagePane) this.niveau.getComponents()[indice]).setImage(image);
    }

}
