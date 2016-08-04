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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eurekaclinical.i2b2.dao.I2b2ProjectDao;
import org.eurekaclinical.i2b2.entity.I2b2ProjectEntity;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2Project;


/**
 *
 * @author Andrew Post
 */
@Path("/protected/i2b2projects")
@Transactional
public class I2b2ProjectResource {

	private final I2b2ProjectDao<I2b2ProjectEntity> projectDao;

    @Inject
    public I2b2ProjectResource(I2b2ProjectDao<I2b2ProjectEntity> inProjectDao) {
		this.projectDao = inProjectDao;
    }
	
	@RolesAllowed("admin")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<I2b2Project> getAll() {
		List<I2b2Project> results = new ArrayList<>();
        for (I2b2ProjectEntity userEntity : this.projectDao.getAll()) {
            results.add(toProject(userEntity));
        }
        return results;
	}

    protected I2b2Project toProject(I2b2ProjectEntity projectEntity) {
        I2b2Project project = new I2b2Project();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
		project.setI2b2Domain(projectEntity.getI2b2Domain().getId());
        return project;
    }

}
