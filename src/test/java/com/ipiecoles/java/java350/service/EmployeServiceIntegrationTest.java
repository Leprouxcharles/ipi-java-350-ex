package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@SpringBootTest
public class EmployeServiceIntegrationTest {


    @Autowired
    EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void integrationEmbaucheEmploye() throws EmployeException {
        //Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        Employe employe = employeRepository.findByMatricule("T12346");
        Assertions.assertNotNull(employe);
        Assertions.assertEquals(nom, employe.getNom());
        Assertions.assertEquals(prenom, employe.getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employe.getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("T12346", employe.getMatricule());
        Assertions.assertEquals(1.0, employe.getTempsPartiel().doubleValue());

        //1521.22 * 1.2 * 1.0
        Assertions.assertEquals(1825.46, employe.getSalaire().doubleValue());
    }


    @Test
    public void integrationEmployeGagnantMoinsQue() throws EmployeException {
        //Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Rib", "Samuel", "T12346", LocalDate.now(), Entreprise.SALAIRE_BASE +20.00, 1, 1.0));
        employeRepository.save(new Employe("Simon", "Jc", "T12348", LocalDate.now(), Entreprise.SALAIRE_BASE -20.00, 1, 1.0));

        //When
        List<Employe> employeList = employeService.EmployeGagnantMoinsQue("T12345");

        //Then
        Employe e = employeRepository.findByMatricule("T12348");

        Assertions.assertTrue(1 == employeList.size());
        Assertions.assertEquals(e, employeList.get(0));
    }

    @ParameterizedTest
    @CsvSource({
            "10000, 12000, 4, 3",
            "12000, 12000, 2, 3",
            "13200, 12000, 2, 4",
            "15500, 12000, 3, 8"
    })
    public void integrationPerformanceCommercial(Long caTraite, Long objectifCa, Integer performance, Integer performanceFinal) throws EmployeException {
        //Given
        employeRepository.save(new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Rib", "Samuel", "C12346", LocalDate.now(), Entreprise.SALAIRE_BASE +20.00, 1, 1.0));
        employeRepository.save(new Employe("Simon", "Jc", "C12347", LocalDate.now(), Entreprise.SALAIRE_BASE -20.00, 1, 1.0));

        Employe e1 = new Employe("Thomas", "Anderson","C12344",
                LocalDate.now(),1500d,performance,1.0);
        employeRepository.save(e1);

        //When
        employeService.calculPerformanceCommercial(e1.getMatricule(),caTraite,objectifCa);

        //Then
        Employe e = employeRepository.findByMatricule("C12344");
        Assertions.assertEquals(e.getPerformance(), performanceFinal);
    }

}
