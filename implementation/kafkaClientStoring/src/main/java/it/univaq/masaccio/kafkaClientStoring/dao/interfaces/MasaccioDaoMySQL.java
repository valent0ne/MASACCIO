package it.univaq.masaccio.kafkaClientStoring.dao.interfaces;

import it.univaq.masaccio.kafkaClientStoring.dao.data.DaoData;
import it.univaq.masaccio.kafkaClientStoring.dao.exception.DaoException;
import it.univaq.masaccio.kafkaClientStoring.model.Area;

import java.util.List;

public interface MasaccioDaoMySQL extends DaoData {

    public List<Area> getAreas() throws DaoException;

}

