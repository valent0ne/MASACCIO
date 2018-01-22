package it.univaq.masaccio.rest.business.repository;

import it.univaq.masaccio.rest.business.domain.Area;
import it.univaq.masaccio.rest.business.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SensorRepository extends JpaRepository<Sensor, Long>{

    List<Sensor> findAll();

    Sensor findOne(Long Id);

    List<Sensor> findAllByArea(Long id);
}
