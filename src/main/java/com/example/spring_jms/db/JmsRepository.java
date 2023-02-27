package com.example.spring_jms.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@Slf4j
public class JmsRepository {

    // бин с jdbc
    private final JdbcTemplate jdbcTemplate;

    public JmsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // сохранение сообщений в бд
    public void writeMessageToDb(TextMessage textMessage) {
        KeyHolder holder = new GeneratedKeyHolder(); // нужен для того чтобы вытащить id добавленной записи

        // этап добавления сообщения в таблицу msg
        jdbcTemplate.update(
            connection -> {PreparedStatement ps = connection.prepareStatement(
                    "INSERT into msg (message) values (?)", // insert into msg (message) values (Message #1)
                    Statement.RETURN_GENERATED_KEYS);
            try {
                ps.setString(1, textMessage.getText());
            } catch (Exception e) {
                log.error("Error (insert message body) in JmsRepository class!", e);
            }
            return ps;
        }, holder);

        // сохраняем заголовок в отдельную таблицу
        try {
            insertHeader(holder.getKey().intValue(), textMessage.getJMSType());
        } catch (JMSException e) {
            log.error("Error insertHeader() in JmsRepository class!", e);
        }
    }

    private void insertHeader(int recordId, String value) {
        jdbcTemplate.update("INSERT into headers (headers_id, head) values (?,?)", recordId, value);
    }

    public void showDataInDb(){
        log.error("\n\n\n\nFrom database results:");
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT msg.message, headers.head " +
                " FROM msg" +
                " JOIN headers ON msg.msg_id = headers.headers_id;");
        // пробегаемся по всем возвращенным результатам
        while(rowSet.next()) {
            log.info(rowSet.getString(1) + "  " + rowSet.getString(2));
        }
    }
}
