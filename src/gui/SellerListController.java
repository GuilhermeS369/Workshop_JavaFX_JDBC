package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML

	private TableColumn<Seller, Integer> tableColumnDepartmentId;
		
	@FXML // TABELA Q SERA USADA PARA ATUALIZAR OS DEPARTAMENTOS
	private TableColumn <Seller, Seller> tableColumnEDIT;
	
	@FXML // TABELA QUE SERA UUSADA PARA DELETAR OS DEPARTAMENTOS
	private TableColumn <Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj ,"/gui/SellerForm.fxml", parentStage);
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// AQUI SETAMOS OQ CADA COLUNA VAI RECEBER
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		tableColumnDepartmentId.setCellValueFactory(new PropertyValueFactory<>("department"));
		//METODO QUE FORMATA AS TABLESVIEWS DATA E BASE SALARY
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		// AQUI DEIXA A TABLE VIEW RESPONSIVA
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
					
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		// INICIA OS BOTOES DE EDITAR
		initEditButtons();
		initRemoveButtons();
	}
	// RESPONSTAVEL POR CRIAR A NOVA JANELA DE FORMULARIO
	private void createDialogForm(Seller obj,String absoluteName, Stage parentStage) {
		try {
			// CRIA A TELA
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			//PEGA O CONTROLADOR E PASSA ESSA TELA COMO CONTROLADORA
			SellerFormController controller = loader.getController();
			//INSTANCIAMOS DEPARTAMENTO PARA O CONTROLLER FORNECIDO
			controller.setSeller(obj);
			// INSTANCIAMOS TAMBEM OS SERVIÇOS PARA O CONTROLLER FORNECIDO
			controller.setServices(new SellerService(), new DepartmentService());
			// METODO RESPONSAVEL POR SUBIR A LISTA DE DEPARTAMENTOS
			controller.loadAssociatedObjects();
			//INSCREVER ESSE DIALOG FORM PARA RECEBER O UPDATE NA HORA Q ALGO MUDAR ONDATACHANGED
			controller.subscribeDataChangeListener(this);
			// PEGAMOS AS INFORMAÇÕES E SUBIMOS NA TELA
			controller.updateFormData();
			
			
			
			// INSTANCIAMOS UM NOVO STAGE PARA POR UM NA FRENTE DO OUTRO:
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller Data");
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
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	
	}

	@Override
	public void onDataChanged() {
		updateTableView();
		
	}
	
	private void initEditButtons() {
		// setCellValueFactory VAI SER RESPOSNAVEL POR INSTANCIAR OS BOTOES E CONFIGURAR
		// OS EVENTOS
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			// O BOTAO EDIT
			private final Button button = new Button("edit");

			@Override //OBJETO QUE SOBE OQ TEM NA LINHA EM Q O BOTAO ESTÁ.
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				} // CONFIGURAÇÃO DO EVENTO
				setGraphic(button);
				button.setOnAction(
				event -> createDialogForm( // AQUI PASSAMOS O OBJ Q É O DEPARTAMENTO DA LINHA EM QUE CLICARMOS
						// E CHAMA O FORMULARIO DE EDIÇÃO
						obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() { 
		// SELECIONA O VALOR QUE IRA NA TABELA
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		// ESCOLHE QUE INFORMAÇÃO IRA NA TABLEA
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller> (){
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				// SE NAO TEM OBJETO NA LINHA ENTAO RETORNA TUDO NULO
				if (obj == null) {
					setGraphic(null);
					return;
				}
				// SETA UM BUTAO QUE VAI FAZER O EVENTO ABAIXO
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
						
		});
		
	}

	private void  removeEntity(Seller obj) {
	// METODO DE REMOVER, PRIMEIRO CHAMAMOS O ALERT:
	// SERA UM BOTAO CLICADO	
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		// FAREMOS UM TESTE QUE OBTEM O RESULTADO DE DENTRO DO METODO COM GET
		// SE FOI OK, ARMAZONEA BUTTONTYPE.OK
		if (result.get() == ButtonType.OK) {
			// PRECISAMOS FAZER UNS TESTES PRINCIPALMENTE DA INSTANCIA:
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
			// COMANDO DE REMOVER
			service.remove(obj);
			// FORÇA ATUALIZAÇÃO DA TABELA
			updateTableView();
			}		
			// ESSA DB INTEGRITY EXCEPTION FOI TIRADA DO departmentDaoJDBC, Q CASO HAJA UM ERRO
			// UM ERRO, ELE LANÇA ESSE TIPO DE EXCEÇÃO
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
					
					
		}
		
	}

}