package consultarResidentes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe controller do conteudo fxml da janela de execucao de procedures. Usada
 * para atribuir funcoes aos botoes e gerar tamanhos com base na resolucao do
 * ecra usado. Efeitos adicionais sao aplicados em CSS.
 * 
 * @author Joao Matos
 *
 */
public class JanelaProcedureController {

	/**
	 * Handler do caso de uso
	 */
	private ConsultaResidentesHandler crHandler;

	/**
	 * Altura do ecra
	 */
	private double height;

	/**
	 * Largura do ecra
	 */
	private double width;

	/**
	 * Alerta para erros
	 */
	private Alert errorAlert;

	/**
	 * Alerta para retornos
	 */
	private Alert returnAlert;

	@FXML
	private StackPane procedureStackpane;
	@FXML
	private VBox procedureVBox;
	@FXML
	private ComboBox<String> procedureSelector;
	@FXML
	private Button okProcedure;

	/**
	 * Prepara os atributos necessarios e executa as funcoes de preparacao de cada
	 * elemento da pagina
	 * 
	 * @param consultaController - controller do ecra superior
	 * @param crHandler          - handler do caso de uso atual
	 */
	public void setUp(ConsultaResidentesHandler crHandler) {
		this.crHandler = crHandler;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		errorAlert = new Alert(AlertType.ERROR);
		Stage s = (Stage) errorAlert.getDialogPane().getScene().getWindow();
		s.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
		errorAlert.getDialogPane().getStylesheets().add("JavaFX.css");
		errorAlert.setTitle("Erro");

		returnAlert = new Alert(AlertType.CONFIRMATION);
		Stage s1 = (Stage) returnAlert.getDialogPane().getScene().getWindow();
		s1.getIcons().add(new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png")));
		returnAlert.getDialogPane().getStylesheets().add("JavaFX.css");
		returnAlert.setTitle("Retorno");

		setUpProcedureVbox();
		setUpProcedureSelector();
		setUpOkProcedure();
	}

	private void setUpProcedureVbox() {
		procedureVBox.setSpacing(height * 0.02);
	}

	private void setUpProcedureSelector() {
		procedureSelector.setPrefSize(width * 0.2, height / 25);
		procedureSelector.getItems().addAll(crHandler.getAllProcedures());

		// Listener
		procedureSelector.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> ov, String s1, String s2) {
				List<String> params = crHandler.getProcedureParams(s2);
				if (params != null) {
					Node combobox = procedureVBox.getChildren().get(0);
					Node button = procedureVBox.getChildren().get(procedureVBox.getChildren().size() - 1);
					procedureVBox.getChildren().clear();
					procedureVBox.getChildren().add(combobox);
					for (int i = 0; i < params.size(); i++) {
						HBox hb = new HBox();
						hb.setAlignment(Pos.CENTER);
						Label label = new Label(params.get(i));
						label.setPrefSize(width * 0.1, height / 25);
						TextField textfield = new TextField();
						if (params.get(i).startsWith("@ano")) // preencher ano letivo auto
							autoFillYear(textfield);
						else if (params.get(i).startsWith("@data"))
							textfield.setPromptText("DD/MM/AAAA");
						else if (params.get(i).startsWith("@quarto"))
							textfield.setPromptText("X.XXX");
						textfield.setPrefSize(width * 0.1, height / 25);
						hb.getChildren().addAll(label, textfield);
						procedureVBox.getChildren().add(hb);
					}
					procedureVBox.getChildren().add(button);
				}
			}

		});
	}

	/**
	 * Preenche o textfield dado com o ano letivo atual. Para efeitos de contagem,
	 * considera-se que o ano letivo se inicia em setembro e termina em agosto
	 * 
	 * @param tf - o textfield a preencher
	 * @return tf preenchido com o ano letivo atual
	 */
	private void autoFillYear(TextField tf) {
		LocalDate currentDate = LocalDate.now();
		if (currentDate.getMonth().getValue() < 9) { // antes de setembro
			String prevYear = String.valueOf(currentDate.getYear() - 1);
			String year = String.valueOf(Integer.parseInt(prevYear.substring(2)) + 1);
			tf.setText(prevYear + "/" + year);
		} else { // apos setembro, inclusive
			String year = String.valueOf(currentDate.getYear());
			String nextYear = String.valueOf(Integer.parseInt(year.substring(2)) + 1);
			tf.setText(year + "/" + nextYear);
		}
	}

	private void setUpOkProcedure() {
		okProcedure.setPrefSize(width / 30, height / 25);
	}

	// Event Listener on Button[#okProcedure].onAction
	@FXML
	public void okProcedure() {
		if (procedureSelector.getValue() != null && !procedureSelector.getValue().equals("Escolher Procedimento")) {
			List<Node> nodes = procedureVBox.getChildren();
			String[] params = new String[nodes.size() - 2];
			for (int i = 1; i < nodes.size() - 1; i++)
				if (nodes.get(i) instanceof HBox) {
					HBox box = (HBox) nodes.get(i);
					TextField tx = (TextField) box.getChildren().get(1);
					params[i - 1] = tx.getText();
					if (params[i - 1].equals("")) { // se o campo for deixado vazio
						Label l = (Label) box.getChildren().get(0);
						params[i - 1] = "null" + l.getText().substring(l.getText().indexOf(' '));
					}
					tx.clear();
				}
			try {
				ResultSet rs = crHandler.enterProcedure(procedureSelector.getValue(), params);
				procedureSelector.getSelectionModel().clearSelection();
				if (rs != null)
					try {
						rs.next();
						returnAlert.setHeaderText("O procedimento retornou: " + rs.getString(1) + ".");
						returnAlert.show();
						rs.close();
					} catch (SQLException e) {
						errorAlert.setHeaderText("Não foi possivel imprimir o retorno do procedimento.");
						errorAlert.show();
					}
			} catch (IllegalArgumentException i) {
				errorAlert.setHeaderText("Erro ao executar o procedimento. Tipo de argumentos nao suportado.");
				errorAlert.show();
			}
		} else {
			errorAlert.setHeaderText("Procedimento inválido.");
			errorAlert.show();
		}
	}
}
