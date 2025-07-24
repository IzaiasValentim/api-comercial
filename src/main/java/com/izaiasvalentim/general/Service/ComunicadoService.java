package com.izaiasvalentim.general.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Comunicado;
import com.izaiasvalentim.general.Domain.DTO.Communication.CommunicationUpdateDTO;
import com.izaiasvalentim.general.Repository.CommunicadoRepository;

@Service
public class ComunicadoService {

    private final CommunicadoRepository communicadoRepository;

    @Autowired
    public ComunicadoService(CommunicadoRepository communicadoRepository) {
        this.communicadoRepository = communicadoRepository;
    }

    public Comunicado registerNewCommunication(Comunicado comunicado) {
        try {
            return communicadoRepository.save(comunicado);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    public List<Comunicado> getAllValidCommunications(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return communicadoRepository.findByIsDeletedFalseAndEndDateAfter(today, pageable).getContent();
    }

    public List<Comunicado> getAllCommunications(Pageable pageable) {
        return communicadoRepository.findByIsDeletedFalse(pageable).getContent();
    }

    public Comunicado updateCommunication(CommunicationUpdateDTO updateDto) {
        Comunicado comunicado = communicadoRepository.findById(updateDto.id()).orElse(null);

        if (comunicado == null) {
            throw new ResourceNotFoundException("Comunicado de id " + updateDto.id() + " não encontrado.");
        }

        if (updateDto.message() != null && !updateDto.message().isEmpty()) {
            comunicado.setMessage(updateDto.message());
        }
        if (updateDto.scope() >= 0) {
            comunicado.setScope(updateDto.scope());
        }
        if (updateDto.endDate() != null && updateDto.endDate().isAfter(comunicado.getEndDate())) {
            comunicado.setEndDate(updateDto.endDate());
        }
        return communicadoRepository.save(comunicado);
    }

    public Comunicado endCommunication(long id) {
        Comunicado comunicadoToEnd = communicadoRepository.findById(id).orElse(null);
        if (comunicadoToEnd == null) {
            throw new ResourceNotFoundException("Comunicado de id " + id + " não encontrado.");
        }
        LocalDate nowDate = LocalDate.now();
        comunicadoToEnd.setEndDate(nowDate);

        return communicadoRepository.save(comunicadoToEnd);
    }

    public Comunicado deleteCommunication(long id) {
        Comunicado comunicadoToDelete = communicadoRepository.findById(id).orElse(null);
        if (comunicadoToDelete == null) {
            throw new ResourceNotFoundException("Comunicado de id " + id + " não encontrado.");
        }
        comunicadoToDelete.setDeleted(true);
        return communicadoRepository.save(comunicadoToDelete);
    }
}
