package util;

public class Index {
	Tuple<Double,Double> location;
	
	public Index(Double i1, Double i2){
		location = new Tuple<Double,Double>(i1, i2);
	}

	public Tuple<Double, Double> getLocation() {
		return location;
	}

	public void setLocation(Tuple<Double, Double> location) {
		this.location = location;
	}
	
	
}
