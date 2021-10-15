package modele;

public class Tas implements Cloneable {
    private final int[] nb_allumettes_par_tas;
    private final int nbTas;

    public Tas(int nbTas) {
        this.nbTas = nbTas;
        this.nb_allumettes_par_tas = new int[nbTas]; // On alloue un espace mémoire correspondant au nombre de tas choisi
        for (int j = 0; j < nbTas; j++) {
            nb_allumettes_par_tas[j] = new Coup(j + 1).getNb_allumettes();
        }
    }

    public int[] getNb_allumettes_par_tas() {
        return nb_allumettes_par_tas;
    }

    @Override
    public Object clone()  {
        return new Tas(this.nbTas);
    }
}
