package it.univaq.mosaccio.kafkaClientStoring.dao.implementation;

import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoDataMySQLImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;
import it.univaq.mosaccio.kafkaClientStoring.dao.interfaces.MosaccioDaoMySQL;
import it.univaq.mosaccio.kafkaClientStoring.model.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * class responsible to the querying of the MySQL db
 */
public class MosaccioDaoMySQLImpl extends DaoDataMySQLImpl implements MosaccioDaoMySQL {
    private PreparedStatement getAreas; // list of all the areas

    /**
     * constructor
     */
    public MosaccioDaoMySQLImpl(){
        super();
    }

    /**
     * initialize the connection to the db and declares the prepared statements
     * @throws DaoException in case of errors
     */
    @Override
    public void init() throws DaoException {
        try{
            super.init();
            this.getAreas = connection.prepareStatement("SELECT * FROM areas");
        }catch (Exception e){
            throw new DaoException("Cannot initialize MosaccioDaoMySQL", e);
        }
    }

    /**
     * retrieves the list of all the areas from the MySQL db
     * @return the list of all the areas
     * @throws DaoException in case of errors
     */
    public List<Area> getAreas() throws DaoException{
        List<Area> out = new ArrayList<>();

        try{
            ResultSet rs = this.getAreas.executeQuery();
            while(rs.next()){
                Area a = new Area(this);
                a.setId(rs.getInt("id"));
                a.setDescription(rs.getString("description"));
                a.setLatitude(rs.getString("latitude"));
                a.setLongitude(rs.getString("longitude"));
                out.add(a);
            }

        }catch (Exception e){
            throw new DaoException("Error MySQL query getAreas()", e);
        }

        return out;
    }
}
