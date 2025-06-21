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
import com.izaiasvalentim.general.Domain.Client;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientDTO;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientRegisterDTO;
import com.izaiasvalentim.general.Repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void requestRegistration(ClientRegisterDTO clientDTO) {

        if (clientRepository.findByIdentificationNumber(clientDTO.getIdentificationNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("Já existe um cliente com este mesmo CPF.");
        }

        Client clientToRegister = clientDTO.registerDTOToClient();
        clientToRegister.setDeleted(false);
        clientRepository.save(clientToRegister);

    }

    public ClientRegisterDTO approveClientRegistration(String identificationNumber) {
        Client clientToApprove = clientRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cliente com este CPF não existe."));

        clientToApprove.setActive(true);
        clientRepository.save(clientToApprove);
        return new ClientRegisterDTO(clientToApprove);
    }

    public List<ClientDTO> findClientsByNameAndStatus(String name, Boolean active) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> clients = clientRepository.findDistinctByNameContainingAndActive(name, active, pageable);

        return clients.getContent().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    public Client findByIdentificationNumber(String identificationNumber) {
        return clientRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nenhum cliente encontrado com o CPF fornecido."));
    }

    public void updateRegistration(ClientRegisterDTO clientDTO) {
        Client existingClient = clientRepository
                .findByIdentificationNumber(clientDTO.getIdentificationNumber())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Nenhum cliente encontrado com o CPF fornecido."));

        if (clientDTO.getName() != null) {
            existingClient.setName(clientDTO.getName());
        }

        if (clientDTO.getEmail() != null) {
            existingClient.setEmail(clientDTO.getEmail());
        }
        if (clientDTO.getAddress() != null) {
            existingClient.setAddress(clientDTO.getAddress());
        }
        if (clientDTO.getPhoneNumber() != null) {
            existingClient.setPhoneNumber(clientDTO.getPhoneNumber());
        }
        if (clientDTO.getPhoneNumberReserve() != null) {
            existingClient.setPhoneNumberReserve(clientDTO.getPhoneNumberReserve());
        }
        if (clientDTO.getPayment() != null) {
            existingClient.setPayment(clientDTO.getPayment());
        }

        clientRepository.save(existingClient);
    }

    public void logicalDeleteClient(String identificationNumber) {
        Client clientToDelete = clientRepository
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nenhum cliente encontrado com o CPF fornecido."));

        clientToDelete.setActive(false);
        clientToDelete.setDeleted(true);
        clientRepository.save(clientToDelete);
    }
}
