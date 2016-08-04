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
import org.eurekaclinical.i2b2.dao.I2b2RoleDao;
import org.eurekaclinical.i2b2.entity.I2b2RoleEntity;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2Role;


/**
 *
 * @author Andrew Post
 */
@Path("/protected/i2b2roles")
@Transactional
public class I2b2RoleResource extends AbstractNamedReadOnlyResource<I2b2RoleEntity, I2b2Role> {

    @Inject
    public I2b2RoleResource(I2b2RoleDao<I2b2RoleEntity> inRoleDao) {
		super(inRoleDao);
    }

	@Override
    protected I2b2Role toComm(I2b2RoleEntity roleEntity, HttpServletRequest req) {
        I2b2Role role = new I2b2Role();
        role.setId(roleEntity.getId());
        role.setName(roleEntity.getName());
        return role;
    }

	@Override
	protected boolean isAuthorizedEntity(I2b2RoleEntity entity, HttpServletRequest req) {
		return true;
	}

}
