package aquavias.jeu;

import java.awt.*;
import java.awt.image.BufferedImage;

class VueGraphique {

    private final Controleur controleur;
    private final Fenetre fenetre;
    private Niveau niveau;
    private PontGraph[][] plateau;
    private int imageW;
    private int calleW;
    private int calleH;

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
        this.fenetre.setMenuBar(true);
        this.initNiveau(largeur, hauteur);
        for (int j = 0; j < hauteur; j++) {
            for (int i = 0; i < largeur; i++) {
                boolean movable = this.controleur.isMovable(i, j);
                this.addToNiveau(this.getImage(i, j), movable, i, j); //ajout des images dans la grille niveau
                if (i == largeur-1) this.addCalleLargeur(i+1, j); //ajoute les calles a la fin de chaque ligne
            }
        }
        this.addCallesHauteur(largeur, hauteur); //ajoute les calles à la fin de chaque colonne
        this.repaint(); //affiche le niveau
    }

    private void initNiveau(int largeur, int hauteur) {
        this.niveau = new Niveau();
        this.initPlateau(largeur, hauteur);
        this.calculImageSize(largeur, hauteur);
        this.calculCalleSize(largeur, hauteur);
        this.setNiveau();
    }

    /**
     * Initialise la matrice plateau avec les pontsGraphiques dépendants du modèle
     * */
    private void initPlateau(int largeur, int hauteur) {
        this.plateau = new PontGraph[largeur][hauteur];
        this.imageW = 0;
        this.calleW = 0;
        this.calleH = 0;
        for(int i = 0; i < largeur; i++){
            for(int j = 0; j < hauteur; j++){
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
     * Calcul la taille que doivent faire les "Calles" pour que la fentre soit totalement remplie
     * */
    private void calculCalleSize(int largeur, int hauteur) {
        Dimension frameDim = this.getEffectiveFrameSize();
        int width = frameDim.width;
        int height = frameDim.height;
        this.calleW = Math.max(width - (this.imageW * largeur), 1);
        this.calleH = Math.max(height - (this.imageW * hauteur), 1);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        EventQueue.invokeLater(() -> {
            this.niveau.add(new ImagePane(image, movable, this, x, y), gbc);
        });
    }

    private void addCalleLargeur(int x, int y) {
        BufferedImage image = PontGraph.transp;
        Accueil jpanel = new Accueil(resizeImage(image, this.calleW, this.imageW));
        GridBagConstraints gbc = new GridBagConstraints();
        if (x == this.plateau.length) gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = x;
        gbc.gridy = y;
        EventQueue.invokeLater(() -> {
            this.niveau.add(jpanel, gbc);
        });
    }

    private void addCallesHauteur(int largeur, int hauteur) {
        BufferedImage image = PontGraph.transp;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = hauteur;
        for (int i = 0; i < largeur + 1; i++) {
            if (i == largeur) gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = i;
            Accueil jpanel = new Accueil(resizeImage(image, this.imageW, this.calleH));
            EventQueue.invokeLater(() -> {
                this.niveau.add(jpanel, gbc);
            });
        }
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
        this.fenetre.victoire();
    }

    void defaite() {
        this.fenetre.defaite();
    }

    void infoRetourMenu(String info) {
        this.fenetre.infoRetourMenu(info);
    }

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
