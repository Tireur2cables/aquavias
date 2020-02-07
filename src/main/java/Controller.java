public class Controller {

    private Jeu jeu;
    private Vue_Terminale term;
    private Vue_Graphique graph;

    public Controller() {
        this.graph = new Vue_Graphique(this);
        this.term = new Vue_Terminale(this);
    }

    public void launch() {
        this.jeu = new Jeu();
        this.term.affiche();
    }

}
