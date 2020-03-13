package com.ipiecoles.java.java350.controller;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/employes")
public class EmployeController {
    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Employe getEmploye(@PathVariable("id") Long id){
        Optional<Employe> optionalEmploye = employeRepository.findById(id);
        if(optionalEmploye.isPresent()) {
            return optionalEmploye.get();
        }
        throw  new EntityNotFoundException("Employe " + id + " introuvable");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleENFException(EntityNotFoundException e){
        return e.getMessage();
    }
}
