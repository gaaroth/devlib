package org.gaaroth.devlib.deltaspike.examples;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.deltaspike.data.api.EntityManagerResolver;

public class EntityManagerResolverBase implements EntityManagerResolver {

	@Inject
    @BaseDatabase
    private EntityManager entityManager;

    @Override
    public EntityManager resolveEntityManager() {   
    	return entityManager;
    }
     

}
