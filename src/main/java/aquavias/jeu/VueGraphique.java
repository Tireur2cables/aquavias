package aquavias.jeu;

import java.awt.*;
import java.awt.image.BufferedImage;

class VueGraphique {

    private Controleur controleur;
    private Fenetre fenetre;
    private Niveau niveau;
    private PontGraph[][] plateau;

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
    }

    /**
     * Initialise la matrice plateau avec les pontsGraphiques dépendants du modèle
     * */
    private void initPlateau(int largeur, int hauteur) {
        this.plateau = new PontGraph[largeur][hauteur];
        for(int i = 0; i < largeur; i++){
            for(int j = 0; j < hauteur; j++){
                this.plateau[i][j] = this.getPontGraphique(i, j);
            }
        }
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
        EventQueue.invokeLater(() -> {
            this.fenetre.setContentPane(new Accueil());
            this.fenetre.setMenuBar(false);
            this.fenetre.pack();
            this.fenetre.repaint();
            this.fenetre.setLocation(dim.width/2-this.fenetre.getSize().width/2, dim.height/2-this.fenetre.getSize().height/2);
            this.fenetre.setVisible(true);
        });
    }

    /**
     * SETTER PART
     */

    private void repaint() {
        this.fenetre.repaint();
        this.fenetre.changeSize(this.controleur.getLargeur(), this.controleur.getHauteur());
    }

    void setEau(int x, int y, boolean eau) {
        if (this.plateau != null) this.plateau[x][y].eau = eau;
    }

    void decrementeCompteur() {
        this.fenetre.decrementeCompteur();
    }

    void decrementeProgressBar() {
        this.fenetre.decrementeProgressBar();
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

    /**
     * GETTER PART
     * */

    BufferedImage getNextImage(int x, int y) {
        plateau[x][y].incrementeOrientation();
        return plateau[x][y].getImage();
    }

    private BufferedImage getImage(int x, int y) {
        return (this.plateau[x][y] == null)? PontGraph.transp : this.plateau[x][y].getImage();
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
        this.refreshEau();
        this.actualiseAllImages();
    }

    void refreshEau(){
        for(int i = 0; i < this.controleur.getLargeur(); i++){
            for(int j = 0; j < this.controleur.getHauteur(); j++){
                if(this.plateau[i][j] != null){
                    this.setEau(i, j, this.controleur.getPont(i, j).getEau());
                }
            }
        }
    }

	/**
	*   Met à jour l'image a la position x,y avec la nouvelle image image
	* */
    private void actualiseImage(BufferedImage image, int x, int y) {
        int largeur = ((GridLayout) this.niveau.getLayout()).getColumns();
        int indice = x+y*largeur;
        ((ImagePane) this.niveau.getComponents()[indice]).setImage(image);
    }

    private void actualiseAllImages() {
        for (int i = 0; i < this.controleur.getLargeur(); i++) {
            for (int j = 0; j < this.controleur.getHauteur(); j++) {
                this.actualiseImage(this.getImage(i, j), i, j);
            }
        }
    }

}
