package aquavias.jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

    private Fenetre affichePont(BufferedImage image) {
        return new Fenetre("aquavias.jeu.Pont", image, this);
    }

    /**
     * INIT PART
     * */

    VueGraphique(Controleur controleur, boolean isTest) {
        this.controleur = controleur;
        if (isTest) this.fenetre = this.affichePont(PontGraph.transp);
        else this.fenetre = new Fenetre(controleur);
    }

    /**
     * Lancement du tuto
     */
    void afficheTuto(int num) {
        this.afficheNiveau();
        if (num == -1)
            this.afficheTutoCompteur(0);
        else
            this.afficheTutoFuite(0);
    }

    private final static String[] textTutoCompteur = new String[]
            {
                    "Ceci est le tuto du mode Compteur!",
                    "Bienvenu dans le jeu Aquavias!\nDans ce jeu tu vas devoir créer un chemin pour faire circuler de l'eau entre l'entrée et la sortie!",
                    "Tu vois le pont contenant de l'eau juste au dessus de ce message? C'est le pont d'entrée!\nC'est ta source d'eau, ton chemin doit partir de ce pont là.",
                    "Regarde ce pont, en rouge, juste au dessus! C'est la sortie!\nTu dois relier l'entrée et la sortie.",
                    "Pour cela il faut faire tourner ce pont là, en dessous du message, en cliquant dessus!\nTu ne peux ni faire pivoter la sortie ni l'entrée.",
                    "Mais avant de commencer, sache qu'il existe deux modes de jeu dans Aquavias.",
                    "Dans cette partie du tutoriel tu es en mode \"compteur\".\nDans ce mode à chaque fois que tu cliques pour faire tourner un pont le compteur ci-dessus, dans la barre de menu, perd une charge.\nLorsqu'il arrive à 0 si tu n'as pas réussi à créer un chemin étanche jusqu'à la sortie tu perdras."
            };

    private final static String[] textTutoFuite = new String[]
            {
                    "Ceci est le tuto du mode fuite!",
                    "Commme tu peux le voir, ici tu disposes d'une certaine quantité d'eau indiquée à la place du compteur de coup.\nLorsqu'un pont emmène de l'eau vers le vide, cela fait baisser ta réserve d'eau.\nPlus tu auras de ponts qui emmènent l'eau vers la sortie moins tu perdras d'eau chaque seconde.",
                    "Il existe également différentes formes de ponts dans le jeu.\nDans le tutoriel précédent tu as pu voir les ponts en ligne droite.",
                    "Ici il y a aussi des ponts formants un virage comme celui ci-dessus.",
                    "Tu trouveras également des ponts en forme de T, créant une bifurcation, comme celui juste en dessous.",
                    "Enfin le dernier type de pont est le pont en croix qui possède une entrée dans chaque direction.\n(un conseil : évite de les tourner cela ne te serviras à rien et te feras perdre du temps ou pire, un coups)",
                    "Si tu réussis ce niveau tu seras fin prêt à t'amuser dans Aquavias!\nBonne chance!"
            };

    /**
     * remplit le JPanel Niveau avec chaque Pont du plateau de Jeu et entraine l'affichage de la fenêtre
     * */
    void afficheNiveau() {
        int hauteur = this.controleur.getHauteur();
        int largeur = this.controleur.getLargeur();
        this.fenetre.setMenuBar(true, controleur.debloqueGenerateur());
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

    private void afficheTutoCompteur(int num) {
        String[] choices = {"Ok"};
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane(textTutoCompteur[num], JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, new ImageIcon("resources/img/ok.gif"), choices, choices[0]);
            Point p = this.choosePoint(num, "compteur");
            JDialog dialog = this.createDialog(optionPane, p);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) {
                Controleur.setPlayable(true);
                if (num+1 < textTutoCompteur.length)
                    this.afficheTutoCompteur(num+1);
            }
        });
    }

    private void afficheTutoFuite(int num) {
        String[] choices = {"Ok"};
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane(textTutoFuite[num], JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, new ImageIcon("resources/img/ok.gif"), choices, choices[0]);
            Point p = this.choosePoint(num, "fuite");
            JDialog dialog = this.createDialog(optionPane, p);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) {
                Controleur.setPlayable(true);
                if (num+1 < textTutoFuite.length)
                    this.afficheTutoFuite(num+1);
            }
        });
    }

    /**
     * MENU PART
     */

    void chargeMenu(Controleur controleur) {
        this.fenetre.setMenuBar(false, controleur.debloqueGenerateur());
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

    private JDialog createDialog(JOptionPane optionPane, Point p) {
        Controleur.setPlayable(false);
        JDialog dialog = new JDialog(this.fenetre,"", true);
        dialog.setContentPane(optionPane);
        dialog.setUndecorated(true);
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (dialog.isVisible() && (e.getSource() == optionPane)) {
                    dialog.setVisible(false);
                }
            }
        });
        dialog.pack();
        if (p == null)
            dialog.setLocationRelativeTo(null);
        else
            dialog.setLocation(p);
        dialog.setVisible(true);
        return dialog;
    }

    private Point choosePoint(int num, String typeTuto) {
        Dimension dim = this.getEffectiveFrameSize();
        if (typeTuto.equals("fuite")) {
            switch (num) {
                case 1 : return new Point(0, dim.height/20);
                case 3 : return new Point(dim.width/15, (dim.height)/3);
                case 4 : return new Point((dim.width*2)/9, dim.height/5);
                case 5 : return new Point((dim.width*3)/8, dim.height/5);
                default : return null;
            }
        }else {
            switch (num) {
                case 2 : return new Point(dim.width/8, (dim.height*3)/4);
                case 3 : return new Point((dim.width*7)/13, (dim.height*3)/4);
                case 4 : return new Point((dim.width*5)/16, dim.height/4);
                case 6 : return new Point(0, dim.height/20);
                default : return null;
            }
        }
    }

    void victoire() {
        if(this.controleur.existeNiveauSuivant()) {
            this.fenetre.victoire();
        }else {
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
