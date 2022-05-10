module Workshop_JavaFX_JDBC {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.graphics, javafx.fxml;
	opens model.entities to javafx.graphics, javafx.fxml, javafx.base;
	opens model.dao to javafx.graphics, javafx.fxml, javafx.base;
	opens model.dao.imlp to javafx.graphics, javafx.fxml, javafx.base;
	opens model.services to javafx.graphics, javafx.fxml;
	opens db to javafx.graphics, javafx.fxml, javafx.base;
	opens gui.util to javafx.graphics, javafx.fxml, javafx.base;
}
