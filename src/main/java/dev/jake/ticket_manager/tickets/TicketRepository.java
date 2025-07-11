package dev.jake.ticket_manager.tickets;

import dev.jake.ticket_manager.device.Device;
import dev.jake.ticket_manager.tickets.DTO.*;
import dev.jake.ticket_manager.users.DTO.TicketUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepository {
    private static final Logger log = LoggerFactory.getLogger(TicketRepository.class);
    private JdbcClient jdbcClient;

    public TicketRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }
    public List<StaffListTicket> getAllTickets(){
        String sql = """
                    SELECT
                        ticket.id AS ticket_id,
                        ticket.issue AS ticket_issue,
                        ticket.created_time AS ticket_created_time,
                        ticket.state AS ticket_state,
                        user_accounts.name AS user_accounts_name,
                        user_accounts.phone_number AS user_account_phone_number
                    FROM ticket
                    JOIN user_accounts ON ticket.issuer_id = user_accounts.id
                    ORDER BY ticket_created_time ASC;
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    TicketUserInfo info = new TicketUserInfo(
                            rs.getString("user_accounts_name"),
                            rs.getString("user_account_phone_number")
                    );
                    return new StaffListTicket(
                            rs.getInt("ticket_id"),
                            info,
                            rs.getString("ticket_issue"),
                            rs.getObject("ticket_created_time", LocalDateTime.class),
                            rs.getString("ticket_state")
                    );
                })
                .list();
    }
    public Optional<StaffViewTicket> getStaffViewTicket(int ticket_id){
        String sql = """
                    SELECT
                        ticket.id AS ticket_id,
                        ticket.issue AS ticket_issue,
                        ticket.created_time AS ticket_created_time,
                        ticket.state AS ticket_state,
                        user_accounts.name AS user_accounts_name,
                        user_accounts.phone_number AS user_account_phone_number,
                        device.*
                    FROM ticket
                    JOIN user_accounts ON ticket.issuer_id = user_accounts.id
                    JOIN device ON ticket.device_id = device.id
                    WHERE ticket.id = :ticket_id
                    ORDER BY ticket_created_time ASC;
                """;

        return jdbcClient.sql(sql)
                .param("ticket_id", ticket_id)
                .query((rs, rowNum) -> {
                    TicketUserInfo info = new TicketUserInfo(
                            rs.getString("user_accounts_name"),
                            rs.getString("user_account_phone_number")
                    );
                    Device device = new Device(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getString("CPU"),
                            rs.getString("mother_board"),
                            rs.getString("drive"),
                            rs.getString("additional_info")
                    );
                    return new StaffViewTicket(
                            rs.getInt("ticket_id"),
                            info,
                            rs.getString("ticket_issue"),
                            rs.getObject("ticket_created_time", LocalDateTime.class),
                            device,
                            rs.getString("ticket_state")
                    );
                })
                .optional();
    }

    public boolean doesTicketExist(int ticket_ID){
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM ticket WHERE id = :ticket_ID)")
                .param("ticket_ID", ticket_ID)
                .query(Boolean.class)
                .single();
    }

    public List<StaffViewTicketInfo> getTicketsInfoOfTickets(int ticket_id){
        String sql = """
                    SELECT
                    
                        ticket_detail.id AS id,
                        user_accounts.name AS staff_name,
                        ticket_detail.process_detail AS process_detail,
                        ticket_detail.created_timed AS created_timed
                                         
                    FROM ticket_detail
                    JOIN user_accounts ON ticket_detail.staff_id = user_accounts.id
                    WHERE ticket_id= :ticket_id
                    ORDER BY created_timed ASC;
                    """;
        return jdbcClient.sql(sql)
                .param("ticket_id", ticket_id)
                .query((rs, rowNum) -> {
                    return new StaffViewTicketInfo(
                            rs.getInt("id"),
                            rs.getString("staff_name"),
                            rs.getString("process_detail"),
                            rs.getObject("created_timed", LocalDateTime.class)
                    );
                })
                .list();
    }
    public Optional<String> getTicketStatus(int ticket_id){
        return jdbcClient.sql("SELECT state FROM ticket WHERE id= :id;")
                .param("id",ticket_id)
                .query(String.class)
                .optional();
    }
    public Optional<Ticket> getTicketByID(int ticket_id){
        return jdbcClient.sql("SELECT * FROM ticket WHERE id= :id;")
                .param("id",ticket_id)
                .query(Ticket.class)
                .optional();
    }
    public List<Ticket> getTicketByIssuerID(int issuer_ID){
        return jdbcClient.sql("SELECT * FROM ticket WHERE issuer_id= :id AND NOT state='CLOSED';")
                .param("id",issuer_ID)
                .query(Ticket.class)
                .list();
    }

    public List<Ticket> getTicketByStaffID(int staff_ID){
        return jdbcClient.sql("SELECT * FROM ticket WHERE id IN (SELECT ticket_id FROM ticket_detail WHERE staff_id=:staff_id);")
                .param("staff_id",staff_ID)
                .query(Ticket.class)
                .list();
    }
    public Optional<TicketInfo> getTicketInfoByID(int ticket_id){
        return jdbcClient.sql("SELECT * FROM ticket_detail WHERE id= :id;")
                .param("id",ticket_id)
                .query(TicketInfo.class)
                .optional();
    }
    public void createTicketInfo(TicketInfo ticketInfo){
        var updated = jdbcClient.sql("INSERT INTO ticket_detail (ticket_id, staff_id, process_detail, created_timed) values (:ticket_id,:staff_id,:process_detail,:created_timed);")
                        .param("ticket_id",ticketInfo.getTicket_id())
                        .param("staff_id",ticketInfo.getStaff_id())
                        .param("process_detail",ticketInfo.getProcess_detail())
                        .param("created_timed",ticketInfo.getCreated_time())
                        .update();

        Assert.state(updated == 1, "Failed to create TicketInfo for" + ticketInfo.getTicket_id() + "at" + ticketInfo.getCreated_time());
    }
    public void createTicket(Ticket ticket){
        var updated = jdbcClient.sql("INSERT INTO ticket (issuer_id, issue, device_id, created_time, state) values (:issuerId,:issue,:device_id,:created_time,:state);")
                .param("issuerId",ticket.getIssuerId())
                .param("issue",ticket.getIssue())
                .param("device_id",ticket.getDevice_id())
                .param("created_time",ticket.getCreated_time())
                .param("state","PENDING")
                .update();

        Assert.state(updated == 1, "Failed to create TicketInfo for" + ticket.getId() + "at" + ticket.getCreated_time());
    }

    public void updateTicket(Ticket ticket) {
        String sql = """
        UPDATE ticket SET 
            issuer_id = :issuerId,
            issue = :issue,
            created_time = :createdTime,
            device_id = :deviceId,
            state = :state
        WHERE id = :id
        """;

        jdbcClient.sql(sql)
                .param("id", ticket.getId())
                .param("issuerId", ticket.getIssuerId())
                .param("issue", ticket.getIssue())
                .param("createdTime", ticket.getCreated_time())
                .param("deviceId", ticket.getDevice_id())
                .param("state", ticket.getState())
                .update();


        log.info("Updated ticket with ID: {}", ticket.getId());
    }
    public void updateTicketInfo(TicketInfo ticketInfo) {
        String sql = """
        UPDATE ticket SET 
            ticket_id = :ticket_id,
            staff_id = :staff_id,
            created_time = :createdTime,
            device_id = :deviceId,
            state = :state
        WHERE id = :id
        """;

        jdbcClient.sql(sql)
                .param("id", ticketInfo.getId())
                .param("ticket_id", ticketInfo.getTicket_id())
                .param("staff_id", ticketInfo.getStaff_id())
                .param("process_detail", ticketInfo.getProcess_detail())
                .param("createdTime", ticketInfo.getCreated_time())
                .update();

    }

    public void deleteTicket(int ticket_id) {
        String ticketInfo_delete_query = "DELETE FROM ticket_detail WHERE ticket_id = :id";

        String ticket_delete_query = "DELETE FROM ticket WHERE id = :id";

        jdbcClient.sql(ticketInfo_delete_query)
                .param("id", ticket_id)
                .update();

        jdbcClient.sql(ticket_delete_query)
                .param("id", ticket_id)
                .update();


    }


}
