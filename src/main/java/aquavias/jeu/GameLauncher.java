package aquavias.jeu;

public class GameLauncher {

    public static void main(String[] args) {
        boolean test = false;
        for (String s : args) {
            if (s.equals("Test")) test = true;
        }
        Controleur controleur = new Controleur(test);
        controleur.launch();
    }

}
