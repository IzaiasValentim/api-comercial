package com.izaiasvalentim.general.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Company;
import com.izaiasvalentim.general.Repository.CompanyRepository;

import jakarta.transaction.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Company createCompany(Company company) {
        if (companyRepository.findById(1).isPresent()) {
            throw new ResourceAlreadyExistsException("Sua estabelecimento já foi definido.");
        }
        company.setId(1);
        return companyRepository.save(company);
    }

    public Company getCompany() {
        return companyRepository.findById(1).orElse(null);
    }

    @Transactional
    public Company updateCompany(Company company) {
        return companyRepository.findById(1).map(existingCompany -> {
            if (company.getName() != null && !company.getName().isEmpty()) {
                existingCompany.setName(company.getName());
            }
            if (company.getAddress() != null && !company.getAddress().isEmpty()) {
                existingCompany.setAddress(company.getAddress());
            }
            if (company.getBrach() != null && !company.getBrach().isEmpty()) {
                existingCompany.setBrach(company.getBrach());
            }
            if (company.getTypeOfService() != null && !company.getTypeOfService().isEmpty()) {
                existingCompany.setTypeOfService(company.getTypeOfService());
            }
            return companyRepository.save(existingCompany);
        }).orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrato, não é possível atualizar."));
    }

    public void deleteCompany() {
        Company existingCompany = this.getCompany();

        if (existingCompany != null) {
            companyRepository.delete(existingCompany);
        } else {
            throw new ResourceNotFoundException("O estabelecimento não existe, não é possível remover.");
        }
    }
}
