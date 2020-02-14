public class VueTerminale {

    private Controleur controleur;

    public VueTerminale(Controleur controleur) {
        this.controleur = controleur;
    }

    public void affiche() {
        System.out.println("Le jeu se lance!");
    }

}
