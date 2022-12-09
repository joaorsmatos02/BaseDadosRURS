package main;

import consultarResidentes.ConsultaResidentesHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Handler de login da aplicacao
 * 
 * @author Joao Matos
 *
 */
public class LoginHandler extends HandlerTemplate {

	/**
	 * Adaptador para a base de dados
	 */
	private BaseDadosRURSAdapter bdAdapter;

	/**
	 * Handler de consulta a base de dados RURS
	 */
	protected ConsultaResidentesHandler crHandler;

	/**
	 * Inicializa os objetos necessarios a execucao do programa
	 */
	public LoginHandler() {
		bdAdapter = BaseDadosRURSAdapter.getBaseDadosRURSAdapter();

		crHandler = new ConsultaResidentesHandler();
	}

	/**
	 * Handler do login como convidado
	 * 
	 * @return true se for bem sucedido, false caso contrario
	 */
	protected boolean handleGuestLogin() {
		bdAdapter.setGuest(true);
		bdAdapter.setAuthMode(false);
		if (!bdAdapter.setupConnection())
			return false;
		return true;
	}

	/**
	 * Handler do login atraves de autenticacao SQL
	 * 
	 * @return true se for bem sucedido, false caso contrario
	 */
	protected boolean handleSQLLogin(String user, String pass) {
		bdAdapter.setGuest(false);
		bdAdapter.setAuthMode(false);
		bdAdapter.setUsername(user);
		bdAdapter.setPassword(pass);
		if (!bdAdapter.setupConnection())
			return false;
		return true;
	}

	/**
	 * Handler do login integrado
	 * 
	 * @return true se for bem sucedido, false caso contrario
	 */
	protected boolean handleIntegratedLogin() {
		bdAdapter.setGuest(false);
		bdAdapter.setAuthMode(true);
		if (!bdAdapter.setupConnection())
			return false;
		return true;
	}

	@Override
	public void launchGUI(Stage primaryStage) {
		try {
			// titulo e logo da app
			primaryStage.setTitle("Base de Dados RURS");
			Image icon = new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png"));
			primaryStage.getIcons().add(icon);

			// ver tamanho do ecra
			double height = Screen.getPrimary().getBounds().getHeight();
			double width = Screen.getPrimary().getBounds().getWidth();

			// carregar stackpane e cena iniciais
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main/EcraInicial.fxml"));
			StackPane root = loader.load();
			EcraInicialController controller = loader.<EcraInicialController>getController();
			Scene scene = new Scene(root, width * (2.0 / 3), height * (2.0 / 3));
			scene.getStylesheets().add(("/JavaFX.css"));
			controller.setUp(this, primaryStage);

			// estabelecer tamanho minimo e carregar cena
			primaryStage.setMinHeight(height * (7.0 / 12));
			primaryStage.setMinWidth(width * (7.0 / 12));
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
			s.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
			alert.getDialogPane().getStylesheets().add("JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Não foi possível inicializar a interface.");
		}
	}
}
