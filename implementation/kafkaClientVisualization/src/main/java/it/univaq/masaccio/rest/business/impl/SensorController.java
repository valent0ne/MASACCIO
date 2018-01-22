package it.univaq.masaccio.rest.business.impl;

import it.univaq.masaccio.rest.business.domain.Sensor;
import it.univaq.masaccio.rest.business.repository.MasaccioService;
import it.univaq.masaccio.rest.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensor")
public class SensorController {
    @Autowired
    private MasaccioService service;

    @GetMapping("/{id}")
    public Response findSensorById(@PathVariable(value = "id") Long id) throws Exception {
        Sensor sensor = service.findSensorById(id);
        Response<Sensor> response = new Response<>(true, "sensor with id: " + id);
        response.setData(sensor);
        return response;
    }

    @GetMapping("/")
    public Response findAllAreas() throws Exception {
        List<Sensor> sensors = service.findAllSensors();
        Response<List<Sensor>> response = new Response<>(true, "all sensors");
        response.setData(sensors);
        return response;
    }

}
