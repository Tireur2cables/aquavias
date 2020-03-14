import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

class Fenetre extends JFrame {

    private Controleur controleur;
    /**
     * Fenetre pour les tests unitaires
     * */
    public Fenetre(String titre, BufferedImage image, VueGraphique vue) {
        super();
        this.controleur = controleur;
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle(titre);
            this.setContentPane(new ImagePane(image, true, vue, 0, 0));
            this.pack();
            this.setVisible(true);
        });
    }

    /**
     * Fenetre pour l'affichage du jeu
     * */
    public Fenetre(Controleur controleur) {
        super();
        this.controleur = controleur;
        EventQueue.invokeLater(() -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle("Aquavias");
            this.setJMenuBar(Menu.createMenu(this, controleur));
            this.setVisible(false);
        });
    }

    void victoire() {
        String[] choices = {"Niveau Suivant", "Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, "Vous avez gagner! BRAVO!\nL'eau est là!","",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE /* Image personnaliable */, null, choices, choices[0]);
            if (retour == 0) /* retour = 0 = Réessayer */
                System.out.println("Niveau suivant");
                //this.controleur.nextLevel();
            else /* retour = 1 = Retour au menu */
                System.out.println("Retour au menu");
                //this.controleur.backMenu();
        });
    }

    void decrementeCompteur() {
        JLabel compteur = ((JLabel) this.getJMenuBar().getComponents()[2]);
        int val = Integer.parseInt(compteur.getText());
        String newVal = String.valueOf(val-1);
        compteur.setText(newVal);
    }

    void decrementeProgressBar() {
		int limite = this.controleur.getLimite();
        JProgressBar progressBar = ((JProgressBar) this.getJMenuBar().getComponents()[2]);
        int val = progressBar.getValue();
        if(val < (limite/5)){
			if(val%2==0)
				 progressBar.setForeground(Color.red);
			else
				progressBar.setForeground(Color.blue);
        }
        progressBar.setValue(val-1);
        progressBar.setString(val-1 + " L restants");
    }

    void defaite() {
        String[] choices = {"Réessayer!", "Retour au menu"};
        EventQueue.invokeLater(() -> {
            int retour = JOptionPane.showOptionDialog(this, "Vous avez perdu! :(","",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE /* Image personnaliable */, null, choices, choices[0]);
            if (retour == 0) /* retour = 0 = Réessayer */
                this.controleur.retry();
            else /* retour = 1 = Retour au menu */
               this.controleur.backMenu();
        });
    }

    void addCompteur() {
        int limite =  this.controleur.getLimite();
        JLabel counter = new JLabel("" + limite);
        this.getJMenuBar().add(counter);
    }

    void addProgressBar() {
        int limite =  this.controleur.getLimite();
        JProgressBar progressBar = new JProgressBar();
		progressBar.setMaximum(limite);
        progressBar.setValue(limite);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.blue);
        progressBar.setString(limite + " L restantes");
        this.getJMenuBar().add(progressBar);
    }

}

class Niveau extends JPanel {

    public Niveau(int largeur, int hauteur) {
        super();
        EventQueue.invokeLater(() -> {
            this.setLayout(new GridLayout(hauteur, largeur));
            this.setPreferredSize(new Dimension(largeur*200,hauteur*200));
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
        this.image = this.vue.getNextImage(this.x, this.y);
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

    public ClickListener(boolean movable, ImagePane imagePane) {
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

class Menu extends JMenuBar{


    public Menu(){
        super();
    }

    /* FIXME: assez long peut etre factoriser ? */
    static Menu createMenu(Fenetre fenetre, Controleur controleur){
        Menu menuBar = new Menu();

        JMenu charger = new JMenu("Charger");
        JMenuItem niveau1 = new JMenuItem("Niveau 1");
        charger.add(niveau1);

        menuBar.add(charger);
        JButton bouton = new JButton("Sauvegarder");
        bouton.addActionListener((ActionEvent e) -> {
            /** FIXME:le numéro du niveau exporté devrait etre le bon ? **/
            controleur.exportNiveau(0, false);
            JOptionPane.showMessageDialog(fenetre, "Niveau exporté!");
        });
        menuBar.add(bouton);
        return menuBar;
    }

}
