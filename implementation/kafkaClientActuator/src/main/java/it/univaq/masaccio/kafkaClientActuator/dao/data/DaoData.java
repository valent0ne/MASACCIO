package it.univaq.masaccio.kafkaClientActuator.dao.data;


import it.univaq.masaccio.kafkaClientActuator.dao.exception.DaoException;

public interface DaoData extends AutoCloseable{


    void init() throws DaoException;


    void destroy() throws DaoException;

}