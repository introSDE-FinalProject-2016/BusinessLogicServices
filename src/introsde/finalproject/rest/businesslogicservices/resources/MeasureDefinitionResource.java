package introsde.finalproject.rest.businesslogicservices.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import introsde.finalproject.rest.businesslogicservices.util.UrlInfo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

@Stateless
@LocalBean
public class MeasureDefinitionResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private UrlInfo urlInfo;
	private String storageServiceURL;

	/**
	 * initialize the connection with the Storage Service (SS)
	 */
	public MeasureDefinitionResource(UriInfo uriInfo, Request request) {
		this.uriInfo = uriInfo;
		this.request = request;

		this.urlInfo = new UrlInfo();
		this.storageServiceURL = urlInfo.getStorageURL();
	}

	private String errorMessage(Exception e) {
		return "{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "
				+ e + "\"}";
	}

	private String externalErrorMessage(String e) {
		return "{ \n \"error\" : \"Error in External services, due to the exception: "
				+ e + "\"}";
	}

	/**
	 * GET /businessLogic-service/measureDefinition 
	 * This method calls a getMeasureDefinition method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readMeasureDefinition() {
		try {
			System.out
					.println("readMeasureDefinition: Reading Measure Definition from Storage Services Module in Business Logic Services");

			String path = "/measureDefinition";
			String xmlResponse;

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(storageServiceURL + path);
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONObject obj = new JSONObject(result.toString());

			if (response.getStatusLine().getStatusCode() == 200) {
				// measureNames object json
				xmlResponse = "<measureNames>";

				JSONArray arr = (JSONArray) obj.get("measureName");

				for (int i = 0; i < arr.length(); i++) {
					// measureName array json
					xmlResponse += "<measureName>" + arr.getString(i)
							+ "</measureName>";
				}
				xmlResponse += "</measureNames>";

				System.out.println(prettyXMLPrint(xmlResponse));

				JSONObject xmlJSONObj = XML.toJSONObject(xmlResponse);
				String jsonPrettyPrintString = xmlJSONObj.toString(4);
				return Response.ok(jsonPrettyPrintString).build();

			} else {
				System.out
						.println("Storage Service Error response.getStatus() != 200");
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(externalErrorMessage(response.toString()))
						.build();
			}
		} catch (Exception e) {
			System.out
					.println("Business Logic Service Error catch response.getStatus() != 200");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage(e)).build();
		}
	}

	/**
	 * Prints pretty format for XML
	 * 
	 * @param xml
	 * @return
	 * @throws TransformerException
	 */
	public String prettyXMLPrint(String xmlString) throws TransformerException {

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		Source xmlInput = new StreamSource(new StringReader(xmlString));
		StringWriter stringWriter = new StringWriter();
		StreamResult xmlOutput = new StreamResult(stringWriter);

		transformer.transform(xmlInput, xmlOutput);
		return xmlOutput.getWriter().toString();
	}
}
