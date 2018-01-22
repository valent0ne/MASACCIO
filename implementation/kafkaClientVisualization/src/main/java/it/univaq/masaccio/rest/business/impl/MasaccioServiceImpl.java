package it.univaq.masaccio.rest.business.impl;

import it.univaq.masaccio.rest.business.domain.Area;
import it.univaq.masaccio.rest.business.domain.Sensor;
import it.univaq.masaccio.rest.business.repository.AreaRepository;
import it.univaq.masaccio.rest.business.repository.MasaccioService;
import it.univaq.masaccio.rest.business.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MasaccioServiceImpl implements MasaccioService{


    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SensorRepository sensorRepository;


    // Sensor services
    public List<Sensor> findAllSensorsByArea(Long id){
        return sensorRepository.findAllByArea(id);
    }

    public Sensor findSensorById(Long id){
        return sensorRepository.findOne(id);
    }

    public List<Sensor> findAllSensors(){
        return sensorRepository.findAll();
    }
    // end Sensor services


    // Area services
    public Area findAreaById(Long id){
        return areaRepository.findOne(id);
    }

    public List<Area> findAllAreas(){
        return areaRepository.findAll();
    }
    // end Area services
}
