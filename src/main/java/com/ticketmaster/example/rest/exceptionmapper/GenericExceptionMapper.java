package com.ticketmaster.example.rest.exceptionmapper;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.xml.XmlMapper;

/**
 * 
 * @author Vincent.Furlanetto
 * 
 *         This is a Generic class that can be used for mapping most Exceptions
 *         in a RESTful service. In this Generic Service and Exception that is
 *         an instanceof RuntimeException will have an HTTP response code of 500
 *         and all other Exception/Errors will have an HTTP response code of 400
 * 
 * @param <T>
 *            The exception Class that this mapper should handle
 * 
 */

public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOG = LoggerFactory
			.getLogger(GenericExceptionMapper.class);
	private ObjectMapper jsonMapper;
	private String outputMediaType;
	private ObjectMapper xmlMapper;

	/**
	 * 
	 * @param exceptionType
	 * @param outputMediaType
	 */
	public GenericExceptionMapper(String outputMediaType) {

		// Defaulting to the JSON
		if (!MediaType.APPLICATION_XML.equals(outputMediaType)
				&& !MediaType.APPLICATION_JSON.equals(outputMediaType)) {

			LOG.debug("Invalid outputMediaType specified, defaulting to "
					+ MediaType.APPLICATION_JSON.toString());

			outputMediaType = MediaType.APPLICATION_JSON;
		}

		this.outputMediaType = outputMediaType;

		// Initializing the JSON mapper
		jsonMapper = new ObjectMapper();

		// Initializing the XML mapper
		xmlMapper = new XmlMapper();
	}

	/**
	 * 
	 * @param throwable
	 * @returns the Throwable object in a serialized JSON string or null if
	 *          unable to serialize
	 */
	public String serializeToJSONString(Throwable throwable) {
		try {

			if (jsonMapper.canSerialize(throwable.getClass())) {

				String json = jsonMapper.writeValueAsString(throwable);
				return json;
			}
		} catch (IOException e) {
			LOG.error("Unable to seriazlize object to JSON", e);
		}
		return null;
	}

	/**
	 * 
	 * @param throwable
	 * @returns the Throwable object in a serialized XML string or null if
	 *          unable to serialize
	 * @throws JAXBException
	 */

	public String serializeToXMLString(Throwable throwable) {
		try {

			if (xmlMapper.canSerialize(throwable.getClass())) {
				String xml = xmlMapper.writeValueAsString(throwable);
				return xml;
			}

		} catch (IOException e) {
			LOG.error("Unable to seriazlize object to Xml", e);
		}
		return null;

	}

	/**
	 * 
	 * @param mediaType
	 * @param throwable
	 * @returns the the throwable object serialized to the appropriate mediaType
	 *          given if the method can serialize the throwable to the mediaType
	 *          supplied the String representation of the throwable will be
	 *          returned by invoking throwable.toString()
	 */
	public String serializeByMediaType(String mediaType, Throwable throwable) {

		if (throwable == null) {
			LOG.error("serializeByMediaType --> Throwable cannot be null, unable to serialized object to mediaType");
			return null;
		}

		String serializedThrowable = null;
		if (MediaType.APPLICATION_XML.equals(mediaType)) {
			serializedThrowable = serializeToXMLString(throwable);
		} else if (MediaType.APPLICATION_JSON.equals(mediaType)) {
			serializedThrowable = serializeToJSONString(throwable);
		}

		// If the normal serialization could be be done, return the string
		// representation of the throwable
		if (serializedThrowable == null || serializedThrowable.trim().isEmpty()) {
			serializedThrowable = throwable.toString();
		}

		return serializedThrowable;
	}

	@Override
	/**
	 * This method will return a BAD_REQUEST status for all statuses that are of instance of RuntimeException, 
	 * For all other Exceptions/Errors the response will be an internal server error
	 */
	public Response toResponse(Throwable throwable) {

		ResponseBuilder responseBuilder = null;
		if (throwable instanceof RuntimeException) {
			responseBuilder = Response
					.status(Response.Status.INTERNAL_SERVER_ERROR);
		} else {
			responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		}

		return responseBuilder
				.entity(serializeByMediaType(outputMediaType, throwable))
				.type(outputMediaType).build();
	}

}
