package it.univaq.masaccio.ActuationManager.dao.interfaces;

import it.univaq.masaccio.ActuationManager.dao.data.DaoData;
import it.univaq.masaccio.ActuationManager.dao.exception.DaoException;
import it.univaq.masaccio.ActuationManager.model.Area;

import java.util.List;

public interface MasaccioDaoMySQL extends DaoData {

    public List<Area> getAreas() throws DaoException;

}

