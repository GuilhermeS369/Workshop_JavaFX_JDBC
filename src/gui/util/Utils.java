package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils {
	// RETORNA O A TELA EM QUE O EVENTO EM QUESTAO  OCORRE
	public static Stage currentStage(ActionEvent event){
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
	// TENTA TRANSFORMAR EM INT
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e ) {
			return null;
		}
	}
	// METODO QUE TRATA A DATA PARA A COLUNA SER DO TIPO DATA, RECEBE QUALQUER TIPO TRATA E RETORNA UM DATE
	public static <T> void formatTableColumnDate (TableColumn <T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell <T, Date>(){
				private SimpleDateFormat sdf = new SimpleDateFormat(format);
				
				@Override
				protected void updateItem(Date item, boolean empty) {
					if (empty) {
						setText(null);
					}else {
						setText(sdf.format(item));
					}
					
				}
				
			};
			return cell;
		});
	}
	// METODO QUE TRATA OS VALORES PARA 2 CASAS DECIMAIS, RECEBE QUALQUER TIPO, TRATA E RETORA UM DOUBLE
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
	
	
}
