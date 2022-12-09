package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe controller do conteudo fxml do ecra inicial. Usada para atribuir
 * funcoes aos botoes e gerar tamanhos com base na resolucao do ecra usado.
 * Efeitos adicionais sao aplicados em CSS.
 * 
 * @author Joao Matos
 *
 */
public class EcraInicialController {

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
	 * Palco principal
	 */
	private Stage primaryStage;

	@FXML
	private StackPane introStackpane;

	@FXML
	private Label title;

	@FXML
	private ImageView ribeiroSantos;

	@FXML
	private GridPane logo;

	@FXML
	private Button integratedAuth;

	@FXML
	private Button sqlAuth;

	@FXML
	private GridPane verticalGrid;

	@FXML
	private Button guestAuth;

	@FXML
	private GridPane mainGrid;

	/**
	 * Prepara os atributos necessarios e executa as funcoes de preparacao de cada
	 * elemento da pagina
	 * 
	 * @param primaryStage - o palco principal
	 */
	public void setUp(LoginHandler loginHandler, Stage primaryStage) {
		this.loginHandler = loginHandler;
		this.primaryStage = primaryStage;

		alert = new Alert(AlertType.ERROR);
		Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
		s.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
		alert.getDialogPane().getStylesheets().add("JavaFX.css");
		alert.setTitle("Erro");
		alert.setHeaderText("Não foi possível ligar à base de dados.");

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpRibeiroSantos();
		setUpLogo();
		setUpVerticalGrid();
		setUpIntegratedAuth();
		setUpSQLAuth();
		setUpGuestAuth();
		setUpMainGrid();
	}

	public void setUpRibeiroSantos() {
		ribeiroSantos.setFitHeight(height / 10);
		ribeiroSantos.setFitWidth(height / 10);
	}

	public void setUpLogo() {
		logo.setHgap(width * 0.015);
		logo.setPadding(new Insets(height * 0.07, 0, 0, width * 0.04));
	}

	public void setUpVerticalGrid() {
		verticalGrid.setVgap(height * 0.01);
	}

	public void setUpIntegratedAuth() {
		integratedAuth.setPrefSize(width / 4, height / 5);
	}

	public void setUpSQLAuth() {
		sqlAuth.setPrefSize(width / 4, height / 5);
	}

	public void setUpGuestAuth() {
		guestAuth.setPrefSize(width / 4, (height * 0.4) + (height * 0.01));
		// adiciona-se height para compensar o espaco entre os dois botoes a direita
	}

	public void setUpMainGrid() {
		mainGrid.setPadding(new Insets(height / 5, width * 0.15, height / 5, width * 0.15));
		mainGrid.setHgap(width * 0.005);
	}

	// Event Listener on Button.onAction
	@FXML
	public void guestHandler() {
		if (!loginHandler.handleGuestLogin()) {
			alert.show();
		} else {
			loginHandler.crHandler.launchGUI(primaryStage); // iniciar caso de uso unico
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void sqlHandler() {
		loginScreen(primaryStage, introStackpane);
	}

	// Event Listener on Button.onAction
	@FXML
	public void iaHandler() {
		if (!loginHandler.handleIntegratedLogin()) {
			alert.show();
		} else {
			loginHandler.crHandler.launchGUI(primaryStage); // iniciar caso de uso unico
		}
	}

	/**
	 * Altera a root da cena atual para uma nova root de login. Nao e criada uma
	 * nova cena de forma a permitir redimensionamentos da janela persistentes ao
	 * voltar atras
	 * 
	 * @param primaryStage - o palco principal
	 * @param parent       - o parent da cena atual
	 */
	private void loginScreen(Stage primaryStage, StackPane parent) {
		try {
			// carregar stackpane e colocar como root
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main/EcraLogin.fxml"));
			StackPane sqlLogin = loader.load();
			EcraLoginController controller = loader.<EcraLoginController>getController();
			controller.setUp(loginHandler, parent, alert);
			primaryStage.getScene().setRoot(sqlLogin);
		} catch (Exception e) {
			Alert loginAlert = new Alert(AlertType.ERROR);
			Stage s = (Stage) loginAlert.getDialogPane().getScene().getWindow();
			s.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
			loginAlert.getDialogPane().getStylesheets().add("JavaFX.css");
			loginAlert.setTitle("Erro");
			loginAlert.setHeaderText("Não foi possível inicializar a interface.");
		}
	}
}
