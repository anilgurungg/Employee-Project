package com.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.EmployeeRepository;
import com.dao.ProjectRepository;
import com.dao.TicketRepository;
import com.dto.ApiResponse;
import com.dto.ProjectDTO;
import com.dto.TicketDTO;
import com.entity.EmployeeEntity;
import com.entity.ProjectEntity;
import com.entity.TicketEntity;
import com.exception.ResourceNotFoundException;
import com.exception.UnAuthorizedException;
import com.security.UserPrincipal;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ProjectEntity convertProjectToEntity( ProjectDTO projectDTO ) {
		return modelMapper.map(projectDTO, ProjectEntity.class);
	}
	
	public ProjectDTO converProjectToDTO( ProjectEntity projectEntity ) {
		return modelMapper.map(projectEntity, ProjectDTO.class);
	}
	
	public TicketEntity convertTicketToEntity( TicketDTO ticketDTO ) {
		return modelMapper.map(ticketDTO, TicketEntity.class);
	}
	
	public TicketDTO convertTicketToDTO( TicketEntity ticketEntity ) {
		return modelMapper.map(ticketEntity, TicketDTO.class);
	}
	
	private void checkTicketBelongsToProject(TicketEntity ticketEntity, ProjectEntity projectEntity) {
        if (ticketEntity.getProject() == null || ticketEntity.getProject().getProjectId() != projectEntity.getProjectId()) {
            throw new UnAuthorizedException(new ApiResponse(Boolean.FALSE, "Ticket does not belong to this project"));
        }
    }
	

	@Override
	public void addTicket(int projectId, TicketDTO ticketDTO, UserPrincipal curentUser) {

		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		EmployeeEntity employeeEntity = employeeRepository.findById(curentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE", "ID", curentUser.getEmployeeId()));
		
		TicketEntity ticketEntity = convertTicketToEntity(ticketDTO);
		ticketEntity.setProject(projectEntity);

		Set<EmployeeEntity> assignedEmployees = new HashSet<EmployeeEntity>();
		assignedEmployees.add(employeeEntity);
		ticketEntity.setAssignedEmployees(assignedEmployees);
		
		employeeEntity.getTickets().add(ticketEntity);
		
		employeeRepository.save(employeeEntity);
		ticketRepository.save(ticketEntity);

	}

	@Override
	public List<TicketDTO> getAllTickets(int projectId) {
		if (!projectRepository.existsById(projectId)) {
	        throw new ResourceNotFoundException("Project", "ID", projectId);
	    }
		
		List<TicketEntity> ticketEntities = ticketRepository.findByProjectProjectId(projectId);
		
		return ticketEntities.stream().map(ticketEntity -> {
				TicketDTO ticketDTO = convertTicketToDTO(ticketEntity);
				ticketDTO.setProjectId(ticketEntity.getProject().getProjectId());
				return ticketDTO;
		}).collect(Collectors.toList());
		
	}
	

	@Override
	public List<TicketDTO> getMyTickets(int projectId, UserPrincipal currentUser) {
		if (!projectRepository.existsById(projectId)) {
	        throw new ResourceNotFoundException("Project", "ID", projectId);
	    }
		
		List<TicketEntity> ticketEntities = ticketRepository.findAllTicketsByEmployeeId(currentUser.getEmployeeId());
		
		return ticketEntities.stream().map(ticketEntity -> {
				TicketDTO ticketDTO = convertTicketToDTO(ticketEntity);
				ticketDTO.setProjectId(ticketEntity.getProject().getProjectId());
				return ticketDTO;
		}).collect(Collectors.toList());
		
	}


	@Override
	public TicketDTO getTicketById(int projectId, int ticketId, UserPrincipal currentUser) {
		if (!projectRepository.existsById(projectId)) {
	        throw new ResourceNotFoundException("Project", "ID", projectId);
	    }
		TicketEntity ticketEntity = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("TICKET", "ID", ticketId));
		if (!ticketEntity.getAssignedEmployeeIds().contains(currentUser.getEmployeeId()) ) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to view this ticket");
			throw new UnAuthorizedException(apiResponse);
		}
		TicketDTO ticketDTO = convertTicketToDTO(ticketEntity);
		ticketDTO.setProjectId(ticketEntity.getProject().getProjectId());	
		return ticketDTO;
	}

	@Override
	public ApiResponse deleteTicketById(int projectId, int ticketId, UserPrincipal currentUser) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));

		TicketEntity ticketEntity = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("TICKET", "ID", ticketId));

	    checkTicketBelongsToProject(ticketEntity, projectEntity);

		if (ticketEntity.getAssignedEmployeeIds().contains(currentUser.getEmployeeId()) ) {
			ticketRepository.deleteById(ticketId);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted ticket");
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to delete this ticket");
		throw new UnAuthorizedException(apiResponse);
	}

	@Override
	public ApiResponse updateTicketById(int projectId, int ticketId, TicketDTO ticketDTO, UserPrincipal currentUser) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));

		TicketEntity ticketEntity = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("TICKET", "ID", ticketId));

		
	    checkTicketBelongsToProject(ticketEntity, projectEntity);

		if (ticketEntity.getAssignedEmployeeIds().contains(currentUser.getEmployeeId()) ) {
			ticketEntity.setSubject(ticketDTO.getSubject());
			ticketEntity.setDescription(ticketDTO.getDescription());
			ticketRepository.save(ticketEntity);
			return new ApiResponse(Boolean.TRUE, "You successfully updated ticket");
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to update this ticket");
		throw new UnAuthorizedException(apiResponse);
	}
	


}