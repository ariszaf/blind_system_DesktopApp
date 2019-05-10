package report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {
    private Integer sampleId;
    private String terminalName;
    private String serverDatetime;
    private String clientDatetime;
    private Double longitude;
    private Double latitude;
    private Float proximityX;
    private Float accelerationX;
    private Float accelerationY;
    private Float accelerationZ;
    private Boolean confirmed;

    public Report() {
    }

    public Report(Integer sampleId, String terminalName, String serverDatetime, String clientDatetime, Double longitude, Double latitude, Float proximityX, Float accelerationX, Float accelerationY, Float accelerationZ, Boolean confirmed) {
        this.sampleId = sampleId;
        this.terminalName = terminalName;
        this.serverDatetime = serverDatetime;
        this.clientDatetime = clientDatetime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.proximityX = proximityX;
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.confirmed = confirmed;
    }

    public Report(String terminalName, String serverDatetime, String clientDatetime, Double longitude, Double latitude, Float proximityX, Float accelerationX, Float accelerationY, Float accelerationZ, Boolean confirmed) {
        this.terminalName = terminalName;
        this.serverDatetime = serverDatetime;
        this.clientDatetime = clientDatetime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.proximityX = proximityX;
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.confirmed = confirmed;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getServerDatetime() {
        return serverDatetime;
    }

    public void setServerDatetime(String serverDatetime) {
        this.serverDatetime = serverDatetime;
    }

    public String getClientDatetime() {
        return clientDatetime;
    }

    public void setClientDatetime(String clientDatetime) {
        this.clientDatetime = clientDatetime;
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

    public Float getProximityX() {
        return proximityX;
    }

    public void setProximityX(Float proximityX) {
        this.proximityX = proximityX;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
