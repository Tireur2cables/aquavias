package aquavias.jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

class Fenetre extends JFrame {

    private Controleur controleur;
    private final static GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

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
            this.setResizable(false);
            this.setVisible(true);
        });
    }

    /**
     * Fenetre pour l'affichage du jeu
     * */
    Fenetre(Controleur controleur) {
        super();
        this.controleur = controleur;
        graphDevice.setFullScreenWindow(this);
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.addCloseOperation();
            this.setTitle("Aquavias");
            this.setVisible(true);
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
                this.controleur.mainMenu();
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
                this.controleur.mainMenu();
        });
    }

    void infoRetourMenu(String info) {
        String[] choices = {"Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, info,"",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            if (retour == 0) /* retour au menu */
                this.controleur.mainMenu();
        });
    }

    /**
     * ADD PART
     */

    void setMenuBar(boolean inNiveau) {
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

    private void updateBarString(double val, JProgressBar progressBar, double debit) {
        if (val > 1)
            progressBar.setString(val + "L restants | -" + debit + "L/s");
        else
            progressBar.setString(val + "L restant | -" + debit + "L/s");
    }

    void decrementeCompteur() {
        JLabel compteur = ((JLabel) this.getJMenuBar().getComponents()[2]);
        double val = this.controleur.getCompteur();
        String newVal = String.valueOf(val);
        EventQueue.invokeLater(() -> {
            compteur.setText(newVal);
        });
    }

    void decrementeProgressBar() {
        int limite = this.controleur.getLimite();
        JProgressBar progressBar = ((JProgressBar) this.getJMenuBar().getComponents()[2]);
        double compteur = this.controleur.getCompteur();
        double debit = this.controleur.getDebit();
        int val = progressBar.getValue();
        if(val < (limite/5))
            this.setClignotement(progressBar);
        double compteurArrondi = arrondir(compteur);
        double debitArr = arrondir(debit);
        EventQueue.invokeLater(() -> {
            progressBar.setValue((int) compteurArrondi);
            this.updateBarString(compteurArrondi, progressBar, debitArr);
        });
    }

    private void setClignotement(JProgressBar progressBar) {
            if(progressBar.getForeground().getRGB() == Color.blue.getRGB())
                EventQueue.invokeLater(() -> {
                    progressBar.setForeground(Color.red);
                });
            else
                EventQueue.invokeLater(() -> {
                    progressBar.setForeground(Color.blue);
                });
    }

    /**
     * Arrondir un double à le deuxieme décimale
     * */
    private static double arrondir(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}

class Niveau extends JPanel {

    public Niveau() {
        super();
        EventQueue.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
        });
    }

}

class ImagePane extends JPanel {

     private BufferedImage image;
     private final int width;
     private final int height;
     private final VueGraphique vue;
     private final int x;
     private final int y;
     private boolean movable;

    ImagePane(BufferedImage image, boolean movable, VueGraphique vue, int x, int y) {
        super();
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.vue = vue;
        this.x = x;
        this.y = y;
        this.movable = movable;
        EventQueue.invokeLater(() -> {
            this.setPreferredSize(new Dimension(this.width, this.height));
        });

        EventQueue.invokeLater(() -> {
            this.addMouseListener(new ClickListener(this));
        });

    }

    boolean isMovable() {
        return this.movable;
    }

    void rotateImage() {
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

    private final ImagePane imagePane;

    ClickListener(ImagePane imagePane) {
        super();
        this.imagePane = imagePane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.imagePane.isMovable()) this.imagePane.rotateImage();
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

class MenuBar extends JMenuBar {

    MenuBar(Fenetre fenetre, Controleur controleur, boolean inNiveau) {
        super();
        JMenu charger = this.createChargerMenu(controleur);
        this.add(charger);

        JMenu options = this.createOptionsMenu(controleur, fenetre, inNiveau);
        this.add(options);
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

    private JMenu createOptionsMenu(Controleur controleur, Fenetre fenetre, boolean inNiveau) {
        JMenu menu = new JMenu("Options");
        if (inNiveau) {
            JMenuItem mainMenu = this.createMainMenu(controleur);
            menu.add(mainMenu);
            JMenuItem save = this.createSave(fenetre, controleur);
            menu.add(save);
        }
        JMenuItem exit = this.createExit(controleur);
        menu.add(exit);
        return menu;
    }

    private JMenuItem createMainMenu(Controleur controleur) {
        JMenuItem mainMenu = new JMenuItem("Menu principal");
        mainMenu.addActionListener((ActionEvent e) -> {
            controleur.mainMenu();
        });
        return mainMenu;
    }

    private JMenuItem createSave(Fenetre fenetre, Controleur controleur) {
        JMenuItem save = new JMenuItem("Sauvegarder");
        save.addActionListener((ActionEvent e) -> {
            controleur.exportNiveau();
            JOptionPane.showMessageDialog(fenetre, "Niveau exporté!");
        });
        return save;
    }

    private JMenuItem createExit(Controleur controleur) {
        JMenuItem exit = new JMenuItem("Quitter");
        exit.addActionListener((ActionEvent e) -> {
            controleur.exit();
        });
        return exit;
    }

}

class Accueil extends JPanel {

    private BufferedImage bg;

    Accueil(BufferedImage bg) {
        this.bg = bg;
        this.setPreferredSize(new Dimension(this.bg.getWidth(), this.bg.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.bg, 0, 0, this);
    }

}
