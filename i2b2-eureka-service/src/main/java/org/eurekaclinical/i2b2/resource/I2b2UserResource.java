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
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.arp.javautil.password.PasswordGenerator;
import org.arp.javautil.password.PasswordGeneratorImpl;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.i2b2.client.I2b2Client;
import org.eurekaclinical.i2b2.client.I2b2ClientFactory;
import org.eurekaclinical.i2b2.client.I2b2Exception;
import org.eurekaclinical.i2b2.client.I2b2UserSetter;
import org.eurekaclinical.i2b2.client.I2b2UserSetterFactory;
import org.eurekaclinical.i2b2.client.comm.I2b2AuthMetadata;
import org.eurekaclinical.i2b2.dao.I2b2DomainDao;
import org.eurekaclinical.i2b2.dao.I2b2ProjectDao;
import org.eurekaclinical.i2b2.dao.I2b2RoleDao;
import org.eurekaclinical.i2b2.entity.I2b2DomainEntity;
import org.eurekaclinical.i2b2.entity.I2b2ProjectEntity;
import org.eurekaclinical.i2b2.entity.I2b2RoleEntity;
import org.eurekaclinical.i2b2.integration.client.comm.I2b2User;
import org.eurekaclinical.i2b2.props.I2b2EurekaServicesProperties;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.eurekaclinical.useragreement.client.EurekaClinicalUserAgreementClient;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 *
 * @author Andrew Post
 */
@Path("/protected")
@Transactional
public class I2b2UserResource {

	private final I2b2ClientFactory i2b2ClientFactory;
	private final I2b2UserSetterFactory i2b2UserSetterFactory;
	private final I2b2EurekaServicesProperties properties;
	private final EurekaClinicalUserAgreementClient userAgreementClient;
	private final I2b2DomainDao<I2b2DomainEntity> domainDao;
	private final I2b2ProjectDao<I2b2ProjectEntity> i2b2ProjectDao;
	private final I2b2RoleDao<I2b2RoleEntity> i2b2RoleDao;

	@Inject
	public I2b2UserResource(I2b2ClientFactory i2b2ClientFactory,
			I2b2UserSetterFactory i2b2UserSetterFactory,
			I2b2EurekaServicesProperties properties,
			EurekaClinicalUserAgreementClient userAgreementClient,
			I2b2ProjectDao<I2b2ProjectEntity> inI2b2ProjectDao,
			I2b2DomainDao<I2b2DomainEntity> inDomainDao,
			I2b2RoleDao<I2b2RoleEntity> inI2b2RoleDao) {
		this.i2b2ClientFactory = i2b2ClientFactory;
		this.i2b2UserSetterFactory = i2b2UserSetterFactory;
		this.properties = properties;
		this.userAgreementClient = userAgreementClient;
		this.i2b2ProjectDao = inI2b2ProjectDao;
		this.domainDao = inDomainDao;
		this.i2b2RoleDao = inI2b2RoleDao;
	}
	
	@GET
	@Path("/i2b2domains/{id}/i2b2users/{username}")
	public I2b2User getByName(@PathParam("id") Long i2b2DomainId, @PathParam("username") String username, @Context HttpServletRequest req) {
		I2b2DomainEntity domain = this.domainDao.retrieve(i2b2DomainId);
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setRedirectHost(domain.getRedirectHost());
		authMetadata.setProxyUrl(domain.getProxyUrl());
		authMetadata.setDomain(domain.getName());
		authMetadata.setUsername(domain.getAdminUsername());
		authMetadata.setPassword(domain.getAdminPassword());
		try (I2b2Client i2b2Client = this.i2b2ClientFactory.getInstance(authMetadata)) {
			I2b2User result = new I2b2User();
			result.setUsername(username);
			return result;
		} catch (I2b2Exception ex) {
			throw new HttpStatusException(Response.Status.NOT_FOUND, ex);
		}
	}

	/**
	 * Automatically creates user and role records in i2b2's PM cell, if the
	 * user is authorized for eurekaclinical-i2b2-integration and is a member of
	 * a group that has an associated i2b2 project.
	 *
	 * @param req the HTTP servlet request object.
	 * @return the response object.
	 */
	@POST
	@Path("/i2b2users/auto")
	public Response autoCreate(@Context HttpServletRequest req) {
		AttributePrincipal userPrincipal = (AttributePrincipal) req.getUserPrincipal();
		String username = userPrincipal.getName();
		String fullName = username;
		String email = null;
		Set<String> domainCache = new HashSet<>();
		I2b2UserSetter userSetter = this.i2b2UserSetterFactory.getInstance();
		try {
			boolean created = false;
			if (this.properties.getUserAgreementUrl() == null
					|| this.userAgreementClient.getUserAgreementStatus() != null) {
				PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();
				for (I2b2ProjectEntity project : this.i2b2ProjectDao.getI2b2ProjectsForUser(username)) {
					I2b2DomainEntity domain = project.getI2b2Domain();
					I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
					authMetadata.setRedirectHost(domain.getRedirectHost());
					authMetadata.setProxyUrl(domain.getProxyUrl());
					authMetadata.setDomain(domain.getName());
					authMetadata.setUsername(domain.getAdminUsername());
					authMetadata.setPassword(domain.getAdminPassword());
					if (domainCache.add(domain.getName())) {
						userSetter.setUser(authMetadata, username, passwordGenerator.generate(), fullName, email, false);
					}
					try (I2b2Client i2b2Client = this.i2b2ClientFactory.getInstance(authMetadata)) {
						for (I2b2RoleEntity i2b2Role : this.i2b2RoleDao.getI2b2Roles(username, project.getId())) {
							i2b2Client.setRole(username, project.getName(), i2b2Role.getName());
						}
					}
					created = true;
				}
			}
			if (created) {
				return Response.created(URI.create("/" + username)).build();
			} else {
				throw new HttpStatusException(Response.Status.FORBIDDEN);
			}
		} catch (ClientException | I2b2Exception ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
