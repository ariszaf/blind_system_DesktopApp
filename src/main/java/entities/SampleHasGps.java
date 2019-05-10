package entities;

public class SampleHasGps {

    private Double longitude;
    private Double latitude;
    private String sensorName;
    private Integer sampleId;

    public SampleHasGps(Double longitude, Double latitude, String sensorName, Integer sampleId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.sensorName = sensorName;
        this.sampleId = sampleId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
