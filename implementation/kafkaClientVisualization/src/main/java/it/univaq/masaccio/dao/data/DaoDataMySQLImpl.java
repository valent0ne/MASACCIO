package it.univaq.masaccio.dao.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import it.univaq.masaccio.Main;
import org.apache.commons.dbcp2.BasicDataSource;

import it.univaq.masaccio.dao.exception.DaoException;

public class DaoDataMySQLImpl implements DaoData{

    private DataSource datasource;
    protected Connection connection;

    public DaoDataMySQLImpl(){
        super();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(Main.properties.getProperty("mysql_user"));
        ds.setPassword(Main.properties.getProperty("mysql_password"));
        ds.setUrl("jdbc:mysql://"+Main.properties.getProperty("mysql_address")+"/"+Main.properties.getProperty("mysql_db_name"));
        ds.setValidationQuery("SELECT 1");
        this.datasource = ds;
        this.connection = null;
    }


    @Override
    public void init() throws DaoException {

        try {
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