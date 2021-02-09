package com.walter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTests {
    @Autowired
    private DataSource dataSource;

    @Test
    public void select() throws SQLException {
        final String SQL = "SELECT o.*, i.order_item_id FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setLong(1, 9785L);
            preparedStatement.setLong(2, 4740584156199727105L);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while(rs.next()) {
                    log.info("result: orderId={}, userId={}, status={}, orderItemId={}", rs.getLong("order_id"), rs.getLong("user_id"),
                            rs.getString("status"), rs.getLong("order_item_id"));
                }
            }
        }
    }

    @Test
    public void selectWithOrderBy() throws SQLException {
        final String SQL = "SELECT o.*, i.order_item_id FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? ORDER BY o.order_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setLong(1, 12558L);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while(rs.next()) {
                    log.info("result: orderId={}, userId={}, status={}, orderItemId={}", rs.getLong("order_id"), rs.getLong("user_id"),
                            rs.getString("status"), rs.getLong("order_item_id"));
                }
            }
        }
    }
}
