package it.univaq.mosaccio.kafkaClientStoring.dao.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import static it.univaq.mosaccio.kafkaClientStoring.Main.*;

import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;

public class DaoDataMySQLImpl implements DaoData{

    private DataSource datasource;
    protected Connection connection;

    public DaoDataMySQLImpl(){
        super();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(MYSQL_USER);
        ds.setPassword(MYSQL_PSW);
        ds.setUrl("jdbc:mysql://"+MYSQL_ADDR+"/"+MYSQL_DB);
        ds.setValidationQuery("SELECT 1");
        this.datasource = ds;
        this.connection = null;
    }


    @Override
    public void init() throws DaoException {

        try {
            // InitialContext ctx = new InitialContext();
            // DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/gamingplatform");

            connection = datasource.getConnection();

        } catch (SQLException e) {
            throw new DaoException("Error: mysql db connection failed", e);
        }

    }


    @Override
    public void destroy() throws DaoException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error: shutdown failed connection", e);
        }
    }


    @Override
    public void close() throws Exception {
        destroy();
    }


}