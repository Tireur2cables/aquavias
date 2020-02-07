public class Vue_Terminale {

    private Controller controller;

    public Vue_Terminale(Controller controller) {
        this.controller = controller;
    }

    public void affiche() {
        System.out.println("Le jeu se lance!");
    }

}
