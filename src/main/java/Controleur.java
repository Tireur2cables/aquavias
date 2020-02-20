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
    //    System.out.println("Test de l'affichage d'un pont");
        this.affichePont('I', true, 0);
    }

    private void affiche() {
        Pont p = this.jeu.getPont(0,0);
        BufferedImage image = null;
        switch (p.forme){
            case 'I' : image = PontI.pontI;
            break;
        }
        double rotation = 0;
        switch (p.orientation){
            case 'N' : rotation = 0;
            break;
            case 'E' : rotation = 90;
            break;
            case 'S' : rotation = 180;
            break;
            case 'O' : rotation = 270;
            break;
        }
        //this.affichePont(image, rotation);
    }

    private void affichePont(char c, boolean eau, double rotation){
        Pont p = new PontI();
        BufferedImage image = null;
        switch (p.forme){
            case 'I' : image = (eau)?PontI.pontIEau:PontI.pontI;
                break;
        }
        this.graph.affichePont(image, rotation);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
