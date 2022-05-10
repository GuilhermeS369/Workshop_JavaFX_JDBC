package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj ,"/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm(Department obj,String absoluteName, Stage parentStage) {
		try {
			// CRIA A TELA
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			//PEGA O CONTROLADOR E PASSA ESSA TELA COMO CONTROLADORA
			DepartmentFormController controller = loader.getController();
			//INSTANCIAMOS DEPARTAMENTO COM O CONTROLLER FORNECIDO
			controller.setDepartment(obj);
			// PEGAMOS AS INFORMAÇÕES E SUBIMOS NA TELA
			controller.updateFormData();
			
			// INSTANCIAMOS UM NOVO STAGE PARA POR UM NA FRENTE DO OUTRO:
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department ata");
			// CRIAMOS UMA NOVA CENA DIZENDO O ELEMENTO RAIZ DELA COMO PANE
			dialogStage.setScene(new Scene(pane));
			// PROIBIDO REDIMENSIOANR TELA:
			dialogStage.setResizable(false);
			// PAI DESSA JANELA
			dialogStage.initOwner(parentStage);
			// METODO Q DIZ SE É MODAL OU NAO(EM QNT N FECHAR, N PODE ACESSAR A JANELA ATRAS)
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// CHAMA A TELA
			dialogStage.showAndWait();
			
			
						
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	
	}
}