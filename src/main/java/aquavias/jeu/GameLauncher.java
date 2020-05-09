package aquavias.jeu;

public class GameLauncher {

    public static void main(String[] args) {
        boolean test = false;
        for (String s : args) {
            if (s.equals("test")) {
                test = true;
                break;
            }
        }
        Controleur controleur = new Controleur(test);
        if (!test) controleur.launch();
    }

}
