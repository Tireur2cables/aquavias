package aquavias.jeu;

import java.awt.*;
import java.awt.image.BufferedImage;

class VueGraphique {

    private final Controleur controleur;
    private final Fenetre fenetre;
    private Niveau niveau;
    private PontGraph[][] plateau;
    private int imageW;

    static Font font = new Font("Tahoma", Font.BOLD, 20);

    /**
     *  TESTS AND DEBUG PART
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
     * Lancement du tuto
     */
    void afficheTuto(int num) {
        this.afficheNiveau();
        if (num == -1)
            this.afficheTutoCompteur();
        else
            this.afficheTutoFuite();
    }

    /**
     * remplit le JPanel Niveau avec chaque Pont du plateau de Jeu et entraine l'affichage de la fenêtre
     * */
    void afficheNiveau() {
        int hauteur = this.controleur.getHauteur();
        int largeur = this.controleur.getLargeur();
        this.fenetre.setMenuBar(true);
        this.initNiveau(largeur, hauteur);
        for (int j = 0; j < hauteur; j++) {
            for (int i = 0; i < largeur; i++) {
                boolean movable = this.controleur.isMovable(i, j);
                this.addToNiveau(this.getImage(i, j), movable, i, j); //ajout des images dans la grille niveau
            }
        }
        this.repaint(); //affiche le niveau
    }

    private void initNiveau(int largeur, int hauteur) {
        this.niveau = new Niveau();
        this.initPlateau(largeur, hauteur);
        this.calculImageSize(largeur, hauteur);
        this.setNiveau();
    }

    /**
     * Initialise la matrice plateau avec les pontsGraphiques dépendants du modèle
     * */
    private void initPlateau(int largeur, int hauteur) {
        this.plateau = new PontGraph[largeur][hauteur];
        this.imageW = 0;
        for(int i = 0; i < largeur; i++) {
            for(int j = 0; j < hauteur; j++) {
                this.plateau[i][j] = this.getPontGraphique(i, j);
            }
        }
    }

    /**
     * Calcul la taille que doivent faire les images
     * Suppose que toutes les images sont de la même taille que l'image de pont transparente
     * */
    private void calculImageSize(int largeur, int hauteur) {
        Dimension dim = this.getEffectiveFrameSize();
        double width = dim.width;
        double height = dim.height;
        this.imageW = PontGraph.transp.getWidth();
        int imageH = PontGraph.transp.getHeight();

        this.imageW = this.imageW * largeur;
        double diff = Math.abs(this.imageW - width) / largeur;
        this.imageW = this.imageW / largeur;
        this.imageW = (int) Math.floor((this.imageW*largeur > width)? this.imageW-diff : this.imageW+diff);
        // Math.floor arrondi a l'entier EN DESSOUS
        
        imageH = imageH * hauteur;
        diff = Math.abs(imageH - height) / hauteur;
        imageH = imageH / hauteur;
        imageH = (int) Math.floor((imageH*hauteur > height)? imageH-diff : imageH+diff);
        this.imageW = Math.min(this.imageW, imageH) - 1; //permet aux ponts d'être carrés
    }

    /**
     * Recupère le plateau Graphique et l'affiche, ainsi que les différents modes de jeu
     * */
    private void setNiveau() {
        EventQueue.invokeLater(() -> {
            this.fenetre.getContentPane().removeAll();
            this.fenetre.setContentPane(this.niveau);
        });
        if (this.controleur.getMode().equals("compteur"))
            this.fenetre.addCompteur();
        else if (this.controleur.getMode().equals("fuite")) {
            this.fenetre.addProgressBar();
            this.controleur.initTimer();
        }
    }

    /**
     *  Ajoute une imagePane avec les paramètres récupérés du model :
     *	-image selon la forme et l'orientation du pont,
     *	-movable si le pont peut être tourné
     * */
    private void addToNiveau(BufferedImage image, boolean movable, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        EventQueue.invokeLater(() -> {
            this.niveau.add(new ImagePane(image, movable, this, x, y), gbc);
        });
    }

    private void afficheTutoCompteur() {
        this.fenetre.infoOk("Dans cette partie du tutoriel tu es em mode \"compteur\".\nDans ce mode à chaque fois que tu clique pour faire tourner un pont, le compteur ci-dessus perd une charge.\n" +
                "Lorsqu'il arrive à 0 si tu n'as pas réussi à amener l'eau jusqu'à la sortie tu perdras. ");//placer sur compteur
        this.fenetre.infoOk("Mais avant sahce qu'il existe deux modes de jeux dans Aquavias.");
        this.fenetre.infoOk("Pour cela il faut faire tourner ce pont la en cliquant dessus!\nTu ne peux pas faire pivoter la sortie ou l'entrée.");
        this.fenetre.infoOk("Regarde ce pont juste en dessous! C'est la sortie!\nTU doit relier l'entree et la sortie.");//placer sur la sortie
        this.fenetre.infoOk("Tu vois le pont juste en dessous de ce message ? c'est le pont d'entrée!\nC'est ta source d'eau, ton chemin doit partir de ce pont là.");//placer sur l'entrée
        this.fenetre.infoOk("Bienvenu dans le jeu Aquavias!\nDans ce jeu tu va devoir créer un chemin pour faire circuler de l'eau entre l'entrée et la sortie!");
        this.fenetre.infoOk("Ceci est le tuto du mode Compteur!");
    }

    private void afficheTutoFuite() {
        this.fenetre.infoOk("Ceci est le tuto du mode fuite!");
    }

    /**
     * MENU PART
     */

    void chargeMenu() {
        this.fenetre.setMenuBar(false);
        BufferedImage image = PontGraph.chargeImage("bg.png");
        EventQueue.invokeLater(() -> {
            Dimension dim = this.getEffectiveFrameSize(); // doit etre dans le eventqeue pour avoir la taille dela jmenubar prise en compte
            this.imageW = dim.width;
            int imageH = dim.height;
            this.fenetre.setContentPane(new Accueil(this.resizeImage(image, this.imageW, imageH)));
            this.repaint();
        });
    }

    /**
     * SETTER PART
     */

    private void repaint() {
        EventQueue.invokeLater(() -> {
            this.fenetre.pack();
            this.fenetre.repaint();
        });
    }


    void decrementeCompteur() {
        this.fenetre.decrementeCompteur();
    }

    void decrementeProgressBar() {
        this.fenetre.decrementeProgressBar();
    }

    private BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
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
        if(this.controleur.existeNiveauSuivant()){
            this.fenetre.victoire();
        }else{
            this.fenetre.victoireSansNiveauSuivant();
        }

    }


    void defaite() {
        this.fenetre.defaite();
    }

    void infoRetourMenu(String info) {
        this.fenetre.infoRetourMenu(info);
    }

    void infoOk(String info) {this.fenetre.infoOk(info);}

    /**
     * GETTER PART
     * */

    private Dimension getEffectiveFrameSize() {
        int width = this.fenetre.getWidth() - (this.fenetre.getInsets().left + this.fenetre.getInsets().right);
        int height = this.fenetre.getHeight() - (this.fenetre.getInsets().bottom + this.fenetre.getInsets().top) - this.fenetre.getJMenuBar().getPreferredSize().height;
        return new Dimension(width, height);
    }

    BufferedImage getImage(int x, int y) {
        BufferedImage image = (this.plateau[x][y] == null)? PontGraph.transp : this.plateau[x][y].getImage();
        return this.resizeImage(image, this.imageW, this.imageW);
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
        int largeur = ((GridBagLayout) this.niveau.getLayout()).getLayoutDimensions()[0].length;
        int indice = x+y*largeur;
        ((ImagePane) this.niveau.getComponents()[indice]).setImage(image);
    }

}
