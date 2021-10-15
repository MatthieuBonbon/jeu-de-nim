package controleur;

import modele.Joueur;
import modele.Tas;
import vue.Ihm;

public class ControleurJeu {
    private final Ihm ihm;
    private final Tas tas;
    private Tas tasClone;
    private int[] nb_allumettes_par_tas;
    private boolean limiteAllumettes;
    private int nbLimiteParCoup;
    private Joueur joueur1;
    private Joueur joueur2;

    public ControleurJeu(Ihm ihm, Tas tas) {
        this.ihm = ihm;
        this.tas = tas;
    }



    public void commencerJeu() {
        // On clone le tas
        clonerTas();

        // Choix d'une limite d'allumettes par coup
        nbLimiteParCoup = ihm.interactionChoixLimiteAllumettes();
        if(nbLimiteParCoup == 0){
            limiteAllumettes = false;
        }
        else{
            limiteAllumettes = true;
        }

        // Permet au joueur de choisir s'il veut jouer contre l'IA ou un autre joueur
        if(ihm.interactionChoixIA()){
            ControleurJeuIA controleurJeuIA = new ControleurJeuIA(ihm, tas, limiteAllumettes, nbLimiteParCoup);
            controleurJeuIA.commencerJeu();
        }
        else{
            commencerJeu1v1();
        }

    }

    private void commencerJeu1v1(){

        // Création des joueurs
        String nomJoueur1 = ihm.interactionCreationJoueurs("Joueur 1");
        String nomJoueur2 = ihm.interactionCreationJoueurs("Joueur 2");

        joueur1 = new Joueur(nomJoueur1);
        joueur2 = new Joueur(nomJoueur2);

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
                jouerUnCoup(joueur2.getNom());
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
