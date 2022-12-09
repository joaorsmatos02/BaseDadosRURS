package consultarResidentes;

import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe controller do conteudo fxml da janela de query unica. Usada para
 * atribuir funcoes aos botoes e gerar tamanhos com base na resolucao do ecra
 * usado. Efeitos adicionais sao aplicados em CSS.
 * 
 * @author Joao Matos
 *
 */
public class JanelaQueryController {

	/**
	 * Handler do caso de uso
	 */
	private ConsultaResidentesHandler crHandler;

	/**
	 * Controller do ecra superior
	 */
	private EcraConsultaController consultaController;

	/**
	 * Largura do ecra
	 */
	private double width;

	@FXML
	private TextArea queryArea;
	@FXML
	private Button okQueryButton;

	/**
	 * Prepara os atributos necessarios e executa as funcoes de preparacao de cada
	 * elemento da pagina
	 * 
	 * @param consultaController - controller do ecra superior
	 * @param crHandler          - handler do caso de uso atual
	 */
	public void setUp(EcraConsultaController consultaController, ConsultaResidentesHandler crHandler) {
		this.consultaController = consultaController;
		this.crHandler = crHandler;

		width = Screen.getPrimary().getBounds().getWidth();

		setUpOkQueryButton();
	}

	private void setUpOkQueryButton() {
		StackPane.setAlignment(okQueryButton, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(okQueryButton, new Insets(width / 50));
	}

	// Event Listener on Button[#okQueryButton].onAction
	@FXML
	public void okQueryButtonHandler(ActionEvent event) {
		try {
			ResultSet rs = crHandler.enterQuery(queryArea.getText());
			queryArea.clear();
			if (rs != null)
				consultaController.printTable(rs);
		} catch (IllegalArgumentException i) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			Stage s = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			s.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
			errorAlert.getDialogPane().getStylesheets().add("JavaFX.css");
			errorAlert.setTitle("Erro");
			errorAlert.setHeaderText(
					"Não tem permissão para executar essa query. No modo residente apenas podem ser executadas queries do tipo SELECT.");
			errorAlert.show();
		}
	}
}
