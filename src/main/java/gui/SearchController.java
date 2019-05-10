package gui;

import dao.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import report.Report;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SearchController implements Initializable {

    public AnchorPane tableRecord;
    @FXML
    private TextField adminClientDateTimeFrom, adminClientDateTimeTo, adminServerDateTimeFrom, adminServerDateTimeTo, adminTerminalNameField, adminGpsLongitudeFromField, adminGpsLatitudeFromField, adminGpsLongitudeToField, adminGpsLatitudeToField, adminProximityXField, adminAccelerationXField, adminAccelerationYField, adminAccelerationZField, adminIdRecordField;
    @FXML
    private Label exampleDatetimeFormatLabel, invalidFromServerDatetimeFormatLabel, invalidFromClientDatetimeFormatLabel, invalidToClientDatetimeFormatLabel, invalidToServerDatetimeFormatLabel, errorServerDatetimeFields, errorGpsLongitudeFields, errorClientDatetimeFields, errorGpsLatitudeFields, invalidSampleIdLabel, invalidFromLatitudeLabel, invalidFromLongitudeLabel, invalidToLongitudeLabel, invalidToLatitudeLabel, invalidProximityXLabel, invalidAccelerationXLabel, invalidAccelerationYLabel, invalidAccelerationZLabel, labelPage;
    @FXML
    private Button btnRefresh, nextPageBtn, previewsPageBtn, enterPageBtn, firstPageBtn, lastPageBtn;
    @FXML
    private ChoiceBox adminCheckBox;
    @FXML
    private ChoiceBox<Integer> pageViewer;
    @FXML
    private TableColumn<Report, String> columnServerDateTime, columnClientDateTime, columnLongitude, columnLatitude, columnProximityX, columnAccelerationX, columnAccelerationY, columnAccelerationZ, columnConfirmed, columnIdRecord, columnTerminalName;
    @FXML
    private TableView<Report> recordTable;
    private int currentPage, lastPage;
    private final int firstPage=1;
    private ObservableList<Report> firstPageData,data;
    private Integer sampleId;
    private String terminalName,fromServerDate,toServerDate,fromClientDate,toClientDate;
    private Double fromLongitude, toLongitude, fromLatitude, toLatitude;
    private Float proximity, accelerationX, accelerationY, accelerationZ;
    private Boolean confirmed;


    private ChangeListener<String> forcePositiveNumberListener = (observable, oldValue, newValue) -> {      //http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        if (!newValue.matches("\\d{0,10}$")) {
            ((StringProperty) observable).set(oldValue);
            //getToolkit().exit();
            Toolkit.getDefaultToolkit().beep();                                 //http://stackoverflow.com/questions/7460173/toolkit-not-beeping-on-ubuntu
        }
    };

    private ChangeListener<String> forcePosCommaNumberListener = (observable, oldValue, newValue) -> {
        if (!newValue.matches("^\\d*\\.?\\d* {0,10}")) {                   //\d*\.?\d* {0,10}
            ((StringProperty) observable).set(oldValue);
            //getToolkit().exit();
            Toolkit.getDefaultToolkit().beep();
        }
    };

    private ChangeListener<String> forceAllNumberListener = (observable, oldValue, newValue) -> {
        if (!newValue.matches("^[-]?\\d*\\.?\\d* {0,9}")) {
            ((StringProperty) observable).set(oldValue);
            //getToolkit().exit();
            Toolkit.getDefaultToolkit().beep();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminCheckBox.setItems(FXCollections.observableArrayList("All samples","Only confirmed","Not confirmed"));
        adminCheckBox.getSelectionModel().selectFirst();

        adminGpsLongitudeFromField.textProperty().addListener(forceAllNumberListener);
        adminGpsLongitudeToField.textProperty().addListener(forceAllNumberListener);
        adminGpsLatitudeFromField.textProperty().addListener(forceAllNumberListener);
        adminGpsLatitudeToField.textProperty().addListener(forceAllNumberListener);
        adminAccelerationXField.textProperty().addListener(forceAllNumberListener);
        adminAccelerationYField.textProperty().addListener(forceAllNumberListener);
        adminAccelerationZField.textProperty().addListener(forceAllNumberListener);
        adminIdRecordField.textProperty().addListener(forcePositiveNumberListener);
        adminProximityXField.textProperty().addListener(forcePosCommaNumberListener);

        columnIdRecord.setCellValueFactory(new PropertyValueFactory<>("sampleId"));
        columnTerminalName.setCellValueFactory(new PropertyValueFactory<>("terminalName"));
        columnServerDateTime.setCellValueFactory(new PropertyValueFactory<>("serverDatetime"));
        columnClientDateTime.setCellValueFactory(new PropertyValueFactory<>("clientDatetime"));
        columnLongitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        columnLatitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        columnProximityX.setCellValueFactory(new PropertyValueFactory<>("proximityX"));
        columnAccelerationX.setCellValueFactory(new PropertyValueFactory<>("accelerationX"));
        columnAccelerationY.setCellValueFactory(new PropertyValueFactory<>("accelerationY"));
        columnAccelerationZ.setCellValueFactory(new PropertyValueFactory<>("accelerationZ"));
        columnConfirmed.setCellValueFactory(new PropertyValueFactory<>("confirmed"));

        btnRefresh.setDisable(true);
        disableNextLastPageBtn(true);
        disablePreviewsFirstPageBtn(true);
        enterPageBtn.setDisable(true);
        pageViewer.setDisable(true);
        labelPage.setDisable(true);
        data = FXCollections.observableArrayList();


    }

    public void loadFromData() {
        clearErrorMessages();

        if(validationControl()!=0) {
            if(!btnRefresh.isDisabled())
                btnRefresh.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You have "+ validationControl() +" error(s)!");
            alert.setContentText("Error!!! Please complete right the textFields.");
            alert.showAndWait();

            return;
        }

        getDataFromDatabase();

        putDataToFirstPage();

        setPagination();

        setColoredRow();
    }

    private void setColoredRow() {                                              //http://stackoverflow.com/questions/32119277/colouring-table-row-in-javafx
        recordTable.setRowFactory(conf -> new TableRow<Report>() {
            @Override
            public void updateItem(Report item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getConfirmed()) {
                    setStyle("-fx-background-color: #84C4FF;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private int validationControl(){
        int errorCounter = 0;

        sampleId = null;
        if (!adminIdRecordField.getText().isEmpty()) {                                                       //http://stackoverflow.com/questions/32866937/how-to-check-if-textfield-is-empty
            try {
                sampleId = Integer.parseInt(adminIdRecordField.getText());
            } catch (NumberFormatException e) {
                invalidSampleIdLabel.setVisible(true);
                errorCounter++;
            }
        }

        terminalName = null;
        if (!adminTerminalNameField.getText().isEmpty())
            terminalName = adminTerminalNameField.getText();

        Pattern datetimePattern = Pattern.compile("(20|19)\\d{2}-(01|02|03|04|05|06|07|08|09|11|12)-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1,6}");

//
//        fromServerDate = null;
//        if (!adminServerDateTimeFrom.getText().isEmpty()){
//            fromServerDate = adminServerDateTimeFrom.getText();
//            if(!datetimePattern.matcher(fromServerDate).matches()){
//                invalidFromServerDatetimeFormatLabel.setVisible(true);
//                errorCounter++;
//            }
//        }
//
//        toServerDate = null;
//        if (!adminServerDateTimeTo.getText().isEmpty()){
//            toServerDate = adminServerDateTimeTo.getText();
////            if(!datetimePattern.matcher(toServerDate).matches()){
////                invalidToServerDatetimeFormatLabel.setVisible(true);
////                errorCounter++;
////            }
//        }
//
//        fromClientDate = null;
//        if (!adminClientDateTimeFrom.getText().isEmpty()){
//            fromClientDate = adminClientDateTimeFrom.getText();
//            if(!datetimePattern.matcher(fromClientDate).matches()){
//                invalidFromClientDatetimeFormatLabel.setVisible(true);
//                errorCounter++;
//            }
//        }
//
//        toClientDate = null;
//        if (!adminClientDateTimeTo.getText().isEmpty()){
//            toClientDate = adminClientDateTimeTo.getText();{
//                if(!datetimePattern.matcher(toClientDate).matches()){
//                    invalidToClientDatetimeFormatLabel.setVisible(true);
//                    errorCounter++;
//                }
//            }
//        }






        fromServerDate = null;
        if (!adminServerDateTimeFrom.getText().isEmpty()){
            fromServerDate = adminServerDateTimeFrom.getText();
            if(!datetimePattern.matcher(fromServerDate).matches()){
                invalidFromServerDatetimeFormatLabel.setVisible(true);
                errorCounter++;
            }
        }

        toServerDate = null;
        if (!adminServerDateTimeTo.getText().isEmpty()){
            toServerDate = adminServerDateTimeTo.getText();
            if(!datetimePattern.matcher(toServerDate).matches()){
                invalidToServerDatetimeFormatLabel.setVisible(true);
                errorCounter++;
            }
        }

        fromClientDate = null;
        if (!adminClientDateTimeFrom.getText().isEmpty()){
            fromClientDate = adminClientDateTimeFrom.getText();
            if(!datetimePattern.matcher(fromClientDate).matches()){
                invalidFromClientDatetimeFormatLabel.setVisible(true);
                errorCounter++;
            }
        }

        toClientDate = null;
        if (!adminClientDateTimeTo.getText().isEmpty()){
            toClientDate = adminClientDateTimeTo.getText();{
                if(!datetimePattern.matcher(toClientDate).matches()){
                    invalidToClientDatetimeFormatLabel.setVisible(true);
                    errorCounter++;
                }
            }
        }





        fromLongitude = null;
        if (!adminGpsLongitudeFromField.getText().isEmpty()) {
            try {
                fromLongitude = Double.parseDouble(adminGpsLongitudeFromField.getText());
            } catch (NumberFormatException e) {
                invalidFromLongitudeLabel.setVisible(true);
                errorCounter++;
            }
        }

        toLongitude = null;
        if (!adminGpsLongitudeToField.getText().isEmpty()) {
            try {
                toLongitude = Double.parseDouble(adminGpsLongitudeToField.getText());
            } catch (NumberFormatException e) {
                invalidToLongitudeLabel.setVisible(true);
                errorCounter++;
            }
        }

        fromLatitude = null;
        if (!adminGpsLatitudeFromField.getText().isEmpty()) {
            try {
                fromLatitude = Double.parseDouble(adminGpsLatitudeFromField.getText());
            } catch (NumberFormatException e) {
                invalidFromLatitudeLabel.setVisible(true);
                errorCounter++;
            }
        }

        toLatitude = null;
        if (!adminGpsLatitudeToField.getText().isEmpty()) {
            try {
                toLatitude = Double.parseDouble(adminGpsLatitudeToField.getText());
            } catch (NumberFormatException e) {
                invalidToLatitudeLabel.setVisible(true);
                errorCounter++;
            }
        }


        proximity = null;
        if (!adminProximityXField.getText().isEmpty()) {
            try {
                proximity = Float.parseFloat(adminProximityXField.getText());
            } catch (NumberFormatException e) {
                invalidProximityXLabel.setVisible(true);
                errorCounter++;
            }
        }

        accelerationX = null;
        if (!adminAccelerationXField.getText().isEmpty()) {
            try {
                accelerationX = Float.parseFloat(adminAccelerationXField.getText());
            } catch (NumberFormatException e) {
                invalidAccelerationXLabel.setVisible(true);
                errorCounter++;
            }
        }

        accelerationY = null;
        if (!adminAccelerationYField.getText().isEmpty()){
            try {
                accelerationY = Float.parseFloat(adminAccelerationYField.getText());
            } catch (NumberFormatException e) {
                invalidAccelerationYLabel.setVisible(true);
                errorCounter++;
            }
        }

        accelerationZ=null;
        if(!adminAccelerationZField.getText().isEmpty()) {
            try {
                accelerationZ = Float.parseFloat(adminAccelerationZField.getText());
            }catch (NumberFormatException e){
                invalidAccelerationZLabel.setVisible(true);
                errorCounter++;
            }
        }

        String checkBox = String.valueOf(adminCheckBox.getValue());
        confirmed=null;
        if(checkBox.equals("Only confirmed"))
            confirmed=true;
        else if(checkBox.equals("Not confirmed"))
            confirmed=false;

        if((adminServerDateTimeFrom.getText().isEmpty() && !adminServerDateTimeTo.getText().isEmpty()) || (!adminServerDateTimeFrom.getText().isEmpty() && adminServerDateTimeTo.getText().isEmpty())){
            errorServerDatetimeFields.setVisible(true);
            errorCounter++;
        }

        if((adminClientDateTimeFrom.getText().isEmpty() && !adminClientDateTimeTo.getText().isEmpty()) || (!adminClientDateTimeFrom.getText().isEmpty() && adminClientDateTimeTo.getText().isEmpty())){
            errorClientDatetimeFields.setVisible(true);
            errorCounter++;
        }

        if((adminGpsLatitudeFromField.getText().isEmpty() && !adminGpsLatitudeToField.getText().isEmpty()) || (!adminGpsLatitudeFromField.getText().isEmpty() && adminGpsLatitudeToField.getText().isEmpty())){
            errorGpsLatitudeFields.setVisible(true);
            errorCounter++;
        }

        if((adminGpsLongitudeFromField.getText().isEmpty() && !adminGpsLongitudeToField.getText().isEmpty()) || (!adminGpsLongitudeFromField.getText().isEmpty() && adminGpsLongitudeToField.getText().isEmpty())){
            errorGpsLongitudeFields.setVisible(true);
            errorCounter++;
        }

        return errorCounter;
    }

    private void getDataFromDatabase(){
        ReportDAO reportDAO = new ReportDAO();
        data = reportDAO.getFilteringReports(sampleId, terminalName, fromServerDate, toServerDate, fromClientDate, toClientDate, fromLongitude, toLongitude, fromLatitude, toLatitude, proximity, accelerationX, accelerationY, accelerationZ, confirmed);
    }

    private void setPagination(){

        ObservableList<Integer> pages=FXCollections.observableArrayList();
        disablePreviewsFirstPageBtn(true);

        if(data.size()>10){
            for(int i=1;i<=data.size()/10;i++){
                pages.add(i);
            }
            if((data.size() % 10)!=0){
                lastPage = (data.size()/10)+ 1;
                pages.add(lastPage);
            }
            else{
                lastPage=data.size()/10;
            }
            setGoToPagePagination(false);
            disableNextLastPageBtn(false);
        }
        else if(data.size()<=10 && data.size()!=0){
            lastPage = 1;
            disableNextLastPageBtn(true);
            pages.add(1);
        }
        else if(data.size()==0){
            setGoToPagePagination(true);
            disableNextLastPageBtn(true);
            btnRefresh.setDisable(true);
        }
        if (data.size()!=0){
            if(btnRefresh.isDisabled())
                btnRefresh.setDisable(false);
        }
        pageViewer.setItems(pages);
        setPageViewer(currentPage);
    }

    public void loadPreviewsPage() {
        currentPage--;
        disableNextLastPageBtn(false);

        if(currentPage==firstPage){
            loadFirstPage();
        }
        else {
            getPageData(currentPage);
        }
    }

    public void loadNextPage() {
        disablePreviewsFirstPageBtn(false);

        currentPage++;
        if(currentPage==lastPage){
            loadLastPage();
        }
        else{
            getPageData(currentPage);
        }
    }

    public void loadPageFromChoiceBox() {
        int oldPage = currentPage;
        currentPage=pageViewer.getValue();

        if(currentPage==firstPage){
            loadFirstPage();
        }
        else if(currentPage==lastPage){
            loadLastPage();
        }
        else {
            getPageData(currentPage);
            if(oldPage<currentPage)
                disablePreviewsFirstPageBtn(false);
            else
                disableNextLastPageBtn(false);
        }
    }

    public void loadLastPage() {
        disablePreviewsFirstPageBtn(false);
        disableNextLastPageBtn(true);
        currentPage=lastPage;
        getPageData(currentPage);
    }

    private void putDataToFirstPage(){
        firstPageData = FXCollections.observableArrayList();

        currentPage=1;
        for(int i=currentPage*10-10; i<(currentPage*10)&&i<data.size(); i++){
            firstPageData.add(data.get(i));
        }
        recordTable.setItems(firstPageData);
    }

    public void loadFirstPage() {
        currentPage=firstPage;
        disablePreviewsFirstPageBtn(true);

        if(data.size()<=10){
            disableNextLastPageBtn(true);
        }
        else{
            disableNextLastPageBtn(false);
        }
        recordTable.setItems(firstPageData);
        setPageViewer(currentPage);
    }

    private void getPageData(int currentPage){
        ObservableList<Report> pageData = FXCollections.observableArrayList();
        for(int i=currentPage*10-10; i<(currentPage*10)&&i<data.size(); i++){
            pageData.add(data.get(i));
        }
        recordTable.setItems(pageData);
        setPageViewer(currentPage);
    }

    private void disableNextLastPageBtn(Boolean nextLastPageSwitcher){
        lastPageBtn.setDisable(nextLastPageSwitcher);
        nextPageBtn.setDisable(nextLastPageSwitcher);
    }

    private void disablePreviewsFirstPageBtn(Boolean previewsFirstPageSwitcher){
        previewsPageBtn.setDisable(previewsFirstPageSwitcher);
        firstPageBtn.setDisable(previewsFirstPageSwitcher);
    }

    private void setPageViewer(int pageNum){
        pageViewer.setValue(pageNum-1);
        pageViewer.getSelectionModel().select(pageNum-1);
    }

    private void setGoToPagePagination(Boolean switcher){
        enterPageBtn.setDisable(switcher);
        labelPage.setDisable(switcher);
        pageViewer.setDisable(switcher);
    }

    public void refreshData() {
        clearErrorMessages();

        getDataFromDatabase();

        putDataToFirstPage();

        setPagination();

        setColoredRow();
    }

    public void clearTextFieldsValues() {
        if(!adminGpsLatitudeFromField.getText().isEmpty())
            adminGpsLatitudeFromField.clear();
        if(!adminIdRecordField.getText().isEmpty())
            adminIdRecordField.clear();
        if(!adminTerminalNameField.getText().isEmpty())
            adminTerminalNameField.clear();
        if(!adminClientDateTimeFrom.getText().isEmpty())
            adminClientDateTimeFrom.clear();
        if(!adminClientDateTimeTo.getText().isEmpty())
            adminClientDateTimeTo.clear();
        if(!adminServerDateTimeFrom.getText().isEmpty())
            adminServerDateTimeFrom.clear();
        if(!adminServerDateTimeTo.getText().isEmpty())
            adminServerDateTimeTo.clear();
        if(!adminGpsLongitudeFromField.getText().isEmpty())
            adminGpsLongitudeFromField.clear();
        if(!adminGpsLongitudeToField.getText().isEmpty())
            adminGpsLongitudeToField.clear();
        if(!adminGpsLatitudeFromField.getText().isEmpty())
            adminGpsLatitudeFromField.clear();
        if(!adminGpsLatitudeToField.getText().isEmpty())
            adminGpsLatitudeToField.clear();
        if(!adminProximityXField.getText().isEmpty())
            adminProximityXField.clear();
        if(!adminAccelerationXField.getText().isEmpty())
            adminAccelerationXField.clear();
        if(!adminAccelerationYField.getText().isEmpty())
            adminAccelerationYField.clear();
        if(!adminAccelerationZField.getText().isEmpty())
            adminAccelerationZField.clear();
        if(!adminCheckBox.getValue().equals("All samples"))
            adminCheckBox.getSelectionModel().selectFirst();
        clearErrorMessages();
    }

    private void clearErrorMessages(){
        if(invalidFromServerDatetimeFormatLabel.isVisible())
            invalidFromServerDatetimeFormatLabel.setVisible(false);

        if(invalidFromServerDatetimeFormatLabel.isVisible())
            invalidFromServerDatetimeFormatLabel.setVisible(false);

        if(invalidFromClientDatetimeFormatLabel.isVisible())
            invalidFromClientDatetimeFormatLabel.setVisible(false);

        if(invalidToClientDatetimeFormatLabel.isVisible())
            invalidToClientDatetimeFormatLabel.setVisible(false);

        if(invalidToServerDatetimeFormatLabel.isVisible())
            invalidToServerDatetimeFormatLabel.setVisible(false);

        if(errorServerDatetimeFields.isVisible())
            errorServerDatetimeFields.setVisible(false);

        if(errorGpsLongitudeFields.isVisible())
            errorGpsLongitudeFields.setVisible(false);

        if(errorClientDatetimeFields.isVisible())
            errorClientDatetimeFields.setVisible(false);

        if(errorClientDatetimeFields.isVisible())
            errorClientDatetimeFields.setVisible(false);

        if(errorGpsLatitudeFields.isVisible())
            errorGpsLatitudeFields.setVisible(false);

        if(invalidSampleIdLabel.isVisible())
            invalidSampleIdLabel.setVisible(false);

        if(invalidFromLatitudeLabel.isVisible())
            invalidFromLatitudeLabel.setVisible(false);

        if(invalidFromLongitudeLabel.isVisible())
            invalidFromLongitudeLabel.setVisible(false);

        if(invalidToLongitudeLabel.isVisible())
            invalidToLongitudeLabel.setVisible(false);

        if(invalidToLatitudeLabel.isVisible())
            invalidToLatitudeLabel.setVisible(false);

        if(invalidProximityXLabel.isVisible())
            invalidProximityXLabel.setVisible(false);

        if(invalidAccelerationXLabel.isVisible())
            invalidAccelerationXLabel.setVisible(false);

        if(invalidAccelerationYLabel.isVisible())
            invalidAccelerationYLabel.setVisible(false);

        if(invalidAccelerationZLabel.isVisible())
            invalidAccelerationZLabel.setVisible(false);
    }

    public void showDateTimeFormat() {
        if(!exampleDatetimeFormatLabel.isVisible())
            exampleDatetimeFormatLabel.setVisible(true);
    }

    public void hideDateTimeFormat() {
        if(exampleDatetimeFormatLabel.isVisible())
            exampleDatetimeFormatLabel.setVisible(false);
    }
}
