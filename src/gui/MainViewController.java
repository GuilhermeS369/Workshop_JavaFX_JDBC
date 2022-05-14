package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) ->{
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) ->{
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x-> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

		try {
			// CRIA A TELA
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// ATRIBUI A TELA A UMA VARIAVEL VBOX
			VBox newVBox = loader.load();
			// CHAMA A CENA PRINCIPAL

			Scene mainScene = Main.getMainScene();
			// ATRIBUI O CONTEUDO VBOX DA CENA PRINCIPAL A UMA VARIAVEL
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// SALVAMOS O CONTEUDO EM UMA VARIAVEL, DPS LIMPAMOS O CONTEUDO:

			// CHAMA A VARIAVEL COM O CONTEUDO DA CENA PRINCIPAL E PUXA O FILHO NA POSIÇÃO 0
			// SENDO O PRIMEIRO
			// Q É O PRIMEIRO VBOX DA JANELA PRINCIAPL
			Node mainMenu = mainVBox.getChildren().get(0);
			// LIMPA TODO O CONTEUDO DOS FILHOS DA CENA PRINCIPAL
			mainVBox.getChildren().clear();

			// AGORA PRECIAMOS REUNIR ELES.
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			// RECEBE O TIPO T VINDO DO CONSUMER, NO CASO DEPARTMENTLIST CONTROLLER
			// VINDO DA FUNÇÃO onMenuDepartmentAction
			T controller = loader.getController();
			// AGORA CHAMAMOS A CLASSE Q INICIA ESSE COMANDO VINDO DO CONSUMER
			initializingAction.accept(controller);
			
			

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
