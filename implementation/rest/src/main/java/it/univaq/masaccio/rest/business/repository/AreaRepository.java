package it.univaq.masaccio.rest.business.repository;

import it.univaq.masaccio.rest.business.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findAll();

    Area findOne(Long id);
}
