package com.izaiasvalentim.general.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Companhia;
import com.izaiasvalentim.general.Repository.CompanhiaRepository;

import jakarta.transaction.Transactional;

@Service
public class CompanhiaService {

    private final CompanhiaRepository companhiaRepository;

    @Autowired
    public CompanhiaService(CompanhiaRepository companhiaRepository) {
        this.companhiaRepository = companhiaRepository;
    }

    @Transactional
    public Companhia createCompany(Companhia companhia) {
        if (companhiaRepository.findById(1).isPresent()) {
            throw new ResourceAlreadyExistsException("Sua estabelecimento já foi definido.");
        }
        companhia.setId(1);
        return companhiaRepository.save(companhia);
    }

    public Companhia getCompany() {
        return companhiaRepository.findById(1).orElse(null);
    }

    @Transactional
    public Companhia updateCompany(Companhia companhia) {
        return companhiaRepository.findById(1).map(existingCompanhia -> {
            if (companhia.getName() != null && !companhia.getName().isEmpty()) {
                existingCompanhia.setName(companhia.getName());
            }
            if (companhia.getAddress() != null && !companhia.getAddress().isEmpty()) {
                existingCompanhia.setAddress(companhia.getAddress());
            }
            if (companhia.getBrach() != null && !companhia.getBrach().isEmpty()) {
                existingCompanhia.setBrach(companhia.getBrach());
            }
            if (companhia.getTypeOfService() != null && !companhia.getTypeOfService().isEmpty()) {
                existingCompanhia.setTypeOfService(companhia.getTypeOfService());
            }
            return companhiaRepository.save(existingCompanhia);
        }).orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrato, não é possível atualizar."));
    }

    public void deleteCompany() {
        Companhia existingCompanhia = this.getCompany();

        if (existingCompanhia != null) {
            companhiaRepository.delete(existingCompanhia);
        } else {
            throw new ResourceNotFoundException("O estabelecimento não existe, não é possível remover.");
        }
    }
}
