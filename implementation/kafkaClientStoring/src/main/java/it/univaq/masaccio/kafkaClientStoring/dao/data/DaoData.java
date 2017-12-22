package it.univaq.masaccio.kafkaClientStoring.dao.data;


import it.univaq.masaccio.kafkaClientStoring.dao.exception.DaoException;

public interface DaoData extends AutoCloseable{


    void init() throws DaoException;


    void destroy() throws DaoException;

}