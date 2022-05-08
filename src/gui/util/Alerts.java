package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
	//METODO RECEBE TITULO, CABEÇALHO, CONTEUDO E O TIPO ALERT ENUMERADO
	public static void showAlert(String title, String header, String content, AlertType type) {
		//INSTANCIAR UM TIPO ALERT ATRIBUINDO UM TIPO ALERTA
		Alert alert = new Alert(type);
		//DEFINIÇÕES DESSE ALERTA:
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
}