package org.eurekaclinical.i2b2.resource;

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
import com.google.inject.persist.Transactional;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.eurekaclinical.common.resource.AbstractGroupResource;
import org.eurekaclinical.i2b2.dao.I2b2ProjectDao;
import org.eurekaclinical.i2b2.dao.I2b2RoleDao;
import org.eurekaclinical.i2b2.entity.GroupEntity;
import org.eurekaclinical.i2b2.entity.I2b2ProjectEntity;
import org.eurekaclinical.i2b2.entity.I2b2RoleEntity;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2IntegrationGroup;
import org.eurekaclinical.standardapis.dao.GroupDao;
import org.eurekaclinical.standardapis.exception.HttpStatusException;


/**
 *
 * @author Andrew Post
 */
@Path("/protected/groups")
@Transactional
public class GroupResource extends AbstractGroupResource<GroupEntity, I2b2IntegrationGroup> {

	private final I2b2ProjectDao<I2b2ProjectEntity> i2b2ProjectDao;
	private final I2b2RoleDao<I2b2RoleEntity> i2b2RoleDao;

    @Inject
    public GroupResource(GroupDao<GroupEntity> inGroupDao, 
			I2b2ProjectDao<I2b2ProjectEntity> inI2b2ProjectDao,
			I2b2RoleDao<I2b2RoleEntity> inI2b2RoleDao) {
        super(inGroupDao);
		this.i2b2ProjectDao = inI2b2ProjectDao;
		this.i2b2RoleDao = inI2b2RoleDao;
    }

    @Override
    protected I2b2IntegrationGroup toComm(GroupEntity groupEntity, HttpServletRequest req) {
        I2b2IntegrationGroup group = new I2b2IntegrationGroup();
        group.setId(groupEntity.getId());
        group.setName(groupEntity.getName());
		List<Long> i2b2Roles = new ArrayList<>();
		for (I2b2RoleEntity i2b2Role : groupEntity.getI2b2Roles()) {
			i2b2Roles.add(i2b2Role.getId());
		}
		group.setI2b2Roles(i2b2Roles);
		List<Long> i2b2Projects = new ArrayList<>();
		for (I2b2ProjectEntity i2b2Project : groupEntity.getI2b2Projects()) {
			i2b2Projects.add(i2b2Project.getId());
		}
		group.setI2b2Projects(i2b2Projects);
        return group;
    }

	@Override
	protected GroupEntity toEntity(I2b2IntegrationGroup commObj) {
		GroupEntity entity = new GroupEntity();
		entity.setId(commObj.getId());
		entity.setName(commObj.getName());
		for (Long i2b2ProjectId : commObj.getI2b2Projects()) {
			I2b2ProjectEntity i2b2ProjectEntity = this.i2b2ProjectDao.retrieve(i2b2ProjectId);
			if (i2b2ProjectEntity != null) {
				entity.addI2b2Project(i2b2ProjectEntity);
			} else {
				throw new HttpStatusException(Response.Status.BAD_REQUEST);
			}
		}
		for (Long i2b2RoleId : commObj.getI2b2Roles()) {
			I2b2RoleEntity i2b2RoleEntity = this.i2b2RoleDao.retrieve(i2b2RoleId);
			if (i2b2RoleEntity != null) {
				entity.addI2b2Role(i2b2RoleEntity);
			} else {
				throw new HttpStatusException(Response.Status.BAD_REQUEST);
			}
		}
		return entity;
	}

	@Override
	protected boolean isAuthorizedComm(I2b2IntegrationGroup commObj, HttpServletRequest req) {
		return true;
	}

	@Override
	protected boolean isAuthorizedEntity(GroupEntity entity, HttpServletRequest req) {
		return true;
	}

}
