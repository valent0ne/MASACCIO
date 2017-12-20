package it.univaq.mosaccio.kafkaClientStoring.dao.implementation;

import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoDataMongoDBImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;
import it.univaq.mosaccio.kafkaClientStoring.dao.interfaces.MosaccioDaoMongoDB;
import it.univaq.mosaccio.kafkaClientStoring.model.Area;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * class responsible to the querying of the MySQL db
 */
public class MosaccioDaoMongoDBImpl extends DaoDataMongoDBImpl implements MosaccioDaoMongoDB {

    /**
     * constructor
     */
    public MosaccioDaoMongoDBImpl(){
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
        }catch (Exception e){
            throw new DaoException("Cannot initialize MosaccioDaoMySQL", e);
        }
    }

    public void insert(JSONObject j){

        //TODO finisci metodo
    }


}
