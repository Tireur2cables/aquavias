package aquavias.jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class Fenetre extends JFrame {

    private Controleur controleur;

    /**
     * INIT PART
     */

    /**
     * Fenetre pour les tests unitaires
     * */
    Fenetre(String titre, BufferedImage image, VueGraphique vue) {
        super();
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle(titre);
            this.setContentPane(new ImagePane(image, true, vue, 0, 0));
            this.pack();
            this.setVisible(true);
            this.setResizable(false);
        });
    }

    /**
     * Fenetre pour l'affichage du jeu
     * */
    Fenetre(Controleur controleur) {
        super();
        this.controleur = controleur;
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.addCloseOperation();
            this.setTitle("Aquavias");
            this.setVisible(false);
            this.setResizable(false); // Le jeu se joue en plein écran pour le moment
        });
    }

    /**
     * DISPLAY POPUP PART
     */

    void victoire() {
        String[] choices = {"Niveau Suivant", "Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, "Vous avez gagné! BRAVO!\nL'eau est là!","",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE /* Image personnaliable */, null, choices, choices[0]);
            if (retour == 0) /* retour = 0 = Niveau Suivant */
                this.controleur.nextLevel();
            else /* retour = 1 = Retour au menu */
                this.controleur.backMenu();
        });
    }

    void defaite() {
        String[] choices = {"Réessayer!", "Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, "Vous avez perdu! :(", "",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE /* Image personnaliable */, null, choices, choices[0]);
            if (retour == 0) /* retour = 0 = Réessayer */
                this.controleur.retry();
            else /* retour = 1 = Retour au menu */
                this.controleur.backMenu();
        });
    }

    void infoRetourMenu(String info) {
        String[] choices = {"Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, info,"",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            if (retour == 0) /* retour au menu */
                this.controleur.backMenu();
        });
    }

    /**
     * ADD PART
     */

    void setMenuBar(boolean inNiveau){
        EventQueue.invokeLater(() -> {
            this.setJMenuBar(new MenuBar(this, this.controleur, inNiveau));
        });
    }

    void addCompteur() {
        int compteur =  (int) this.controleur.getCompteur();
        JLabel counter = new JLabel("" + compteur);
        this.getJMenuBar().add(counter);
    }

    void addProgressBar() {
        int limite = this.controleur.getLimite();
        double compteur = this.controleur.getCompteur();
        double debit = this.controleur.getDebit();
        JProgressBar progressBar = new JProgressBar();
		progressBar.setMaximum(limite);
        progressBar.setValue((int) compteur);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.blue);
        this.updateBarString(compteur, progressBar, debit);
        this.getJMenuBar().add(progressBar);
    }

    private void addCloseOperation() {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                /* Ferme la fenêtre */
                dispose();

                /* arrête le programme dans son ensemble */
                controleur.exit();
            }

        });
    }

    /**
     *  UPDATE PART
     */

    void changeSize(int largeur, int hauteur) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        EventQueue.invokeLater(() -> {
            this.setSize(largeur*200, hauteur*200);
            this.pack();
            //this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.setLocationRelativeTo(null);
        });
    }

    private void updateBarString(double val, JProgressBar progressBar, double debit) {
        if (this.controleur.getMode().equals("compteur"))
            val = (int) val;
        if (val > 1)
            progressBar.setString(val + "L restants | -" + debit + "L/s");
        else
            progressBar.setString(val + "L restant | -" + debit + "L/s");
    }

    void decrementeCompteur() {
        JLabel compteur = ((JLabel) this.getJMenuBar().getComponents()[2]);
        double val = this.controleur.getCompteur();
        String newVal = String.valueOf(val);
        compteur.setText(newVal);
    }

    void decrementeProgressBar() {
        int limite = this.controleur.getLimite();
        JProgressBar progressBar = ((JProgressBar) this.getJMenuBar().getComponents()[2]);
        double compteur = this.controleur.getCompteur();
        double debit = this.controleur.getDebit();
        int val = progressBar.getValue();
        if(val < (limite/5))
            this.setClignotement(progressBar);
        compteur = this.arrondir(compteur);
        debit = this.arrondir(debit);
        progressBar.setValue((int) compteur);
        this.updateBarString(compteur, progressBar, debit);
    }

    private void setClignotement(JProgressBar progressBar) {
            if(progressBar.getValue()%2==0)
                progressBar.setForeground(Color.red);
            else
                progressBar.setForeground(Color.blue);
    }

    /**
     * Arrondir un double à le deuxieme décimale
     * */
    private double arrondir(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}

class Niveau extends JPanel {

    public Niveau(int largeur, int hauteur) {
        super();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        EventQueue.invokeLater(() -> {
            this.setLayout(new GridLayout(hauteur, largeur));
            this.setPreferredSize(new Dimension(dim.width,dim.height));
        });
    }
}

class ImagePane extends JPanel {

     private BufferedImage image;
     private int width;
     private int height;
     private VueGraphique vue;
     private int x;
     private int y;

    ImagePane(BufferedImage image, boolean movable, VueGraphique vue, int x, int y) {
        super();
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.vue = vue;
        this.x = x;
        this.y = y;
        EventQueue.invokeLater(() -> {
            this.setPreferredSize(new Dimension(this.width, this.height));
        });

        EventQueue.invokeLater(() -> {
            this.addMouseListener(new ClickListener(movable, this));
        });

    }

    void rotateImage() {
        /* On tourne les ponts de 90° */
        this.image = this.vue.getImage(this.x, this.y);
        this.vue.rotate(this.x, this.y);
    }

    void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this);
    }
}

class ClickListener implements MouseListener {

    private boolean movable;
    private ImagePane imagePane;

    ClickListener(boolean movable, ImagePane imagePane) {
        super();
        this.movable = movable;
        this.imagePane = imagePane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.movable) {
            this.imagePane.rotateImage();
            this.imagePane.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}

class MenuBar extends JMenuBar{

    MenuBar(Fenetre fenetre, Controleur controleur, boolean inNiveau) {
        super();
        JMenu charger = this.createChargerMenu(controleur);
        this.add(charger);

        if (inNiveau) {
            JButton save = this.createSave(fenetre, controleur);
            this.add(save);
        }
    }

    private JMenu createChargerMenu(Controleur controleur) {
        JMenu charger = new JMenu("Charger");
        ArrayList<File> niveaux = Controleur.getListNiveau();
        for (File f : niveaux) {
            JMenuItem niveau = createMenuItem(f.getName(), controleur);
            charger.add(niveau);
        }
        return charger;
    }

    private JMenuItem createMenuItem(String name, Controleur controleur) {
        int num = findNum(name);
        String newName = this.getFileName(name, num);
        JMenuItem item = new JMenuItem(newName);
        item.addActionListener((ActionEvent e) -> {
            controleur.chargeNiveau(num);
        });
        return item;
    }

    private int findNum(String name) {
        String num = "";
        for (int i = 6; name.charAt(i) != '.'; i++) {
            num = num + name.charAt(i);
        }
        return Integer.parseInt(num);
    }

    private String getFileName(String name, int num) {
        String nom = name.substring(0, 6);
        return nom + " " + num;
    }

    private JButton createSave(Fenetre fenetre, Controleur controleur) {
        JButton save = new JButton("Sauvegarder");
        save.addActionListener((ActionEvent e) -> {
            controleur.exportNiveau();
            JOptionPane.showMessageDialog(fenetre, "Niveau exporté!");
        });
        return save;
    }

}

class Accueil extends JPanel {

    private BufferedImage bg;

    Accueil(int largeur, int hauteur) {
        BufferedImage bg =  PontGraph.chargeImage("bg.png");
        this.bg = VueGraphique.resizeImage(bg, largeur, hauteur);
        this.setPreferredSize(new Dimension(largeur,hauteur));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.bg, 0, 0, this);
    }

}
