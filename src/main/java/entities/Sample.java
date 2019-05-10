package entities;

import java.sql.Timestamp;
import java.util.Date;

public class Sample {
    private String serverDatetime;
    private String clientDatetime;
    private Boolean confirmed;
    private String terminalName;

    public Sample(String serverDatetime, String clientDatetime, Boolean confirmed, String terminalName) {
        this.serverDatetime = serverDatetime;
        this.clientDatetime = clientDatetime;
        this.confirmed = confirmed;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }
}
