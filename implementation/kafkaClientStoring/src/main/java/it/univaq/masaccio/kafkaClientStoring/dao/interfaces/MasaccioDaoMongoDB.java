package it.univaq.masaccio.kafkaClientStoring.dao.interfaces;

import it.univaq.masaccio.kafkaClientStoring.dao.data.DaoData;

public interface MasaccioDaoMongoDB extends DaoData {

    public void insert(String value, String collection);
}

