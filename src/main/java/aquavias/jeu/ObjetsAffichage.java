package aquavias.jeu;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
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
        graphDevice.setFullScreenWindow(this); //plein écran
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // inutil car en plein écran
            this.setTitle("Aquavias");
            this.setVisible(true);
        });
    }

    /**
     * DISPLAY POPUP PART
     */

    void victoire() {
        String[] choices = {"Niveau Suivant", "Retour au menu"};
        controleur.ajoutListeNiveauTermine();
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane("Vous avez gagné! BRAVO!\nL'eau est là!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, new ImageIcon("resources/img/victory.png"), choices, choices[0]);
            JDialog dialog = this.createDialog(optionPane);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) { /* retour = Niveau Suivant */
                Controleur.setPlayable(true);
                this.controleur.nextLevel();
            }else {
                Controleur.setPlayable(true);
                this.controleur.exportNiveauSuivant(this.controleur.getNumNiveau() + 1); //Existe car ce menu n'est affiché qu'en cas de niveau suivant
                this.controleur.mainMenu();
            }
        });
    }

    void victoireSansNiveauSuivant() {
        String[] choices = {"Retour au menu"};
        controleur.ajoutListeNiveauTermine();
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane("Vous avez gagné! BRAVO!\nL'eau est là!\nVous êtes arrivé au dernier Niveau !", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION,new ImageIcon("resources/img/victorylast.png"), choices, choices[0]);
            JDialog dialog = this.createDialog(optionPane);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) {
                Controleur.setPlayable(true);
                this.controleur.endGame();
                this.controleur.supprimerSauvegarde();
            }
        });
    }

    void defaite() {
        String[] choices = {"Réessayer!", "Retour au menu"};
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane("Vous avez perdu! :(", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION,new ImageIcon("resources/img/defeat.png"), choices, choices[0]);
            JDialog dialog = this.createDialog(optionPane);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) {/* retour = Réessayer */
                Controleur.setPlayable(true);
                this.controleur.retry();
            }else { /* retour = Retour au menu */
                Controleur.setPlayable(true);
                this.controleur.mainMenu();
            }
        });
    }

    void infoRetourMenu(String info) {
        String[] choices = {"Retour au menu"};
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE,  JOptionPane.YES_NO_OPTION,new ImageIcon("resources/img/menu.png"), choices, choices[0]);
            JDialog dialog = this.createDialog(optionPane);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) { /* retour au menu */
                Controleur.setPlayable(true);
                this.controleur.mainMenu();
            }
        });
    }

    void infoOk(String info) {
        String[] choices = {"Ok"};
        EventQueue.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, new ImageIcon("resources/img/ok.png"), choices, choices[0]);
            JDialog dialog = this.createDialog(optionPane);
            String retour = (String) optionPane.getValue();
            if (retour.equals(choices[0])) {
                Controleur.setPlayable(true);
            }
        });
    }
    /**
     * ADD PART
     */

    void setMenuBar(boolean inNiveau) {
        EventQueue.invokeLater(() -> {
            this.setJMenuBar(null);
            this.setJMenuBar(new MenuBar(this, this.controleur, inNiveau));
        });
    }

    void addCompteur() {
        int compteur =  (int) this.controleur.getCompteur();
        JLabel counter = new JLabel("" + compteur);
        counter.setFont(VueGraphique.font);
        EventQueue.invokeLater(() -> {
            this.getJMenuBar().add(counter);
        });
    }

    void addProgressBar() {
        int limite = this.controleur.getLimite();
        double compteur = this.controleur.getCompteur();
        double debit = this.controleur.getDebit();
        JProgressBar progressBar = new JProgressBar(0, limite);
        progressBar.setValue((int) compteur);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.blue);
        this.updateBarString(compteur, progressBar, debit);
        progressBar.setFont(VueGraphique.font);
        EventQueue.invokeLater(() -> {
            this.getJMenuBar().add(progressBar);
        });
    }

    private JDialog createDialog(JOptionPane optionPane) {
        Controleur.setPlayable(false);
        JDialog dialog = new JDialog(this,"", true);
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
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     *  UPDATE PART
     */

    private void updateBarString(double val, JProgressBar progressBar, double debit) {
        String restant = (val > 1)? "L restants " : "L restant ";
        String deb = (debit == 0)? "| " + ((int) debit) : "| -" + debit;
        progressBar.setString(val + restant + deb + "L/s");
    }

    void decrementeCompteur() {
        int indice = this.getIndiceCompteur();
        JLabel compteur = ((JLabel) this.getJMenuBar().getComponents()[indice]);
        int val = (int) this.controleur.getCompteur();
        String newVal = String.valueOf(val);
        EventQueue.invokeLater(() -> {
            compteur.setText(newVal);
        });
    }

    void decrementeProgressBar() {
        EventQueue.invokeLater(() -> { // il faut tout englober car la méthode est utilisée par le thread du timer ce qui pose probleme si il ne passe pas par l'EDT
            int indice = this.getIndiceProgressBar();
            JProgressBar progressBar = ((JProgressBar) this.getJMenuBar().getComponents()[indice]);
            int limite = this.controleur.getLimite();
            double compteur = this.controleur.getCompteur();
            double debit = this.controleur.getDebit();
            int val = progressBar.getValue();
            if(val < (limite/5))
                this.setClignotement(progressBar);
            double compteurArrondi = arrondir(compteur);
            double debitArr = arrondir(debit);
            progressBar.setValue((int) compteurArrondi);
            this.updateBarString(compteurArrondi, progressBar, debitArr);
        });
    }

    private void setClignotement(JProgressBar progressBar) {
        if(progressBar.getForeground().getRGB() == Color.blue.getRGB())
            progressBar.setForeground(Color.red);
        else
            progressBar.setForeground(Color.blue);
    }

    /**
     * Arrondir un double à le deuxieme décimale
     * */
    private static double arrondir(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     *  GETTER PART
     */

    private int getIndiceCompteur() {
        Component[] tab = this.getJMenuBar().getComponents();
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] instanceof JLabel) return i; //suppose qu'il n'y a qu'un seul jlabell dans la jmenubar
        }
        throw new RuntimeException("Impossible de trouver le compteur dans la jmenubarre");
    }

    private int getIndiceProgressBar() {
        Component[] tab = this.getJMenuBar().getComponents();
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] instanceof JProgressBar) return i; //suppose qu'il n'y a qu'une seule jprogressbar dans la jmenubar
        }
        throw new RuntimeException("Impossible de trouver la progressbar dans la jmenubarre");
    }

}

class Niveau extends JPanel {

    Niveau() {
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
     private final boolean movable;

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
        if (this.imagePane.isMovable())
            this.imagePane.rotateImage();
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
        JMenu charger = this.createChargerMenu(controleur, inNiveau);
        charger.setFont(VueGraphique.font);
        this.add(charger);

        JMenu options = this.createOptionsMenu(controleur, fenetre, inNiveau);
        options.setFont(VueGraphique.font);
        this.add(options);
    }

    private JMenu createChargerMenu(Controleur controleur, boolean inNiveau) {
        JMenu charger = new JMenu("Charger");
        if(!inNiveau && controleur.existeUneSauvegarde()){
            JMenuItem continuer = createMenuItemContinuer(controleur);
            continuer.setForeground(Color.BLUE);
            charger.add(continuer);
        }
        ArrayList<File> niveaux = Controleur.getListNiveau();
        for (File f : niveaux) {
            try {
                JSONObject json = new JSONObject(FileUtils.readFileToString(f, "utf-8"));
                String difficulte = json.getString("difficulte");
                JMenuItem niveau = createMenuItem(f.getName(), controleur, difficulte);
                charger.add(niveau);
            }catch (IOException e) {
                throw new RuntimeException("Impossible de lire le fichier niveau à l'adresse : " + f.getAbsolutePath());
            }
        }
        return charger;
    }

    private JMenuItem createMenuItem(String name, Controleur controleur, String difficulte) {
        int num = findNum(name);
        String newName = this.getFileName(name, num, difficulte);
        JMenuItem item = new JMenuItem(newName);
        if(controleur.niveauDejaTermine(num)){
            item.setForeground(Color.gray);
        }
        else{
            item.setForeground(Color.black);
        }
        item.addActionListener((ActionEvent e) -> {
            controleur.chargeNiveau(num);
        });
        return item;
    }

    private JMenuItem createMenuItemContinuer(Controleur controleur){
        String name = "Continuer";
        JMenuItem item = new JMenuItem(name);
        item.addActionListener((ActionEvent e) -> {
            controleur.chargeNiveau();
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

    private String getFileName(String name, int num, String difficulte) {
        String nom = name.substring(0, 6);
        return nom + " " + num + " (" + difficulte + ")";
    }

    private JMenu createOptionsMenu(Controleur controleur, Fenetre fenetre, boolean inNiveau) {
        JMenu menu = new JMenu("Options");
        if (inNiveau) {
            JMenuItem mainMenu = this.createMainMenu(controleur);
            menu.add(mainMenu);
            JMenuItem save = this.createSave(fenetre, controleur);
            menu.add(save);
        }
        JMenuItem exit = this.createExit(fenetre, controleur, inNiveau);
        menu.add(exit);
        return menu;
    }

    private JMenuItem createMainMenu(Controleur controleur) {
        JMenuItem mainMenu = new JMenuItem("Menu principal");
        mainMenu.addActionListener((ActionEvent e) -> {
            controleur.exportNiveau(true);
            controleur.mainMenu();
        });
        return mainMenu;
    }

    private JMenuItem createSave(Fenetre fenetre, Controleur controleur) {
        JMenuItem save = new JMenuItem("Sauvegarder");
        save.addActionListener((ActionEvent e) -> {
            controleur.exportNiveau(true);
            fenetre.infoRetourMenu("Niveau Sauvergardé!");
        });
        return save;
    }

    private JMenuItem createExit(Fenetre fenetre, Controleur controleur, boolean inNiveau) {
        JMenuItem exit = new JMenuItem("Quitter");
        exit.addActionListener((ActionEvent e) -> {
            controleur.saveListeNiveauTermine();
            if(inNiveau){
                controleur.exportNiveau(true);
            }
            fenetre.dispose();
            controleur.exit();
        });
        return exit;
    }

}

class Accueil extends JPanel {

    private final BufferedImage bg;

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
