package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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


    @ParameterizedTest
    @CsvSource({
            "'T12345', 0, 1.0, 1, 1000,0",
            "'T12345', 0, 0.5, 1, 500,0"
    })
    public void testGetPrimeAnnuelle(String matricule, Integer nbAnneeAnciennte, Double tempsPartiel, Integer performance, Double primeFinal){
        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setMatricule(matricule);
        employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneeAnciennte));
        employe.setTempsPartiel(tempsPartiel);
        employe.setPerformance(performance);

        //When = Exécution de la méthode à tester
        Double prime = employe.getPrimeAnnuelle();

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(prime).isEqualTo(primeFinal);
    }


   /* @ParameterizedTest(name = "immat {0} est valide : {1}")
    @CsvSource({
            "'XXXXX', false",
            "'AA-123-BB', true"
    })
    void testCheckBadImmatriculation(String immat, Boolean result) {
        //Given, When, Then
        Employe employe = new Employe();
        Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(result);
    }*/

}
