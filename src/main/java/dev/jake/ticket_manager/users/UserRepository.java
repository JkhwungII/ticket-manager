package dev.jake.ticket_manager.users;

import dev.jake.ticket_manager.users.DTO.TicketUser;
import dev.jake.ticket_manager.users.DTO.TicketUserInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public class UserRepository {
    private JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public void createUser(@RequestBody TicketUser user){
        jdbcClient.sql("INSERT INTO user_accounts (name, role, password) values (:name, :role, :password);")
                .param("name",user.getName())
                .param("role",user.getRole())
                .param("password",user.getPassword())
                .update();
    }

    public Optional<TicketUser> getUserByUsername(String username){
        return jdbcClient.sql("SELECT * FROM user_accounts WHERE name= :name;")
                .param("name",username)
                .query(TicketUser.class)
                .optional();
    }

    public Optional<TicketUserInfo> getUserInfoByID(int id){
        return jdbcClient.sql("SELECT name,phone_number FROM user_accounts WHERE id= :id;")
                .param("id",id)
                .query(TicketUserInfo.class)
                .optional();
    }
}
