public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        this.jeu = new Jeu(this);
        System.out.println("Le jeu se lance!");
    }

    public Jeu getJeu() {
        return jeu;
    }
}
