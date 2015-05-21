package edu.emory.bmi.aiw.i2b2export.resource;

/*
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 Emory University
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
import com.google.inject.Inject;
import edu.emory.bmi.aiw.i2b2export.config.I2b2EurekaServicesProperties;
import edu.emory.bmi.aiw.i2b2export.xml.I2b2ExportServiceXmlException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DefaultSourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig.Section;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.i2b2etl.dsb.I2B2DataSourceBackend;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for i2b2 to request that a specified patient set be sent to another service.
 *
 * @author Andrew Post
 */
@Path("/protected/sendpatientset")
@Produces(MediaType.APPLICATION_JSON)
public class PatientSetSenderResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientSetSenderResource.class);

	private final ServicesClient servicesClient;
	private final I2b2EurekaServicesProperties properties;

	@Inject
	public PatientSetSenderResource(ServicesClient servicesClient, I2b2EurekaServicesProperties inProperties) {
		this.servicesClient = servicesClient;
		this.properties = inProperties;
	}

	@GET
	public void doSend(@QueryParam("resultInstanceId") String resultInstanceId, @QueryParam("action") String actionId) throws I2b2ExportServiceXmlException, ClientException {
		try {
			JobSpec jobSpec = new JobSpec();
			jobSpec.setUpdateData(false);
			jobSpec.setDestinationId(actionId);
			jobSpec.setSourceConfigId(properties.getSourceConfigId());
			SourceConfig sc = new SourceConfig();
			sc.setId(properties.getSourceConfigId());
			Section section = new Section();
			section.setId(I2B2DataSourceBackend.class.getName());
			DefaultSourceConfigOption option = new DefaultSourceConfigOption();
			option.setName("resultInstanceId");
			option.setValue(new Long(resultInstanceId));
			section.setOptions(new SourceConfigOption[]{option});
			sc.setDataSourceBackends(new Section[]{section});
			jobSpec.setPrompts(sc);
			this.servicesClient.submitJob(jobSpec);
		} catch (ClientException ex) {
			logError(ex);
			throw ex;
		}
	}

	private static void logError(Throwable e) {
		LOGGER.error("Exception thrown: {}", e);
	}
}
