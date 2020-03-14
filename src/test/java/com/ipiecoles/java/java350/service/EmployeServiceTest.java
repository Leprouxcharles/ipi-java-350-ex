package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {


    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this.getClass());
    }


    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("00344");

        //When
        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

        //Then
        Mockito.verify(employeRepository).findByMatricule("T00345");
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00345");
        //1521.22 == 1825.464
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
    }


   @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBtsLimitMat() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When
       try {
           employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
           Assertions.fail("Une exception aurait du être levé");
       } catch (EmployeException e) {
           //Then
           Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
       }

       /* OR :
       //When
       Throwable exception = Assertions.catchThrowable(() ->
               employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel));

       //Then
       Assertions.assertThat(exception).isInstanceOf(EmployeException.class);
       Assertions.assertThat(exception.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        */

    }


    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBtsExistingEmploye() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(new Employe());

       //When
       Throwable exception = Assertions.catchThrowable(() ->
               employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel));

       //Then
       Assertions.assertThat(exception).isInstanceOf(EntityExistsException.class);
       Assertions.assertThat(exception.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");



    }


    @Test
    public void testCalculSalireMoyen() throws Exception {
        //Given
        Mockito.when(employeRepository.count()).thenReturn(10l);
        Mockito.when(employeRepository.sumSalaire()).thenReturn(10000d);
        Mockito.when(employeRepository.sumTempsPartiel()).thenReturn(10d);

        //When
        Double salaireMoyen = employeService.calculSalaireMoyenETP();

        //Then
        Assertions.assertThat(salaireMoyen).isEqualTo(1000d);
    }


    @ParameterizedTest
    @CsvSource({
            "10000, 12000, 1, 2",
            "12000, 12000, 2, 3",
            "13200, 12000, 2, 4",
            "15500, 12000, 3, 8"
    })
    public void testCalculPerformanceCommercial(Long caTraite, Long objectifCa, Integer performance, Integer performanceFinal) throws EmployeException {
        //Given
        Employe e1 = new Employe("Doe", "John","C12345",
                LocalDate.now(),1500d,performance,1.0);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(0.5);
        Mockito.when(employeRepository.findByMatricule("C12345")).thenReturn(e1);

        //When
        employeService.calculPerformanceCommercial(e1.getMatricule(),caTraite,objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performanceFinal);
    }

    @ParameterizedTest
    @CsvSource({
            "'C12345', , 12000, 1, 'Le chiffre d''affaire traité ne peut être négatif ou null !'",
            "'C12345',-10000 , 12000, 1, 'Le chiffre d''affaire traité ne peut être négatif ou null !'",
            "'C12345',10000 , , 1, 'L''objectif de chiffre d''affaire ne peut être négatif ou null !'",
            "'C12345',10000 , -12000 , 1, 'L''objectif de chiffre d''affaire ne peut être négatif ou null !'",
            "'T12345',10000 , 12000 , 1, 'Le matricule ne peut être null et doit commencer par un C !'",
            " ,10000 , 12000 , 1, 'Le matricule ne peut être null et doit commencer par un C !'",
            "'C12345', 10000 , 12000, 1, 'Le matricule C12345 n''existe pas !'",

    })
    public void testCalculPerformanceCommercialException(String matricule, Long caTraite, Long objectifCa, Integer performance, String errorMessage) throws EmployeException {
        //Given

        //When
        Throwable exception = Assertions.catchThrowable(() ->
                employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa));

        //Then
        Assertions.assertThat(exception).isInstanceOf(EmployeException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }



}
