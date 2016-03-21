package org.gaaroth.devlib.deltaspike.examples;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

@ApplicationScoped
public class EntityManagerProducer {
	
	@Inject
	@PersistenceUnitName("persistence")
	private EntityManagerFactory entityManagerFactoryBase;
	
	@Inject
	@PersistenceUnitName("persistence_metodo")
	private EntityManagerFactory entityManagerFactoryMetodo;
	
	@Inject
	@PersistenceUnitName("persistence_arxivar")
	private EntityManagerFactory entityManagerFactoryArxivar;

	@Produces
	//@RequestScoped
	//@ApplicationScoped
	@BaseDatabase
	public EntityManager createBase() {
		return this.entityManagerFactoryBase.createEntityManager();
	}

	public void disposeBase(@Disposes @BaseDatabase EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}
	
}