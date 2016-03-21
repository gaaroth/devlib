package org.gaaroth.devlib.deltaspike.examples;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.joda.time.DateTime;

import com.vaadin.addon.jpacontainer.JPAContainer;

@Stateless
public class AttivitaController {

    @Inject
    AttivitaRepository repository;

    public AttivitaController() {
    	
    }

    public void save(AttivitaEntity entry) {
        repository.save(entry);
    }

    public void delete(AttivitaEntity value) {
        repository.removeById(value.getIdAttivita());
    }

    public List<AttivitaEntity> findAll() {
        return repository.findAll();
    }
    
    public List<AttivitaEntity> findAllScadute() {
    	return repository.findByDataOraScadenzaLessThan(
    		new DateTime().withHourOfDay(0).withMinuteOfHour(0).withMillisOfSecond(0).toDate());
    }
    
    public List<AttivitaEntity> findAllNonChiuse() {
    	List<AttivitaEntity> lista = new ArrayList<AttivitaEntity>();
    	lista.addAll(repository.findByStatoAttivita_idStatoAttivita(1L));
    	lista.addAll(repository.findByStatoAttivita_idStatoAttivita(3L));
        return lista;
    }
    
    public Long count() {
        return repository.count();
    }
    
    public AttivitaEntity refreshEntry(AttivitaEntity entry) {
        return repository.findBy(entry.getIdAttivita());
    }
    
    public JPAContainer<AttivitaEntity> getJPAContainer() {
    	return repository.getJPAContainer();
    }

}