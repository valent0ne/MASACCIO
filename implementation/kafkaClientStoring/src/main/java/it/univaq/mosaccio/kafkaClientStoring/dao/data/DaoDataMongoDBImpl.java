package it.univaq.mosaccio.kafkaClientStoring.dao.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import static it.univaq.mosaccio.kafkaClientStoring.Main.*;

import com.mongodb.client.MongoDatabase;
import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;

public class DaoDataMongoDBImpl implements DaoData{

    private MongoClient mongoClient = null;
    private MongoDatabase dataBase = null;

    public DaoDataMongoDBImpl(){
        super();
    }


    @Override
    public void init() throws DaoException {

        try {
            mongoClient = new MongoClient(MONGO_ADDR.split(":")[0], Integer.parseInt(MONGO_ADDR.split(":")[1]));
            dataBase = mongoClient.getDatabase(MONGO_DB);

        } catch (Exception e) {
            throw new DaoException("Error: mongodb connection failed", e);
        }

    }


    @Override
    public void destroy() throws DaoException {
        try {
            mongoClient.close();
        } catch (Exception e) {
            throw new DaoException("Error: mongodb shutdown failed connection", e);
        }
    }


    @Override
    public void close() throws Exception {
        destroy();
    }


}