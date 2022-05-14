package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
	// ALERTA DO TIPO CONFIRMAÇÃO PARA AVISO DE EXCLUSÃO
	public static Optional <ButtonType> showConfirmation (String title,  String content){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		return alert.showAndWait();
		
	}
	
}