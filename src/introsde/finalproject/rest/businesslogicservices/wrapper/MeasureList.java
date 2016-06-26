package introsde.finalproject.rest.businesslogicservices.wrapper;

import introsde.finalproject.rest.businesslogicservices.model.Measure;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "measure-profile")
public class MeasureList {

	@XmlElement(name = "measure")
	@JsonProperty("measure")
	public List<Measure> measureList = new ArrayList<Measure>();

	public List<Measure> getMeasureList() {
		return measureList;
	}

	public void setMeasureList(List<Measure> measureList) {
		this.measureList = measureList;
	}

}
