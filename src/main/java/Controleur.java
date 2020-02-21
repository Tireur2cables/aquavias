import java.awt.image.BufferedImage;

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
        this.affichePont('I', true, 0);
    }

    private void affichePont(char c, boolean eau, double rotation){
        BufferedImage image = null;
        switch (c) {
            case 'I' :
                image = (eau)? PontI.pontIEau : PontI.pontI;
                break;
            case 'T' :
                image = (eau)? PontT.pontTEau : PontT.pontT;
                break;
            case 'L' :
                image = (eau)? PontL.pontLEau : PontL.pontL;
                break;
        }
        if (image == null) throw new RuntimeException("Affichage du pont incorrect!");
        this.graph.affichePont(image, rotation);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
