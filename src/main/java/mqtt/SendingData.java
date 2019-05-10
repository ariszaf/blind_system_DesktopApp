package mqtt;


public class SendingData {
    private final int sub_option;

    public SendingData(int sub_option) {
        this.sub_option = sub_option;
    }

    public int getSub_option() {
        return sub_option;
    }

}
