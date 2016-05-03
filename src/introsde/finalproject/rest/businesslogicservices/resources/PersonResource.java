package introsde.finalproject.rest.businesslogicservices.resources;

import introsde.finalproject.rest.businesslogicservices.util.UrlInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
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
public class PersonResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private UrlInfo urlInfo;
	private String storageServiceURL;

	private static String mediaType = MediaType.APPLICATION_JSON;

	/**
	 * initialize the connection with the Storage Service (SS)
	 */
	public PersonResource(UriInfo uriInfo, Request request) {
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

	// ******************* PERSON ***********************
	
	/**
	 * GET /business-service/person 
	 * This method calls a getPersonList method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readPersonListDetails() {
		try {
			System.out
					.println("readPersonList: Reading list of details about all people from Storage Services Module in Business Logic Services...");

			String path = "/person";

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
				
				return Response.ok(obj.toString()).build();
				
/*				// people object json
				xmlResponse = "<people>";

				JSONArray arr = (JSONArray) obj.getJSONArray("person");

				for (int i = 0; i < arr.length(); i++) {

					// person array json
					xmlResponse += "<person>";

					xmlResponse += "<pid>" + arr.getJSONObject(i).get("pid")
							+ "</pid>";
					xmlResponse += "<firstname>"
							+ arr.getJSONObject(i).get("firstname")
							+ "</firstname>";
					xmlResponse += "<lastname>"
							+ arr.getJSONObject(i).get("lastname")
							+ "</lastname>";
					xmlResponse += "<birthdate>"
							+ arr.getJSONObject(i).get("birthdate")
							+ "</birthdate>";
					xmlResponse += "<email>"
							+ arr.getJSONObject(i).get("email") + "</email>";
					xmlResponse += "<gender>"
							+ arr.getJSONObject(i).get("gender") + "</gender>";

					
					 * xmlResponse += "<currentHealth>"; JSONObject currentObj =
					 * (JSONObject)
					 * arr.getJSONObject(i).getJSONObject("currentHealth");
					 * JSONArray measureArr =
					 * currentObj.getJSONArray("measure"); for(int j=0;
					 * j<measureArr.length(); j++){
					 * 
					 * //measure array json xmlResponse += "<measure>";
					 * 
					 * xmlResponse += "<mid>" +
					 * measureArr.getJSONObject(j).get("mid") + "</mid>";
					 * xmlResponse += "<name>" +
					 * measureArr.getJSONObject(j).get("name") + "</name>";
					 * xmlResponse += "<value>" +
					 * measureArr.getJSONObject(j).get("value") + "</value>";
					 * xmlResponse += "<created>" +
					 * measureArr.getJSONObject(j).get("created") +
					 * "</created>";
					 * 
					 * xmlResponse += "</measure>"; } xmlResponse +=
					 * "</currentHealth>";
					 

					
					 * xmlResponse += "<goals>"; JSONObject goalsObj =
					 * (JSONObject) arr.getJSONObject(i).getJSONObject("goals");
					 * JSONArray goalArr = goalsObj.getJSONArray("goal");
					 * 
					 * for(int k=0; k<goalArr.length(); k++){
					 * 
					 * //goal array json xmlResponse += "<goal>";
					 * 
					 * xmlResponse += "<gid>" +
					 * goalArr.getJSONObject(k).get("gid") + "</gid>";
					 * xmlResponse += "<type>" +
					 * goalArr.getJSONObject(k).get("type") + "</type>";
					 * xmlResponse += "<value>" +
					 * goalArr.getJSONObject(k).get("value") + "</value>";
					 * xmlResponse += "<startDateGoal>" +
					 * goalArr.getJSONObject(k).get("startDateGoal") +
					 * "</startDateGoal>"; xmlResponse += "<endDateGoal>" +
					 * goalArr.getJSONObject(k).get("endDateGoal") +
					 * "</endDateGoal>"; xmlResponse += "<achieved>" +
					 * goalArr.getJSONObject(k).get("achieved") + "</achieved>";
					 * 
					 * xmlResponse += "</goal>"; } xmlResponse += "</goals>";
					 

					xmlResponse += "</person>";
				}

				xmlResponse += "</people>";

				System.out.println(prettyXMLPrint(xmlResponse));

				JSONObject xmlJSONObj = XML.toJSONObject(xmlResponse);
				String jsonPrettyPrintString = xmlJSONObj.toString(4);
				return Response.ok(jsonPrettyPrintString).build();
				*/
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
	 * GET /business-service/person/{idPerson}  
	 * This method calls a getPerson method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Path("{pid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readPerson(@PathParam("pid") int idPerson){
    	try{
    		System.out
			.println("readPerson: Reading Person with " + idPerson + " from Storage Services Module in Business Logic Services...");
    		
    		String path = "/person/" + idPerson;
        	
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpGet request = new HttpGet(storageServiceURL + path);
        	HttpResponse response = client.execute(request);
        	
        	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	StringBuffer result = new StringBuffer();
        	String line = "";
        	while ((line = rd.readLine()) != null) {
        	    result.append(line);
        	}

        	JSONObject o = new JSONObject(result.toString());

        	if(response.getStatusLine().getStatusCode() == 200){
                return Response.ok(o.toString()).build();
        	}else{
        		System.out
				.println("Storage Service Error response.getStatus() != 200");
        		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(externalErrorMessage(response.toString()))
				.build();
        		//return Response.status(204).build();
        	}
      
    	}catch(Exception e){
    		System.out
			.println("Business Logic Service Error catch response.getStatus() != 200");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
    	}
    	
    }
	
	/**
	 * POST /business-service/person 
	 * This method calls a createPerson method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response insertNewPerson(String inputPersonJSON) {
		try {

			System.out
					.println("insertNewPerson: Inserting a new Person from Storage Services Module in Business Logic Services");

			String path = "/person";

			Client client = ClientBuilder.newClient();
			WebTarget service = client.target(storageServiceURL + path);
			Builder builder = service.request(mediaType);

			Response response = builder.post(Entity.json(inputPersonJSON));

			String result = response.readEntity(String.class);
			
			if (response.getStatus() != 201) {
				System.out
						.println("Storage Service Error response.getStatus() != 201");
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(externalErrorMessage(response.toString()))
						.build();
			} else {
				return Response.ok(result).build();
			}
		} catch (Exception e) {
			System.out
					.println("Business Logic Service Error catch response.getStatus() != 201");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage(e)).build();
		}
	}

	/**
	 * PUT /business-service/person/{idPerson} 
	 * This method calls a updatePerson method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@PUT
	@Path("{pid}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updatePerson(@PathParam("pid") int idPerson, String inputPersonJSON){
    	try{
    		System.out
			.println("updatePerson: Updating Person from Storage Services Module in Business Logic Services");
    		
    		String path = "/person/" + idPerson;
    		
    		Client client = ClientBuilder.newClient();
    		WebTarget webTarget = client.target(storageServiceURL + path);
    		Builder builder = webTarget.request(mediaType);
    		
    		Response response = builder.put(Entity.json(inputPersonJSON));
    		
    		String result = response.readEntity(String.class);
    		
    		if (response.getStatus() != 200) {
    			System.out
					.println("Storage Service Error response.getStatus() != 200");
    			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    					.entity(externalErrorMessage(response.toString()))
    					.build();
    		} else {
				return Response.ok(result).build();
			}
    	}catch(Exception e){
    		System.out.println("Business Logic Service Error catch response.getStatus() != 200");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage(e)).build();
    	}
    }
	
	/**
	 * DELETE /business-service/person/{idPerson} 
	 * This method calls a deletePerson method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@DELETE
	@Path("{pid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deletePerson(@PathParam("pid") int idPerson){
    	try{
    		System.out
			.println("deletePerson: Deleting Person from Storage Services Module in Business Logic Services");
    		
    		String path = "/person/" + idPerson;
    		
    		Client client = ClientBuilder.newClient();
    		WebTarget webTarget = client.target(storageServiceURL + path);
    		Builder builder = webTarget.request(mediaType);
    		
    		Response response = builder.delete();
    		
    		String result = response.readEntity(String.class);
    		
    		if (response.getStatus() != 204) {
    			System.out
					.println("Storage Service Error response.getStatus() != 204");
    			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    					.entity(externalErrorMessage(response.toString()))
    					.build();
    		} else {
				return Response.ok(result).build();
			}
    	}catch(Exception e){
    		System.out.println("Business Logic Service Error catch response.getStatus() != 204");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage(e)).build();
    	}
    }
	
	/**
	 * GET /business-service/person/{idPerson}/current-health 
	 * This method calls a getPerson method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Path("{pid}/current-health")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCurrentHealthDetails(@PathParam("pid") int idPerson) {
		try {
			System.out
					.println("readCurrentHealthDetails: Reading list of all current measures for a person with " +  idPerson + " from Storage Services Module in Business Logic Services...");

			String path = "/person/" + idPerson;
			String xmlResponse = null;
			
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
				
				xmlResponse = "<currentHealth-profile>";
				JSONObject currentObj = (JSONObject) obj.get("currentHealth");
				
				JSONArray measureArr = currentObj.getJSONArray("measure"); 
				for(int j=0;j<measureArr.length(); j++){
					//measure array json 
					xmlResponse += "<measure>";
						xmlResponse += "<mid>" + measureArr.getJSONObject(j).get("mid") + "</mid>";
						xmlResponse += "<name>" + measureArr.getJSONObject(j).get("name") + "</name>";
						xmlResponse += "<value>" + measureArr.getJSONObject(j).get("value") + "</value>";
						xmlResponse += "<created>" + measureArr.getJSONObject(j).get("created") + "</created>";
					xmlResponse += "</measure>"; 
				} 
				xmlResponse += "</currentHealth-profile>";

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
	 * GET /business-service/person/{idPerson}/history-health 
	 * This method calls a getHistoryHealth method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Path("{pid}/history-health")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readHistoryHealthDetails(@PathParam("pid") int idPerson) {
		try {
			System.out
					.println("readHistoryHealthDetails: Reading list of all history measures for a person with " +  idPerson + " from Storage Services Module in Business Logic Services...");

			String path = "/person/" + idPerson + "/historyHealth";
			String xmlResponse = null;
			
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
				
				xmlResponse = "<historyHealth-profile>";
				
				JSONArray measureArr = (JSONArray) obj.getJSONArray("measure"); 
				for(int j=0;j<measureArr.length(); j++){
					
					//measure array json 
					xmlResponse += "<measure>";
						xmlResponse += "<mid>" + measureArr.getJSONObject(j).get("mid") + "</mid>";
						xmlResponse += "<name>" + measureArr.getJSONObject(j).get("name") + "</name>";
						xmlResponse += "<value>" + measureArr.getJSONObject(j).get("value") + "</value>";
						xmlResponse += "<created>" + measureArr.getJSONObject(j).get("created") + "</created>";
					xmlResponse += "</measure>"; 
				} 
				
				xmlResponse += "</historyHealth-profile>";

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

	
	
	// ******************* MEASURE ***********************
	
	/**
	 * GET /business-service/person/{idPerson}/health-profile/{measureName} 
	 * This method calls a getMeasure method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Path("{pid}/health-profile/{measureName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readMeasureNameDetails(@PathParam("pid") int idPerson, @PathParam("measureName") String measureName) {
		try {
			System.out
					.println("readMeasureHealthDetails: Reading list of all " + measureName + " for a person with " +  idPerson + " from Storage Services Module in Business Logic Services...");

			String path = "/person/" + idPerson + "/measure/" + measureName;
			String xmlResponse = null;
			
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
				
				xmlResponse = "<health-profile>";
				
				JSONArray measureArr = (JSONArray) obj.getJSONArray("measure"); 
				for(int j=0;j<measureArr.length(); j++){
					
					//measure array json 
					xmlResponse += "<" + measureName + ">";
						xmlResponse += "<mid>" + measureArr.getJSONObject(j).get("mid") + "</mid>";
						xmlResponse += "<name>" + measureArr.getJSONObject(j).get("name") + "</name>";
						xmlResponse += "<value>" + measureArr.getJSONObject(j).get("value") + "</value>";
						xmlResponse += "<created>" + measureArr.getJSONObject(j).get("created") + "</created>";
					xmlResponse += "</" + measureName + ">"; 
				} 
				
				xmlResponse += "</health-profile>";

				System.out.println(prettyXMLPrint(xmlResponse));

				JSONObject xmlJSONObj = XML.toJSONObject(xmlResponse);
				String jsonPrettyPrintString = xmlJSONObj.toString(4);
				return Response.ok(jsonPrettyPrintString).build();	
			}else {
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

	
	// ******************* GOAL ***********************
	
	/**
	 * GET /business-service/person/{idPerson}/goal  
	 * This method calls a getGoalList method in
	 * Storage Services Module
	 * 
	 * @return
	 */
	@GET
	@Path("{pid}/goal")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readGoalListDetails(@PathParam("pid") int idPerson){
    	try{
    		System.out
			.println("readGoalListDetails: Reading list of all goals for Person with " + idPerson + " from Storage Services Module in Business Logic Services...");
    		
    		String path = "/person/" + idPerson + "/goal";
        	
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpGet request = new HttpGet(storageServiceURL + path);
        	HttpResponse response = client.execute(request);
        	
        	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	StringBuffer result = new StringBuffer();
        	String line = "";
        	while ((line = rd.readLine()) != null) {
        	    result.append(line);
        	}

        	JSONObject o = new JSONObject(result.toString());

        	if(response.getStatusLine().getStatusCode() == 200){
                return Response.ok(o.toString()).build();
                
        	}else{
        		System.out
				.println("Storage Service Error response.getStatus() != 200");
        		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(externalErrorMessage(response.toString()))
				.build();
        		//return Response.status(204).build();
        	}
      
    	}catch(Exception e){
    		System.out
			.println("Business Logic Service Error catch response.getStatus() != 200");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
    	}
    	
    }
	
	@GET
	@Path("{pid}/comparison-value/{measureName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response comparisonValueOfMeasure(@PathParam("pid") int idPerson, @PathParam("measureName") String measureName){
		try{
			System.out
			.println("comparisonValueOfMeasure: Getting the result about the comparison of the value's " + measureName + " between goal and currentHealth for Person with " + idPerson + " from Storage Services Module in Business Logic Services...");
    		
    		String path = "/person/" + idPerson;
    		
    		String xmlBuild = "";
    		String comparison = "";
    		
    		double currentMeasureValueDouble = -1.;
			double goalValueDouble = -1.;
    		
			int currentMeasureValueInt = -1;
			int goalValueInt = -1;
			
    		
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpGet request = new HttpGet(storageServiceURL + path);
        	HttpResponse response = client.execute(request);
        	
        	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	StringBuffer result = new StringBuffer();
        	String line = "";
        	while ((line = rd.readLine()) != null) {
        	    result.append(line);
        	}
        	
        	//person json obj
        	JSONObject obj = new JSONObject(result.toString());
			
        	//GETTING CURRENT MEASURE LIST FOR A SPECIFIED PERSON
        	//currentHealth json obj - measure json array
        	JSONArray measureArr = (JSONArray) obj.getJSONObject("currentHealth").getJSONArray("measure");
        	for(int i = 0; i < measureArr.length(); i++){
        		if((measureArr.getJSONObject(i).getString("name")).equals(measureName)){
        			String target = measureArr.getJSONObject(i).getString("name");
        			System.out.println("measureName : " + target);
        			if(target.equals("heart rate") || target.equals("steps")){
						currentMeasureValueInt = measureArr.getJSONObject(i).getInt("value");
						System.out.println("measure-value: " + currentMeasureValueInt);
					}else{
						currentMeasureValueDouble = measureArr.getJSONObject(i).getDouble("value");
						System.out.println("measure-value: " + currentMeasureValueDouble);
					}
        		}
        	}
        	
        	//GETTING GOAL LIST FOR A SPECIFIED PERSON
        	//goals json obj - goal json array
        	JSONArray goalArr = (JSONArray) obj.getJSONObject("goals").getJSONArray("goal");
        	for(int i = 0; i < goalArr.length(); i++){
        		if((goalArr.getJSONObject(i).getString("type")).equals(measureName)){
        			String target = goalArr.getJSONObject(i).getString("type");
        			System.out.println("goalType : " + target);
        			if(target.equals("heart rate") || target.equals("steps")){
						goalValueInt = goalArr.getJSONObject(i).getInt("value");
						System.out.println("goal-value: " + goalValueInt);
					}else{
						goalValueDouble = goalArr.getJSONObject(i).getDouble("value");
						System.out.println("goal-value: " + goalValueDouble);
					}
        		}
        	}
        	
        	//COMPARISON
        	switch(measureName){
			case "heart rate":
				if(currentMeasureValueInt == -1 || goalValueInt == -1){
	        		return Response.status(404).build();
	        	}
	        	if(currentMeasureValueInt >= goalValueInt){
	        		comparison = "ok";
	        	}else{
	        		comparison = "ko";
	        	}
				break;
			case "steps":
				if(currentMeasureValueInt == -1 || goalValueInt == -1){
	        		return Response.status(404).build();
	        	}
	        	if(currentMeasureValueInt >= goalValueInt){
	        		comparison = "ok";
	        	}else{
	        		comparison = "ko";
	        	}
				break;
			default:
				if(currentMeasureValueDouble == -1. || goalValueDouble == -1.){
	        		return Response.status(404).build();
	        	}
	        	if(currentMeasureValueDouble >= goalValueDouble){
	        		comparison = "ok";
	        	}else{
	        		comparison = "ko";
	        	}
			}
			System.out.println("comparison: " + comparison);
        	
        	
        	xmlBuild = "<comparison-information>";
        		xmlBuild += "<measure>" + measureName + "</measure>";
        		if(measureName.equals("heart rate") || measureName.equals("steps")){
        			xmlBuild += "<currentMeasureValue>" + currentMeasureValueInt + "</currentMeasureValue>";
            		xmlBuild += "<goalValue>" + goalValueInt + "</goalValue>";
        		}else{
        			xmlBuild += "<currentMeasureValue>" + currentMeasureValueDouble + "</currentMeasureValue>";
            		xmlBuild += "<goalValue>" + goalValueDouble + "</goalValue>";
        		}
        		xmlBuild += "<result>" + comparison + "</result>";
        	xmlBuild += "</comparison-information>";
        	
        	System.out.println(prettyXMLPrint(xmlBuild));
        	
        	JSONObject xmlJSONObj = XML.toJSONObject(xmlBuild);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            
            return Response.ok(jsonPrettyPrintString).build();
            
		}catch(Exception e){
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