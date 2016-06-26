package introsde.finalproject.rest.businesslogicservices.model;

public class Measure {

	private int idMeasure;
	private String name;
	private String value;
	private String timestamp;

	// Constructor measure class
	public Measure(){}
	
	public Measure(int idMeasure, String name, String value, String timestamp) {
		this.idMeasure = idMeasure;
		this.name = name;
		this.value = value;
		this.timestamp = timestamp;	
	}

	public int getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
