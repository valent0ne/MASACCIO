package it.univaq.masaccio.dao.interfaces;


import it.univaq.masaccio.dao.data.DaoData;
import it.univaq.masaccio.dao.exception.DaoException;
import it.univaq.masaccio.model.Area;

import java.util.List;
import java.util.Map;

public interface MasaccioDaoMySQL extends DaoData {

    public List<Area> getAreas() throws DaoException;

}

