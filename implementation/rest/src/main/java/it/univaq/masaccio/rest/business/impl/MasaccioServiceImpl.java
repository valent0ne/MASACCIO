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
// we use an instance of the extended interface to use the method of the framework.
@Service
@Transactional
public class MasaccioServiceImpl implements MasaccioService{

    // import of the already implemented interfaces from the framework and we use them.
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SensorRepository sensorRepository;


    // Sensor services
    public List<Sensor> findAllSensorsByArea(String name){
        Area area = areaRepository.findOneByName(name);
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
    public Area findAreaByName(String name){
        return areaRepository.findOneByName(name);
    }

    public List<Area> findAllAreas(){
        return areaRepository.findAll();
    }
    // end Area services
}
