module com.winway.onlinechat {
    requires javafx.controls;
    requires javafx.fxml;
    
    
    opens com.winway.onlinechat to javafx.fxml;
    exports com.winway.onlinechat;
}