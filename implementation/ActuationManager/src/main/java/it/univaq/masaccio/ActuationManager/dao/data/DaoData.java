package it.univaq.masaccio.ActuationManager.dao.data;


import it.univaq.masaccio.ActuationManager.dao.exception.DaoException;

public interface DaoData extends AutoCloseable{


    void init() throws DaoException;


    void destroy() throws DaoException;

}