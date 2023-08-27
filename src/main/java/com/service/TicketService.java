package com.service;

import com.dto.ApiResponse;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.dto.TicketDTO;
import com.security.UserPrincipal;

public interface TicketService {

//	void addTicket(TicketDTO ticketDTO,UserPrincipal currentUser );

	PagedResponseDTO<TicketDTO> getAllTickets( Integer page, Integer size, int projectId);

	TicketDTO getTicketById(int projectId,int ticketId, UserPrincipal currentUser);

	ApiResponse deleteTicketById(int projectId, int ticketId, UserPrincipal currentUser);

	ApiResponse updateTicketById(int projectId,int ticketId, TicketDTO ticketDTO, UserPrincipal currentUser);

	void addTicket(int projectId, TicketDTO ticketDTO, UserPrincipal currentUser);

	PagedResponseDTO<TicketDTO> getMyTickets(Integer page, Integer size,int projectId, UserPrincipal currentUser);

	TicketDTO addAsigneeToTicket(int projectId, int employeeId,  int ticketId);

	TicketDTO removeAsigneeFromTicket(int projectId, int employeeId, int ticketId);

	

}
