package entities;

public class SampleHasAccelerometer {

    private Float accelerationX;
    private Float accelerationY;
    private Float accelerationZ;
    private String sensorName;
    private Integer sampleId;

    public SampleHasAccelerometer(Float accelerationX, Float accelerationY, Float accelerationZ, String sensorName, Integer sampleId) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.sensorName = sensorName;
        this.sampleId = sampleId;
    }

    public Float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(Float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public Float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(Float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public Float getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(Float accelerationZ) {
        this.accelerationZ = accelerationZ;
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
