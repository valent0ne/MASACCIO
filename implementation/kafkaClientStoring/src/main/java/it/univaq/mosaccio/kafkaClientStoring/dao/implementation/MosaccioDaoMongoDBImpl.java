package it.univaq.mosaccio.kafkaClientStoring.dao.implementation;


import com.mongodb.client.MongoCollection;
import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoDataMongoDBImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.interfaces.MosaccioDaoMongoDB;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * class responsible to the querying of the MySQL db
 */
public class MosaccioDaoMongoDBImpl extends DaoDataMongoDBImpl implements MosaccioDaoMongoDB {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MosaccioDaoMongoDBImpl.class);

    /**
     * constructor
     */
    public MosaccioDaoMongoDBImpl(){
        super();
    }



    public void insert(String value, String collection){
        try{
            LOGGER.info("inserting value to collection {}", collection);
            MongoCollection<Document> c = this.dataBase.getCollection(collection);
            Document doc = Document.parse(value);
            c.insertOne(doc);
            LOGGER.info("value inserted");
        }catch (Exception e){
            LOGGER.error("cannot insert to mongo - {}", e.getMessage());
            if(LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }

    }


}
