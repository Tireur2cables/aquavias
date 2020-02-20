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
        this.affiche();
    }

    private void affiche() {
        Pont p = this.jeu.getPont(0,0);
        BufferedImage image = null;
        switch (p.forme){
            case 'I' : image = PontI.pontI;
            break;
        }
        double rotation = (p.orientation == 'N')?0:90;
        this.affichePont(image, rotation);
    }

    private void affichePont(BufferedImage image, double rotation){
        this.graph.affichePont(image, rotation);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
