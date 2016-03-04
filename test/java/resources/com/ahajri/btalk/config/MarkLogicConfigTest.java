package com.ahajri.btalk.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.RestTemplate;

import com.ahajri.btalk.data.domain.Discussion;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.query.QueryManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPDigestAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

@Configuration
@ComponentScan
@PropertySource({ "classpath:ml-config.properties",
		"classpath:application.properties", "classpath:jndi.properties" })
public class MarkLogicConfigTest {

	public static final Logger LOGGER = Logger.getLogger(MarkLogicConfig.class);

	public String host = "localhost";

	public String port = "8000";

	public String username = "ahajri";

	public String password = "mafhh14$";

	public String db = "btalkdb";

	@Bean
	public DatabaseClient getDatabaseClient() {
		try {
			System.out.println("#####################################");
			DatabaseClientFactory.getHandleRegistry().register(
					JAXBHandle.newFactory(Discussion.class));
		} catch (JAXBException e) {
			LOGGER.error(e);
		}
		return DatabaseClientFactory.newClient(host,
				Integer.parseInt(port.trim()), db, username, password,
				DatabaseClientFactory.Authentication.DIGEST);
	}

	@Bean
	public QueryManager getQueryManager() {
		return getDatabaseClient().newQueryManager();
	}

	@Bean
	public XMLDocumentManager getXMLDocumentManager() {
		return getDatabaseClient().newXMLDocumentManager();
	}

	@Bean
	public JSONDocumentManager getJSONDocumentManager() {
		return getDatabaseClient().newJSONDocumentManager();
	}

	@Bean
	public String getMarkLogicBaseURL() {
		System.out.println("MarkLogic URL: "
				+ String.format("http://%s:%s", host, port));
		return String.format("http://%s:%s", host, port);
	}

	@Bean
	public Client getJerseyClient() {
		Client client = Client.create(); // thread-safe
		client.addFilter(new LoggingFilter());
		client.addFilter(new HTTPDigestAuthFilter(username, password));
		return client;
	}

	@Bean
	public RestTemplate restTemplateInfocentre() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		messageConverters.add(new MarshallingHttpMessageConverter(
				new XStreamMarshaller()));
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}

}
