package com.izaiasvalentim.general.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Communication;
import com.izaiasvalentim.general.Domain.DTO.Communication.CommunicationUpdateDTO;
import com.izaiasvalentim.general.Repository.CommunicationRepository;

@Service
public class CommunicationService {

    private final CommunicationRepository communicationRepository;

    @Autowired
    public CommunicationService(CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    public Communication registerNewCommunication(Communication communication) {
        try {
            return communicationRepository.save(communication);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    public List<Communication> getAllValidCommunications(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return communicationRepository.findByIsDeletedFalseAndEndDateAfter(today, pageable).getContent();
    }

    public List<Communication> getAllCommunications(Pageable pageable) {
        return communicationRepository.findByIsDeletedFalse(pageable).getContent();
    }

    public Communication updateCommunication(CommunicationUpdateDTO updateDto) {
        Communication communication = communicationRepository.findById(updateDto.id()).orElse(null);

        if (communication == null) {
            throw new ResourceNotFoundException("Comunicado de id " + updateDto.id() + " não encontrado.");
        }

        if (updateDto.message() != null && !updateDto.message().isEmpty()) {
            communication.setMessage(updateDto.message());
        }
        if (updateDto.scope() >= 0) {
            communication.setScope(updateDto.scope());
        }
        if (updateDto.endDate() != null && updateDto.endDate().isAfter(communication.getEndDate())) {
            communication.setEndDate(updateDto.endDate());
        }
        return communicationRepository.save(communication);
    }

    public Communication endCommunication(long id) {
        Communication communicationToEnd = communicationRepository.findById(id).orElse(null);
        if (communicationToEnd == null) {
            throw new ResourceNotFoundException("Comunicado de id " + id + " não encontrado.");
        }
        LocalDate nowDate = LocalDate.now();
        communicationToEnd.setEndDate(nowDate);

        return communicationRepository.save(communicationToEnd);
    }

    public Communication deleteCommunication(long id) {
        Communication communicationToDelete = communicationRepository.findById(id).orElse(null);
        if (communicationToDelete == null) {
            throw new ResourceNotFoundException("Comunicado de id " + id + " não encontrado.");
        }
        communicationToDelete.setDeleted(true);
        return communicationRepository.save(communicationToDelete);
    }
}
