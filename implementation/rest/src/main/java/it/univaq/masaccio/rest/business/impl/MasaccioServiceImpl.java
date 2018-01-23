package it.univaq.masaccio.rest.business.impl;

import it.univaq.masaccio.rest.business.domain.Area;
import it.univaq.masaccio.rest.business.domain.Sensor;
import it.univaq.masaccio.rest.business.repository.AreaRepository;
import it.univaq.masaccio.rest.business.repository.MasaccioService;
import it.univaq.masaccio.rest.business.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

// we expose this implementation of the interface,to the world.
@Service
@Transactional
public class MasaccioServiceImpl implements MasaccioService{

    // import of the already implemented interfaces from the framework and we use them.
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SensorRepository sensorRepository;


    // Sensor services
    public List<Sensor> findAllSensorsByArea(Long id){
        Area area = new Area();
        area.setId(id);
        return sensorRepository.findAllByArea(area);
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
