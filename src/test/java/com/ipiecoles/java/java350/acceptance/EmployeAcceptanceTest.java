package com.ipiecoles.java.java350.acceptance;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import com.thoughtworks.gauge.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;


@Component
public class EmployeAcceptanceTest {

    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    EmployeService employeService;

    @BeforeScenario
    @AfterScenario
    public void before(){
        employeRepository.deleteAll();
    }

    @Step("J'embauche une personne appelée <prenom> <nom> diplômée d'un <diplome> en tant que <poste> à <txActivite>.")
    public void embauche(String prenom, String nom, String diplome, String poste, String txActivite) throws EmployeException {
        NiveauEtude niveauEtude = NiveauEtude.valueOf(diplome.toUpperCase());
        Poste posteEmp = Poste.valueOf(poste.toUpperCase());
        Double tempsActivite = 1.0;
        if (txActivite.equals("mi-temps")) {
            tempsActivite = 0.5;
        }
        employeService.embaucheEmploye(nom, prenom, posteEmp, niveauEtude, tempsActivite);
    }


    @Step("Soit un employé appelé <prenom> <nom> de matricule <matricule> et performance <performance>.")
    public void implementation1(String prenom, String nom, String matricule, Integer performance) {
        Employe employe = new Employe();
        employe.setPrenom(prenom);
        employe.setNom(nom);
        employe.setMatricule(matricule);
        employe.setPerformance(performance);
        employeRepository.save(employe);
    }

    @Step("J'obtiens bien un nouvel employé appelé <John> <Doe> portant le matricule <T00001> et touchant un salaire de <1521.22>€.")
    public void implmentation2(String prenom, String nom, String matricule, Double salaire) {
        Employe employe = employeRepository.findByMatricule(matricule);
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(salaire);
    }

    @Step("je calcule la performance de l'employe de matricule <matricule> pour un chiffre d'affaire de <caTraite> € et pour un Objectif de <objectifCa> €.")
    public void implementation2(String matricule, Long caTraite, Long objectifCa) throws EmployeException {
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
    }

    @Step("j'obtient la mise à jour l'employe de matricule <matricule> avec une performance de <performance>.")
    public void implementation3(String matricule, Integer performance) {
        Employe employe = employeRepository.findByMatricule(matricule);
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performance);
    }
}

