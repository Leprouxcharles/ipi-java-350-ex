package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class EmployeTest {

    //date d'embauche
    // 1 : null => 0
    // 2 : > auj + 2 ans => 0
    // 3 : < auj - 3  ans => 3
    // 4 : auj  => 0

    @Test
    public void testGetNombreAnneeAncienneteDtEmbaucheNull(){
        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When = Exécution de la méthode à tester
        Integer nombreAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(nombreAnneeAnciennete).isEqualTo(0);
    }

    @Test
    public void testGetNombreAnneeAncienneteDtEmbaucheToday(){
        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When = Exécution de la méthode à tester
        Integer nombreAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(nombreAnneeAnciennete).isEqualTo(0);
    }


    @Test
    public void testGetNombreAnneeAncienneteDtEmbaucheTodayPlus2Y(){
        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));

        //When = Exécution de la méthode à tester
        Integer nombreAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(nombreAnneeAnciennete).isEqualTo(0);
    }

    @Test
    public void testGetNombreAnneeAncienneteDtEmbaucheTodayMinus3Y(){
        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(3));

        //When = Exécution de la méthode à tester
        Integer nombreAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(nombreAnneeAnciennete).isEqualTo(3);
    }



}
