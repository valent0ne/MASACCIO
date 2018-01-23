package it.univaq.masaccio.dao.data;


import it.univaq.masaccio.dao.exception.DaoException;

public interface DaoData extends AutoCloseable{


    void init() throws DaoException;


    void destroy() throws DaoException;

}