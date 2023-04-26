module com.winway.onlinechat {
    requires javafx.graphics;
    requires javafx.controls;
    requires java.base;
    requires java.compiler;
    requires javafx.fxml;
    
    opens com.winway.onlinechat.server to java.base;
    exports com.winway.onlinechat.server to java.base;
    opens com.winway.onlinechat.common to javafx.fxml, javafx.controls, javafx.graphics, java.base;
    exports com.winway.onlinechat.common to javafx.fxml, javafx.controls, javafx.graphics, java.base;
    exports com.winway.onlinechat.client to java.base, javafx.controls, javafx.fxml, javafx.graphics;
    opens com.winway.onlinechat.client to java.base, javafx.controls, javafx.fxml, javafx.graphics;
    exports com.winway.onlinechat.client.core to java.base, javafx.controls, javafx.fxml, javafx.graphics;
    opens com.winway.onlinechat.client.core to java.base, javafx.controls, javafx.fxml, javafx.graphics;
}