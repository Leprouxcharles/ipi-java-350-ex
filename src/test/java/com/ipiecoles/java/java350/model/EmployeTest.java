package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static com.ipiecoles.java.java350.model.Entreprise.SALAIRE_BASE;

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
    public void testGetPrimeAnnuelle(String matricule, Integer nbAnneeAnciennte, Double tempsPartiel, Integer performance, Double primeFinal) throws EmployeException {
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


    @ParameterizedTest
    @CsvSource({
            "1521.22, 1",
            "760.61, 0.5"
    })
    public void testTempsPartiel(Double salaireFinal, Double tempsPartielFinal) throws EmployeException {

        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();

        employe.setTempsPartiel(tempsPartielFinal);

        //When = Exécution de la méthode à tester
        Double tempsPartiel = employe.getTempsPartiel();
        Double salaire = employe.getSalaire();


        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(tempsPartiel).isEqualTo(tempsPartielFinal);
        Assertions.assertThat(salaire).isEqualTo(salaireFinal);
    }



    @ParameterizedTest
    @CsvSource({
            "2200.0, 0.1",
            "2000.0, 0.15",
    })
    public void testaugmenterSalaire(Double salaireFinal, Double pourcentage) throws Exception {

        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setSalaire(2000.0);

        //When = Exécution de la méthode à tester
        Double salaire = employe.augmenterSalaire(pourcentage);

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(salaire).isEqualTo(salaireFinal);
    }

    @ParameterizedTest
    @CsvSource({
            "2000.0, 0"
    })
    public void testaugmenterSalairePoucentageNull(Double salaireFinal, Double pourcentage) throws Exception {

        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setSalaire(2000.0);


        //When = Exécution de la méthode à tester
        Throwable exception = Assertions.catchThrowable(() ->
                employe.augmenterSalaire(pourcentage));

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(exception).isInstanceOf(Exception.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Poucentage ne peut pas être null");
    }


    @ParameterizedTest
    @CsvSource({
            "2019-01-01, 8, 1",
            "2021-01-01, 10, 1",
            "2022-01-01, 10, 1",
            "2032-01-01, 11, 1",
            "2022-01-01, 5, 0.5",
            "2032-01-01, 6, 0.5",
    })
    public void testgetNbRtt(LocalDate date, Integer nbRTFinal, Double tempsPartiel ) throws EmployeException {

        //Given = Initialisation des données d'entrée
        Employe employe = new Employe();
        employe.setTempsPartiel(tempsPartiel);

        //When = Exécution de la méthode à tester
        Integer nbRtt = employe.getNbRtt(date);

        //Then = Vérifications de ce qu'a fait la méthode
        Assertions.assertThat(nbRtt).isEqualTo(nbRTFinal);
    }

}
