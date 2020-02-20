public class Controleur {

    private Jeu jeu;
    private VueGraphique graph;

    public Controleur() {
        this.graph = new VueGraphique(this);
    }

    public void launch() {
        this.jeu = new Jeu(this);
        System.out.println("Le jeu se lance!");
        System.out.println("Test de l'affichage d'un pont");
    }

    public void affichePont(char c, boolean eau, double rotation){
        this.graph.affichePont(c, eau, rotation);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
