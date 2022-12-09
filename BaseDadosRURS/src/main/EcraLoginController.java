package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe controller do conteudo fxml do ecra de login. Usada para atribui
 * funcoes aos botoes e gerar tamanhos com base na resolucao do ecra usado.
 * Efeitos adicionais sao aplicados em CSS.
 * 
 * @author Joao Matos
 *
 */
public class EcraLoginController {

	/**
	 * Handler de login
	 */
	private LoginHandler loginHandler;

	/**
	 * Altura do ecra
	 */
	private double height;

	/**
	 * Largura do ecra
	 */
	private double width;

	/**
	 * Alerta de erro ao ligar a bd
	 */
	private Alert alert;

	/**
	 * Root da cena antes de alterar, na eventualidade de querer regrassar
	 */
	private Parent parent;

	@FXML
	private StackPane loginStackpane;
	@FXML
	private GridPane logoLogin;
	@FXML
	private ImageView ribeiroSantos;
	@FXML
	private Label title;
	@FXML
	private VBox mainVbox;
	@FXML
	private Label usernameLabel;
	@FXML
	private TextField username;
	@FXML
	private Label passwordLabel;
	@FXML
	private PasswordField password;
	@FXML
	private Button login;
	@FXML
	private Button goBack;

	/**
	 * Prepara os atributos necessarios e executa as funcoes de preparacao de cada
	 * elemento da pagina
	 * 
	 * @param parent - root da cena antes de alterar, na eventualidade de querer
	 *               regrassar
	 */
	public void setUp(LoginHandler loginHandler, Parent parent, Alert alert) {
		this.loginHandler = loginHandler;
		this.parent = parent;
		this.alert = alert;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpLogo();
		setUpRibeiroSantos();
		setUpMainVbox();
		setUpUser();
		setUpPass();
		setUpLogin();
		setUpGoBack();
	}

	private void setUpLogo() {
		logoLogin.setHgap(width * 0.015);
		logoLogin.setPadding(new Insets(height * 0.07, 0, 0, width * 0.04));
	}

	private void setUpRibeiroSantos() {
		ribeiroSantos.setFitHeight(height / 10);
		ribeiroSantos.setFitWidth(height / 10);
	}

	private void setUpMainVbox() {
		mainVbox.setSpacing(height * 0.02);
	}

	private void setUpUser() {
		username.setMaxWidth(width * 0.2);
	}

	private void setUpPass() {
		password.setMaxWidth(width * 0.2);
	}

	private void setUpLogin() {
		login.setPrefSize(width / 15, height / 18);
	}

	private void setUpGoBack() {
		goBack.setPrefSize(width / 15, height / 18);
		StackPane.setAlignment(goBack, Pos.BOTTOM_LEFT);
		StackPane.setMargin(goBack, new Insets(0, 0, height * 0.04, width * 0.03));
	}

	// Event Listener on Button.onAction
	@FXML
	public void loginHandler(ActionEvent event) {
		if (!loginHandler.handleSQLLogin(username.getText(), password.getText())) {
			alert.show();
		} else {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			loginHandler.crHandler.launchGUI(stage); // iniciar caso de uso unico
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void goBackHandler(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(parent);
	}
}
