package it.univaq.masaccio.kafkaClientActuator.dao.interfaces;

import it.univaq.masaccio.kafkaClientActuator.dao.data.DaoData;
import it.univaq.masaccio.kafkaClientActuator.dao.exception.DaoException;
import it.univaq.masaccio.kafkaClientActuator.model.Area;

import java.util.List;
import java.util.Map;

public interface MasaccioDaoMySQL extends DaoData {

    public List<Area> getAreas() throws DaoException;

    public Map<Integer, Integer> getSensors() throws DaoException;


}

