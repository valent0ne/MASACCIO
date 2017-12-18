package it.univaq.mosaccio.kafkaClientStoring.dao.data;


import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;

public interface DaoData extends AutoCloseable{


    void init() throws DaoException;


    void destroy() throws DaoException;

}