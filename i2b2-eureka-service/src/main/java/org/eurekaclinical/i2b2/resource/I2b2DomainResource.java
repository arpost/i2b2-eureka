package org.eurekaclinical.i2b2.resource;

/*-
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 - 2016 Emory University
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

import com.google.inject.persist.Transactional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import org.eurekaclinical.common.resource.AbstractNamedReadOnlyResource;
import org.eurekaclinical.i2b2.dao.I2b2DomainDao;
import org.eurekaclinical.i2b2.entity.I2b2DomainEntity;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2Domain;


/**
 *
 * @author Andrew Post
 */
@Path("/protected/i2b2domains")
@Transactional
public class I2b2DomainResource extends AbstractNamedReadOnlyResource<I2b2DomainEntity, I2b2Domain> {

    @Inject
    public I2b2DomainResource(I2b2DomainDao<I2b2DomainEntity> inRoleDao) {
		super(inRoleDao);
    }

	@Override
    protected I2b2Domain toComm(I2b2DomainEntity domainEntity, HttpServletRequest req) {
        I2b2Domain domain = new I2b2Domain();
        domain.setId(domainEntity.getId());
        domain.setName(domainEntity.getName());
		domain.setProxyUrl(domainEntity.getProxyUrl());
		if (req.isUserInRole("admin")) {
			domain.setAdminUsername(domainEntity.getAdminUsername());
			domain.setAdminPassword(domainEntity.getAdminPassword());
		}
        return domain;
    }

	@Override
	protected boolean isAuthorizedEntity(I2b2DomainEntity entity, HttpServletRequest req) {
		return true;
	}

}
