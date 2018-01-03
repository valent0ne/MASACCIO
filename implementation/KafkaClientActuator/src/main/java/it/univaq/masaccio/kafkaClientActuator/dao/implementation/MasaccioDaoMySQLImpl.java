package it.univaq.masaccio.kafkaClientActuator.dao.implementation;

import it.univaq.masaccio.kafkaClientActuator.dao.data.DaoDataMySQLImpl;
import it.univaq.masaccio.kafkaClientActuator.dao.exception.DaoException;
import it.univaq.masaccio.kafkaClientActuator.dao.interfaces.MasaccioDaoMySQL;
import it.univaq.masaccio.kafkaClientActuator.model.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class responsible to the querying of the MySQL db
 */
public class MasaccioDaoMySQLImpl extends DaoDataMySQLImpl implements MasaccioDaoMySQL {
    private PreparedStatement getAreas; // list of all the areas
    private PreparedStatement getSensors; // list of all the sensors

    /**
     * constructor
     */
    public MasaccioDaoMySQLImpl(){
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
            this.getAreas = connection.prepareStatement("SELECT areas.name AS name FROM areas LEFT JOIN actuators ON areas.id = actuators.area");
            this.getSensors = connection.prepareStatement("SELECT * FROM sensors_actuators");
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
                a.setName(rs.getString("name"));
                out.add(a);
            }

        }catch (Exception e){
            throw new DaoException("Error MySQL query getAreas()", e);
        }

        return out;
    }

    public Map<Integer, Integer> getSensors() throws DaoException{
        Map<Integer, Integer> out = new HashMap<>();

        try{
            ResultSet rs = this.getSensors.executeQuery();
            while(rs.next()){
                out.put(rs.getInt("id_sensor"), rs.getInt("id_actuator"));
            }

        }catch (Exception e){
            throw new DaoException("Error MySQL query getSensors()", e);
        }

        return out;
    }
}
