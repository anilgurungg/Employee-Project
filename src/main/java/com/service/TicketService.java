package com.service;

import java.util.List;

import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.ResponseDTO;
import com.dto.TicketDTO;
import com.security.UserPrincipal;

public interface TicketService {

//	void addTicket(TicketDTO ticketDTO,UserPrincipal currentUser );

	List<TicketDTO> getAllTickets(int projectId);

	TicketDTO getTicketById(int projectId,int ticketId, UserPrincipal currentUser);

	ApiResponse deleteTicketById(int projectId, int ticketId, UserPrincipal currentUser);

	ApiResponse updateTicketById(int projectId,int ticketId, TicketDTO ticketDTO, UserPrincipal currentUser);

	void addTicket(int projectId, TicketDTO ticketDTO, UserPrincipal currentUser);

	List<TicketDTO> getMyTickets(int projectId, UserPrincipal currentUser);

	

}
