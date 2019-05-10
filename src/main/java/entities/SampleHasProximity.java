package entities;

public class SampleHasProximity {

    private Float proximityX;
    private String sensorName;
    private Integer sampleId;

    public SampleHasProximity(Float proximityX, String sensorName, Integer sampleId) {
        this.proximityX = proximityX;
        this.sensorName = sensorName;
        this.sampleId = sampleId;
    }

    public Float getProximityX() {
        return proximityX;
    }

    public void setProximityX(Float proximityX) {
        this.proximityX = proximityX;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }
}
