package edu.emory.bmi.aiw.i2b2export.i2b2;

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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
public class I2b2XmlPostSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(I2b2XmlPostSupport.class);

	private final I2b2EurekaServicesProperties properties;

	@Inject
	public I2b2XmlPostSupport(I2b2EurekaServicesProperties inProperties) {
		this.properties = inProperties;
	}

	/**
	 * Performs an HTTP POST of an XML request to the i2b2 proxy cell. Request
	 * XML must contain the redirect_url node with the address of the actual
	 * service endpoint to call in order for the request to be properly routed.
	 *
	 * @param xml the XML request to send
	 * @return the XML response from i2b2 as a {@link Document}
	 * @throws ClientProtocolException if an error occurs
	 * @throws IOException if an error occurs
	 * @throws ParserConfigurationException if an error occurs
	 * @throws SAXException if an error occurs
	 * @throws IllegalStateException if an error occurs
	 */
	public Document postXmlToI2b2(String xml) throws ClientProtocolException, IOException, IllegalStateException,
			SAXException, ParserConfigurationException {
		LOGGER.debug("POSTing XML: {}", xml);
		HttpClient http = new DefaultHttpClient();
		HttpPost i2b2Post = new HttpPost(this.properties.getProxyUrl());
		StringEntity xmlEntity = new StringEntity(xml);
		xmlEntity.setContentType("text/xml");
		i2b2Post.setEntity(xmlEntity);
		HttpResponse resp = http.execute(i2b2Post);
		try (InputStream contentIn = resp.getEntity().getContent()) {
			Document result = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(contentIn);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Response XML: {}", xmlToString(result));
			}
			return result;
		}
	}

	private static String xmlToString(Node node) {
		Source source = new DOMSource(node);
		StringWriter stringWriter = new StringWriter();
		try {
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			stringWriter.close();
		} catch (IOException | TransformerException e) {
			LOGGER.error("Error in xmlToString: ", e);
			try {
				stringWriter.close();
			} catch (IOException ignore) {
			}
			return null;
		}
		return stringWriter.getBuffer().toString();
	}

	/**
	 * Generates a random message number for i2b2 requests. Copied from the i2b2
	 * SMART project: https://community.i2b2.org/wiki/display/SMArt/SMART+i2b2
	 * in class: edu.harvard.i2b2.smart.ws.SmartAuthHelper
	 *
	 * @return a unique message ID
	 */
	public String generateMessageId() {
		StringWriter strWriter = new StringWriter();
		for (int i = 0; i < 20; i++) {
			int num = getValidAcsiiValue();
			strWriter.append((char) num);
		}
		return strWriter.toString();
	}

	private static int getValidAcsiiValue() {
		int number = 48;
		while (true) {
			number = 48 + (int) Math.round(Math.random() * 74);
			if ((number > 47 && number < 58) || (number > 64 && number < 91)
					|| (number > 96 && number < 123)) {
				break;
			}
		}
		return number;
	}
}
