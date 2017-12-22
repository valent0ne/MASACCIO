package it.univaq.masaccio.kafkaClientStoring.dao.data;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import it.univaq.masaccio.kafkaClientStoring.Main;
import it.univaq.masaccio.kafkaClientStoring.dao.exception.DaoException;

public class DaoDataMongoDBImpl implements DaoData{

    private MongoClient mongoClient = null;
    protected MongoDatabase dataBase = null;

    public DaoDataMongoDBImpl(){
        super();
    }


    @Override
    public void init() throws DaoException {

        try {
            mongoClient = new MongoClient(Main.properties.getProperty("mongo_address").split(":")[0], Integer.parseInt(Main.properties.getProperty("mongo_address").split(":")[1]));
            dataBase = mongoClient.getDatabase(Main.properties.getProperty("mongo_db_name"));

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