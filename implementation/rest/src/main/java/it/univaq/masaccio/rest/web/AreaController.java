package it.univaq.masaccio.rest.web;

import it.univaq.masaccio.rest.business.domain.Area;
import it.univaq.masaccio.rest.business.domain.Sensor;
import it.univaq.masaccio.rest.business.repository.MasaccioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private MasaccioService service;

    @GetMapping("/{id}")
    public Response findAreaById(@PathVariable(value = "id") Long id) throws Exception{
        Area area = service.findAreaById(id);
        Response<Area> response = new Response<>(true, "area with id: "+id);
        response.setData(area);
        return response;
    }

    @GetMapping("/")
    public Response findAllAreas() throws Exception{
        List<Area> areas = service.findAllAreas();
        Response<List<Area>> response = new Response<>(true, "all areas");
        response.setData(areas);
        return response;
    }

    @GetMapping("/{id}/sensors")
    public Response findAllSensorsByArea(@PathVariable(value = "id") Long id) throws Exception{
        List<Sensor> sensors = service.findAllSensorsByArea(id);
        Response<List<Sensor>> response = new Response<>(true, "all sensors in area: "+id);
        response.setData(sensors);
        return response;

    }


}
