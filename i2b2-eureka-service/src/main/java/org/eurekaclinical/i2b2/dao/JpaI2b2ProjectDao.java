package org.eurekaclinical.i2b2.dao;

/*-
 * #%L
 * Eureka! Clinical User Agreement Service
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import org.eurekaclinical.i2b2.entity.I2b2ProjectEntity;
import org.eurekaclinical.standardapis.dao.GenericDao;


/**
 * 
 *
 * @author Andrew Post
 */
public class JpaI2b2ProjectDao extends GenericDao<I2b2ProjectEntity, Long> implements I2b2ProjectDao<I2b2ProjectEntity> {

    /**
     * Create an object with the give entity manager.
     *
     * @param inEMProvider The entity manager to be used for communication with
     * the data store.
     */
    @Inject
    public JpaI2b2ProjectDao(final Provider<EntityManager> inEMProvider) {
        super(I2b2ProjectEntity.class, inEMProvider);
    }

	@Override
	public I2b2ProjectEntity getI2b2ProjectByName(String name) {
		return getUniqueByAttribute("name", name);
	}
	
}
