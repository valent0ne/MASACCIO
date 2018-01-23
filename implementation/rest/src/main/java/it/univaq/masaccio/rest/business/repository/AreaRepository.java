package it.univaq.masaccio.rest.business.repository;

import it.univaq.masaccio.rest.business.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// we extend the interface of the framework.
public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findAll();

    Area findOneByName(String name);
}
