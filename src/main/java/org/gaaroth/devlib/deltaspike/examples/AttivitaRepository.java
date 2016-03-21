package org.gaaroth.devlib.deltaspike.examples;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import org.apache.deltaspike.data.api.EntityManagerConfig;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

@Repository(forEntity = AttivitaEntity.class)
@EntityManagerConfig(entityManagerResolver = EntityManagerResolverBase.class, flushMode = FlushModeType.AUTO)
public abstract class AttivitaRepository implements EntityRepository<AttivitaEntity, Long> {

	@Inject
	@BaseDatabase
	protected EntityManager entityManager;

	public JPAContainer<AttivitaEntity> getJPAContainer() {
		JPAContainer<AttivitaEntity> container = JPAContainerFactory.make(AttivitaEntity.class, entityManager);
		return container;
	}
	
	public abstract List<AttivitaEntity> findByStatoAttivita_idStatoAttivita(Long idStato);
	
	@Modifying
    @Query("DELETE FROM AttivitaEntity as ae where ae.idAttivita = ?1")
	public abstract void removeById(Long id);

	public abstract List<AttivitaEntity> findByDataOraScadenzaLessThan(Date withMillisOfSecond);

}