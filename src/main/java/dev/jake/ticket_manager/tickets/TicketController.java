package dev.jake.ticket_manager.tickets;



import dev.jake.ticket_manager.tickets.DTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    public TicketController(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/ticket")
    void createTicket(@RequestBody Ticket ticket){
        ticketRepository.createTicket(ticket);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/staff/TicketInfo")
    void createTicketInfo(@RequestBody TicketInfo ticketInfo){
        ticketRepository.createTicketInfo(ticketInfo);
    }

    @PreAuthorize("#user_ID == authentication.principal.getID()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{user_ID}")
    List<Ticket> getTicketsByIssuerID(@PathVariable Integer user_ID){
        return ticketRepository.getTicketByIssuerID(user_ID);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/staff/TicketList")
    List<StaffListTicket> getActiveTickets(){
        return ticketRepository.getAllTickets();
    }

    @GetMapping("/staff/{ticketID}")
    ResponseEntity<StaffViewTicket> getStaffViewTicket(@PathVariable int ticketID){
        Optional<StaffViewTicket> ticket_query = ticketRepository.getStaffViewTicket(ticketID);
        if (ticket_query.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket_query.get());
    }

    @GetMapping("/staff/{ticketID}/raw")
    ResponseEntity<Ticket> getTicket(@PathVariable int ticketID){
        Optional<Ticket> ticket_query = ticketRepository.getTicketByID(ticketID);
        if (ticket_query.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket_query.get());
    }


    @GetMapping("/staff/TicketInfo/{ticketID}")
    ResponseEntity<List<StaffViewTicketInfo>> getTicketInfo(@PathVariable Integer ticketID){
        if (!ticketRepository.doesTicketExist(ticketID)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketRepository.getTicketsInfoOfTickets(ticketID));
    }

    @GetMapping("/TicketProgress/{ticketID}")
    ResponseEntity<String> getTicketProgress(@PathVariable int ticketID){
        Optional<String> ticket_progress = ticketRepository.getTicketStatus(ticketID);
        if (ticket_progress.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket_progress.get());
    }

    @PatchMapping("/staff/TicketInfo/{ticketInfoID}")
    ResponseEntity<Void> modifyTicketInfo(@PathVariable int ticketInfoID, @RequestBody TicketInfo update){
        Optional<TicketInfo> ticket_info_query = ticketRepository.getTicketInfoByID(ticketInfoID);
        if (ticket_info_query.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TicketInfo target = ticket_info_query.get();

        update.setId(target.getId());
        ticketRepository.updateTicketInfo(update);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/staff/Ticket/{ticket_ID}")
    ResponseEntity<Void> modifyTicket(@PathVariable int ticket_ID, @RequestBody Ticket update){
        if (!ticketRepository.doesTicketExist(ticket_ID)){
            return ResponseEntity.notFound().build();
        }

        ticketRepository.updateTicket(update);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/Ticket/{ticket_ID}")
    ResponseEntity<Void> deleteTicket(@PathVariable int ticket_ID){
        if (!ticketRepository.doesTicketExist(ticket_ID)){
            return ResponseEntity.notFound().build();
        }

        ticketRepository.deleteTicket(ticket_ID);
        return ResponseEntity.noContent().build();
    }

}
