package it.univaq.mosaccio.kafkaClientStoring.dao.interfaces;

import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoData;
import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;
import it.univaq.mosaccio.kafkaClientStoring.model.Area;

import java.util.List;

public interface MosaccioDaoMySQL extends DaoData {

    public List<Area> getAreas() throws DaoException;

}

