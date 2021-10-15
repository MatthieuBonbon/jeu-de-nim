package modele;

public class Coup {
    private final int nb_allumettes;

    Coup(int num_tas) {
        this.nb_allumettes = (2 * num_tas) - 1;
    }

    public int getNb_allumettes() {
        return nb_allumettes;
    }
}
