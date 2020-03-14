import java.awt.*;
import java.awt.image.BufferedImage;
public class VueGraphique {

    private Controleur controleur;
    private Fenetre fenetre;
    private Niveau niveau;
    private PontGraph[][] plateau;

    public VueGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.fenetre = new Fenetre(controleur);
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

    public void initNiveau(int hauteur, int largeur) {
        this.niveau = new Niveau(hauteur, largeur);
        this.initPlateau(hauteur, largeur);
    }

    public void initPlateau(int hauteur, int largeur){
        for(int i = 0; i < largeur; i++){
            for(int j = 0; j < hauteur; j++){
                this.plateau[i][j] = this.getPontGraphique(i, j);
            }
        }
    }

    public PontGraph getPontGraphique(int i, int j){
        Pont p = this.controleur.getPont(i, j);
        PontGraph newP = null;
        if(p == null) return newP;
        else{
            switch (p.getForme()){
                case 'I' : newP = new PontIGraph(p.orientation);
                    break;
                case 'L' : newP = new PontLGraph(p.orientation);
                    break;
                case 'T' : newP = new PontTGraph(p.orientation);
                    break;
            }
        }
        return newP;
    }
	/**
	* Recupère le plateau Graphique et l'affiche, ainsi que les différents modes de jeu
	* */
    public void afficheNiveau() {
        EventQueue.invokeLater(() -> {
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
    public void addToPlateau(BufferedImage image, boolean movable, int x, int y) {
        EventQueue.invokeLater(() -> {
            this.niveau.add(new ImagePane(image, movable, this.controleur, x, y));
        });
    }

	/**
	*   Met à jour l'image a la position x,y avec la nouvelle image image
	* */
    public void actualiseImage(BufferedImage image, int x, int y) {
        int largeur = ((GridLayout) this.niveau.getLayout()).getColumns();
        int indice = y+x*largeur;
        ((ImagePane) this.niveau.getComponents()[indice]).setImage(image);
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

    void decrementeProgressBar() {
        this.fenetre.decrementeProgressBar();
    }

}
