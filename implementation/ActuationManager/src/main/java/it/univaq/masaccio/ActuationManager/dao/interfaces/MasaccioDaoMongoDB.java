package it.univaq.masaccio.ActuationManager.dao.interfaces;

import it.univaq.masaccio.ActuationManager.dao.data.DaoData;

public interface MasaccioDaoMongoDB extends DaoData {

    public void insert(String value, String collection);
}

