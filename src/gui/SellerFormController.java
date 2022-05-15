package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private DepartmentService departmentService;

	private SellerService service;
	// LISTA DOS OBJETOS Q VAO RECEBER O EVENTO
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	@FXML // LISTA Q SERA A PRESENTADA NA FORM
	private ObservableList<Department> obsList;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			// CHAMA O METODO QUE TRAS OS DADOS DO FORM PARA UM DEPARTMENT
			entity = getFormData();
			// TESTA PARA SUBIR PRO BANCO DE DADOS ESSE DEPARTMENT
			service.saveOrUpdate(entity);
			// COMANDA QUE NOTIFICA OS OBJETOS Q ESPERAM RECEBER O EVENTO
			notifyDataChangeListeners();
			// COMANDO QUE FECHA O A JANELA ATUAL
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			// ELE RETORNA A LISTA DE ERROS PARA O METODO QUE MUDA
			// A LABEL ONDE ESTA FICARA O ERRO
			setErrorMessages(e.getErrors());

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		// PERCORRE MINHA LISTA
		for (DataChangeListener listener : dataChangeListeners)
			// ALTERA CADA UMA DELAS
			listener.onDataChanged();

	}

	// PUXA OQ TEM NA CAIXA, SETA NO OBJ DEPARTMENT E RETORNA NO METODO
	private Seller getFormData() {

		Seller obj = new Seller();

		// INSTANCIAMOS A CLASSE RESPONSAVEL POR TRATAR OS ERROS
		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		// VALIDA SE A CAIXA É NULA OU VAZIA PARA GERAR A EXCEÇÃO
		// .TRIM TIRA OS ESPAÇOS DO Q VAI SER GET
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		// SE AL ISTA FOR MAIOR Q 0, LANÇA A EXCEÇÃO.
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializaNodes();

	}

	private void initializaNodes() { // DEFINIMOS OQ VAI EM CADA LINHA
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 100);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) { // SE FOR NULO, TRAZ ENTIDADE VAZIA
			throw new IllegalStateException("Entity was null");
		} // PEGA OS DADOS DO NOSSO OBJETO E JOGA NO NOSSO FORMULARIO
			// VALUEOF É CONVERSOR DE ID PARA STRING
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		// SE FOR DIFERENTE DE NULO, TENTAMOS A CONVERSÃO
		if (entity.getBirthDate() != null) {
			// DATEPICKER TRABALHA COM LOCAL DATE, E O GET NOS RETORNA UM JAVA.UTIL.DATE
			// ENTAO UTILIZAREMOS UM CONVERSOR, E USAREMOS A DATA ATUAL DO COMPUTADOR
			// COMO BASE, POIS HÁ O FUSO-HORARIO
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		/// SE O METODO FOR NULO, ENTAO FAREMOS O COMBOBOX SELECIONAR O PRIMEIRO ELEMENTO DA LSITA
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();		

		}
		// SE NAO, ELE SELECIONA O MESMO DO Q ESTA CADASTRADO NO BANCO DE DADOS
		else {
		comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		// CARREGAMOS OS DEPARTAMENTOS DA BASE DE DADOS
		List<Department> list = departmentService.findAll();
		// AGORA ATRIBUIREMOS NA OBSLIST OBSERVABLE
		obsList = FXCollections.observableArrayList(list);
		// SETAREMOS NA COMBO BOX
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		// FAÇO UMA LISTA COM O NOME DAS CHAVES
		Set<String> fields = errors.keySet();
		// USO CADA CHAVE TRAZENDO O ELEMENTO VINCULADO A ELA
		if (fields.contains("name")) {
			// EXIBE NA LABEL O ERRO DA CHAVE NAME
			labelErrorName.setText(errors.get("name"));
		}

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
