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
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.common.resource.AbstractUserResource;
import org.eurekaclinical.i2b2.entity.GroupEntity;
import org.eurekaclinical.i2b2.entity.RoleEntity;
import org.eurekaclinical.i2b2.entity.UserEntity;
import org.eurekaclinical.i2b2.entity.UserTemplateEntity;
import org.eurekaclinical.standardapis.dao.GroupDao;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2IntegrationUser;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/users")
@Transactional
public class UserResource extends AbstractUserResource<I2b2IntegrationUser, UserEntity, RoleEntity> {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(UserResource.class);
	
	private static final AutoAuthCriteriaParser AUTO_AUTH_CRITERIA_PARSER = new AutoAuthCriteriaParser();
	
	private final RoleDao<RoleEntity> roleDao;
	private final GroupDao<GroupEntity> groupDao;
	private final UserTemplateDao<UserTemplateEntity> userTemplateDao;
	private final UserDao<UserEntity> userDao;

	@Inject
	public UserResource(UserDao<UserEntity> inUserDao,
			RoleDao<RoleEntity> inRoleDao,
			GroupDao<GroupEntity> inGroupDao,
			UserTemplateDao<UserTemplateEntity> inUserTemplateDao) {
		super(inUserDao);
		this.userDao = inUserDao;
		this.roleDao = inRoleDao;
		this.groupDao = inGroupDao;
		this.userTemplateDao = inUserTemplateDao;
	}

	@GET
	@Path("/auto")
	@Produces(MediaType.APPLICATION_JSON)
	public I2b2IntegrationUser createOrGetUserAuto(@Context HttpServletRequest req) {
		return toComm(createOrGetUserEntity(req), req);
	}

	@POST
	@Path("/auto")
	public Response createUserAuto(@Context HttpServletRequest req) {
		return Response.created(URI.create("/" + createOrGetUserEntity(req).getId())).build();
	}

	private UserEntity createOrGetUserEntity(HttpServletRequest req) {
		AttributePrincipal userPrincipal = (AttributePrincipal) req.getUserPrincipal();
		Map<String, Object> attributes = userPrincipal.getAttributes();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User {} has attributes {}",
					new Object[]{
						req.getRemoteUser(),
						attributes
					});
		}
		boolean autoAuthorizationPermitted = true;
		if (autoAuthorizationPermitted) {
			String remoteUser = req.getRemoteUser();
			if (remoteUser != null) {
				UserEntity user = this.userDao.getByName(remoteUser);
				if (this.userDao.getByName(remoteUser) == null) {
					UserTemplateEntity autoAuthorizationTemplate
							= this.userTemplateDao.getAutoAuthorizationTemplate();
					try {
						if (autoAuthorizationTemplate != null
								&& AUTO_AUTH_CRITERIA_PARSER.parse(autoAuthorizationTemplate.getCriteria(), attributes)) {
							user = toUserEntity(autoAuthorizationTemplate, remoteUser);
							this.userDao.create(user);
							return user;
						} else {
							throw new HttpStatusException(Response.Status.FORBIDDEN);
						}
					} catch (CriteriaParseException ex) {
						throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR);
					}
				} else {
					return user;
				}
			} else {
				throw new HttpStatusException(Response.Status.UNAUTHORIZED);
			}
		} else {
			throw new HttpStatusException(Response.Status.FORBIDDEN);
		}
	}

	private UserEntity toUserEntity(UserTemplateEntity userTemplate, String username) {
		UserEntity user = new UserEntity();
		user.setUsername(username);
		user.setGroups(userTemplate.getGroups());
		user.setRoles(userTemplate.getRoles());
		return user;
	}

	@Override
	protected I2b2IntegrationUser toComm(UserEntity userEntity, HttpServletRequest req) {
		I2b2IntegrationUser user = new I2b2IntegrationUser();
		user.setId(userEntity.getId());
		user.setUsername(userEntity.getUsername());
		List<Long> roles = new ArrayList<>();
		for (RoleEntity roleEntity : userEntity.getRoles()) {
			roles.add(roleEntity.getId());
		}
		user.setRoles(roles);
		List<Long> groups = new ArrayList<>();
		for (GroupEntity groupEntity : userEntity.getGroups()) {
			groups.add(groupEntity.getId());
		}
		user.setGroups(groups);
		return user;
	}

	@Override
	protected UserEntity toEntity(I2b2IntegrationUser user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setUsername(user.getUsername());
		List<RoleEntity> roleEntities = this.roleDao.getAll();
		for (Long roleId : user.getRoles()) {
			for (RoleEntity roleEntity : roleEntities) {
				if (roleEntity.getId().equals(roleId)) {
					userEntity.addRole(roleEntity);
				}
			}
		}
		List<GroupEntity> groupEntities = this.groupDao.getAll();
		for (Long groupId : user.getGroups()) {
			for (GroupEntity groupEntity : groupEntities) {
				if (groupEntity.getId().equals(groupId)) {
					userEntity.addGroup(groupEntity);
				}
			}
		}
		return userEntity;
	}

}
