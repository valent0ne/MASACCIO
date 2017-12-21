package it.univaq.mosaccio.kafkaClientStoring.dao.interfaces;

import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoData;

public interface MosaccioDaoMongoDB extends DaoData {

    public void insert(String value, String collection);
}

