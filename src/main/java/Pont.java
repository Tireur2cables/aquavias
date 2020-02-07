public class Pont {

    private char forme;
    private char orientation;
    private boolean[] sorties;
    private boolean water;

    public Pont(char forme, char orientation) {
        this.forme = forme;
        this.orientation = orientation;
        this.sorties = calculateSorties();
        this.water = false;
    }

    public boolean[] calculateSorties(){
        boolean[] tab = new boolean[4];
        return tab;
    }

}
