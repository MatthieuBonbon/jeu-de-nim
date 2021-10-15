package controleur;

import modele.Joueur;
import modele.Tas;
import vue.Ihm;

public class ControleurJeuIA {

    private final Ihm ihm;
    private final Tas tas;
    private int[] nb_allumettes_par_tas;
    private boolean limiteAllumettes;
    private int nbLimiteParCoup;
    private Tas tasClone;
    private Joueur joueur1;
    private Joueur joueur2;


    public ControleurJeuIA(Ihm ihm, Tas tas, boolean limiteAllumettes, int nbLimiteParCoup) {
        this.ihm = ihm;
        this.tas = tas;
        this.limiteAllumettes = limiteAllumettes;
        this.nbLimiteParCoup = nbLimiteParCoup;
    }

    public void commencerJeu() {
        // On clone le tas
        clonerTas();


        // Création des joueurs
        String nomJoueur1 = ihm.interactionCreationJoueurs("Joueur 1");

        joueur1 = new Joueur(nomJoueur1);
        joueur2 = new Joueur("IA");

        gestionJeu();
    }


    private void clonerTas() {
        // Cette méthode permet de cloner le tas
        tasClone = (Tas)tas.clone();
        this.nb_allumettes_par_tas = tasClone.getNb_allumettes_par_tas();
    }


    private void demanderAffichageTas(){
        //Cette méthode demande l'affichage des tas
        for(int i = 0; i < nb_allumettes_par_tas.length; i++){
            ihm.affichageAllumettes(nb_allumettes_par_tas[i]);
        }
    }


    private void enleverAllumettes(String nom) {
        // Cette méthode permet d'enlever des allumettes

        String resultatInteraction = ihm.interactionEnleverAllumettes(nom);
        int i = 0;
        int j = 0;

        for (int k = 0; k <resultatInteraction.length(); k++){

            if(resultatInteraction.substring(k, (k + 1)).equals(" ")){

                i = Integer.parseInt(resultatInteraction.substring(0,k))-1;
                j = Integer.parseInt(resultatInteraction.substring(k+1));
                break;
            }
        }

        if ((i + 1) <= nb_allumettes_par_tas.length && (i + 1) > 0) {


            if (j <= nb_allumettes_par_tas[i] && j > 0) {
            /* On vérifie qu'il y est au minimum autant d'allumettes dans le tas
               que le nombre d'allumettes que le joueur souhaite retirer */

                if (limiteAllumettes) {
                    // Si les joueurs décident d'une limite d'allumettes
                    if (nbLimiteParCoup >= j) {
                        nb_allumettes_par_tas[i] -= j;
                    } else {
                        // Si le nombre limite d'allumettes par coup n'est pas respecté
                        ihm.errSupNbAllumettes();
                        enleverAllumettes(nom);
                    }
                } else {
                    nb_allumettes_par_tas[i] -= j;
                }
            } else {
                // Si le nombre limite d'allumettes par coup n'est pas respecté
                ihm.errSupNbAllumettes();
                enleverAllumettes(nom);
            }
        }
        else{
            // Si le nombre limite d'allumettes par coup n'est pas respecté
            ihm.errSupNbAllumettes();
            enleverAllumettes(nom);
        }
    }

    private void jouerUnCoup(String nom){
        // Cette méthode permet de jouer un coup
        enleverAllumettes(nom); // Enlève une ou plusieurs allumettes
        demanderAffichageTas(); // Demande d'afficher l'état des tas
    }

    private int calculerXOR(){
        int calculXOR;
        calculXOR = 0;
        for (int i = 0; i <nb_allumettes_par_tas.length; i++){
            calculXOR^=nb_allumettes_par_tas[i]; // affectation + XOR
        }
        return calculXOR;
    }

    private void jouerUnCoupIA() {

        int resultatXOR;
        resultatXOR = calculerXOR();


        // Stratégie situation perdante

        // /!\ On ne considère pas ici si une limite d'allumettes existe
        // car si c'est le cas, elle est au minimum égale à 1
        // et n'influe donc pas sur cette partie du code /!\

        if (resultatXOR == 0) {

            // On cherche le premier tas contenant au moins une allumette
            for (int i = 0; i < nb_allumettes_par_tas.length; i++) {

                // Si le tas à l'index i contient au moins une allumette
                if (nb_allumettes_par_tas[i] >= 1) {

                    ihm.affichageCoupIA(i + 1, 1);     // On affichage le coup de l'IA
                    nb_allumettes_par_tas[i] -= 1;    // On décrémente de 1 le nombre d'allumettes
                    break;      // et on sort de la boucle
                }
            }
        }

            // Stratégie situation gagnante (resultatXOR != 0)
            else {

                // S'il n'y a pas de limite d'allumettes
                if(!limiteAllumettes){
                    // On cherche le premier tas où
                    // nb allumettes > (nb allumettes ^ resultatXOR)
                    for (int j = 0; j < nb_allumettes_par_tas.length; j++) {

                        // Si le tas à l'index j est valide
                        if ((nb_allumettes_par_tas[j] ^ resultatXOR) < nb_allumettes_par_tas[j]) {

                            ihm.affichageCoupIA(j + 1,    // On affiche le coup de l'IA
                                    (nb_allumettes_par_tas[j] - (nb_allumettes_par_tas[j] ^ resultatXOR)));

                            nb_allumettes_par_tas[j] -= (nb_allumettes_par_tas[j]     // On décrémente le nombre d'allumettes
                                    - (nb_allumettes_par_tas[j] ^ resultatXOR));

                            break;      // et on sort de la boucle
                        }
                    }
                }

                // S'il y a une limite d'allumettes
                else{

                    for(int k = 0; k < nb_allumettes_par_tas.length; k++){

                        if((nb_allumettes_par_tas[k] ^ resultatXOR) < nb_allumettes_par_tas[k]){

                            ihm.affichageCoupIA(k+1,    // On affiche le coup de l'IA
                                    (nb_allumettes_par_tas[k] % (nbLimiteParCoup+1)));

                            nb_allumettes_par_tas[k] -=     // On décrémente le nombre d'allumettes
                                    (nb_allumettes_par_tas[k] % (nbLimiteParCoup+1));

                            break;      // et on sort de la boucle
                        }
                    }
                }
            }
        demanderAffichageTas();
    }




    private boolean tasVides(){
        // Permet de savoir si les tas sont vides

        int nbAllumettesRestantes = 0;

        // On compte le nombre d'allumettes restantes au total
        for(int i = 0; i<nb_allumettes_par_tas.length; i++){
            nbAllumettesRestantes += nb_allumettes_par_tas[i];
        }

        // Si il n'y a plus d'allumettes
        if(nbAllumettesRestantes == 0){
            return true;
        }
        else {
            return false;
        }
    }


    private void finJeu(){
        // Gère la fin du jeu

        ihm.affichageFinal(joueur1.getNom(),
                joueur2.getNom(),
                joueur1.getNb_parties_gagnees(),
                joueur2.getNb_parties_gagnees());
    }


    private void gestionJeu() {
        /* Cette méthode regroupe les différentes
           méthodes précédentes afin d'assurer la gestion du jeu */

        boolean joueur1Joue;    // On définie et initialise la variable booléenne qui
        joueur1Joue = true;     // va déterminer quel joueur doit jouer le prochain coup

        demanderAffichageTas(); // On demande l'affichage initial des tas

        while (!tasVides()){    // Tant que le tas n'est pas vide
            if(joueur1Joue){    // Le joueur 1 joue
                jouerUnCoup(joueur1.getNom());
                joueur1Joue = false;
            }
            else{   // Le joueur 2 joue
                jouerUnCoupIA();
                joueur1Joue = true;
            }
        }

        if (!joueur1Joue){              // appelle la méthode de la classe Joueur
            joueur1.partieGagnee();     // qui incrémente le nombre de parties gagnées

            if(ihm.interactionFinirJouer(joueur1.getNom())){    // Si les joueurs décident de rejouer
                clonerTas();
                gestionJeu();
            }
            else{   // Sinon on appelle la méthode qui gère la fin du jeu
                finJeu();
            }

        }
        else{
            joueur2.partieGagnee();

            if(ihm.interactionFinirJouer(joueur2.getNom())){    // Si les joueurs décident de rejouer
                clonerTas();
                gestionJeu();
            }
            else{   // Sinon on appelle la méthode qui gère la fin du jeu
                finJeu();
            }

        }


    }
}