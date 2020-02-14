public class Controleur {

    private Jeu jeu;
    private VueTerminale term;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
        this.term = new VueTerminale(this);
    }

    public void launch() {
        this.jeu = new Jeu(this);
        System.out.println("Le jeu se lance!");
    }

}
