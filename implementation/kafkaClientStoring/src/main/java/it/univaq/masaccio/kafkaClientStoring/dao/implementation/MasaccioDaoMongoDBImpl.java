package it.univaq.masaccio.kafkaClientStoring.dao.implementation;


import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import it.univaq.masaccio.kafkaClientStoring.dao.interfaces.MasaccioDaoMongoDB;
import it.univaq.masaccio.kafkaClientStoring.dao.data.DaoDataMongoDBImpl;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * class responsible to the querying of the MySQL db
 */
public class MasaccioDaoMongoDBImpl extends DaoDataMongoDBImpl implements MasaccioDaoMongoDB {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MasaccioDaoMongoDBImpl.class);

    /**
     * constructor
     */
    public MasaccioDaoMongoDBImpl(){
        super();
    }



    public void insert(String value, String collection){
        try{
            LOGGER.info("inserting value to collection {}", collection);
            MongoCollection<Document> c = this.dataBase.getCollection(collection);
            Document doc = Document.parse(value);
            // async insertion
            c.insertOne(doc, new SingleResultCallback<Void>() {
                @Override
                public void onResult(final Void result, final Throwable t) {
                    LOGGER.info("value inserted");
                }
            });
        }catch (Exception e){
            LOGGER.error("cannot insert to mongo - {}", e.getMessage());
            if(LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }

    }


}
