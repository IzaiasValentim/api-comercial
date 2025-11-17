package com.izaiasvalentim.general.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Cliente;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientDTO;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientRegisterDTO;
import com.izaiasvalentim.general.Repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void requestRegistration(ClientRegisterDTO clientDTO) {

        if (clienteRepository.findByIdentificationNumber(clientDTO.getIdentificationNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("Já existe um cliente com este mesmo CPF.");
        }

        Cliente clienteToRegister = clientDTO.registerDTOToClient();
        clienteToRegister.setDeleted(false);
        clienteRepository.save(clienteToRegister);

    }

    public ClientRegisterDTO approveClientRegistration(String identificationNumber) {
        Cliente clienteToApprove = clienteRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cliente com este CPF não existe."));

        clienteToApprove.setActive(true);
        clienteRepository.save(clienteToApprove);
        return new ClientRegisterDTO(clienteToApprove);
    }

    public List<ClientDTO> findClientsByNameAndStatus(String name, Boolean active) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> clients = clienteRepository.findDistinctByNameContainingAndActive(name, active, pageable);

        return clients.getContent().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    public Cliente findByIdentificationNumber(String identificationNumber) {
        return clienteRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nenhum cliente encontrado com o CPF fornecido."));
    }

    public void updateRegistration(ClientRegisterDTO clientDTO) {
        Cliente existingCliente = clienteRepository
                .findByIdentificationNumber(clientDTO.getIdentificationNumber())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Nenhum cliente encontrado com o CPF fornecido."));

        if (clientDTO.getName() != null) {
            existingCliente.setName(clientDTO.getName());
        }

        if (clientDTO.getEmail() != null) {
            existingCliente.setEmail(clientDTO.getEmail());
        }
        if (clientDTO.getAddress() != null) {
            existingCliente.setAddress(clientDTO.getAddress());
        }
        if (clientDTO.getPhoneNumber() != null) {
            existingCliente.setPhoneNumber(clientDTO.getPhoneNumber());
        }
        if (clientDTO.getPhoneNumberReserve() != null) {
            existingCliente.setPhoneNumberReserve(clientDTO.getPhoneNumberReserve());
        }
        if (clientDTO.getPayment() != null) {
            existingCliente.setPayment(clientDTO.getPayment());
        }

        clienteRepository.save(existingCliente);
    }

    public void logicalDeleteClient(String identificationNumber) {
        Cliente clienteToDelete = clienteRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nenhum cliente encontrado com o CPF fornecido."));

        clienteToDelete.setActive(false);
        clienteToDelete.setDeleted(true);
        clienteRepository.save(clienteToDelete);
    }
}
