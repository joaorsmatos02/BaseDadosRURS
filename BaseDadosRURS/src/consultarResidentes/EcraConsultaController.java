package consultarResidentes;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;

/**
 * Classe controller do conteudo fxml do ecra do caso de uso de consulta da base
 * de dados. Usada para atribuir funcoes aos botoes e gerar tamanhos com base na
 * resolucao do ecra usado. Efeitos adicionais sao aplicados em CSS.
 * 
 * @author Joao Matos
 *
 */
public class EcraConsultaController {

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

	@FXML
	private StackPane consultaStackpane;
	@FXML
	private GridPane logoConsulta;
	@FXML
	private HBox viewHbox;
	@FXML
	private HBox customQueryHbox;
	@FXML
	private ImageView ribeiroSantos;
	@FXML
	private Label title;
	@FXML
	private HBox buttonsHbox;
	@FXML
	private ComboBox<String> viewSelector;
	@FXML
	private Button okButton;
	@FXML
	private Button procedureButton;
	@FXML
	private Button queryButton;
	@FXML
	private TableView<TableRow> table;

	/**
	 * Prepara os atributos necessarios e executa as funcoes de preparacao de cada
	 * elemento da pagina
	 * 
	 * @param crHandler - handler do caso de uso atual
	 */
	public void setUp(ConsultaResidentesHandler crHandler) { // passar this
		this.crHandler = crHandler;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		if (crHandler.isGuest()) // se for guest remover botao de procedimentos
			customQueryHbox.getChildren().remove(0);

		setUpLogoConsulta();
		setUpRibeiroSantos();
		setUpButtonsHbox();
		setUpViewHbox();
		setUpViewSelector();
		setUpOkButton();
		setUpCustomQueryHbox();
		setUpProcedureButton();
		setUpQueryButton();
		setUpTable();
	}

	private void setUpLogoConsulta() {
		logoConsulta.setHgap(width * 0.015);
		logoConsulta.setPadding(new Insets(height * 0.07, 0, 0, width * 0.04));
	}

	private void setUpRibeiroSantos() {
		ribeiroSantos.setFitHeight(height / 10);
		ribeiroSantos.setFitWidth(height / 10);
	}

	private void setUpButtonsHbox() {
		buttonsHbox.setPrefWidth(width - width * 0.16);
		HBox.setHgrow(viewHbox, Priority.ALWAYS);
		HBox.setHgrow(customQueryHbox, Priority.ALWAYS);
		buttonsHbox.setPadding(new Insets(height * 0.2, width * 0.08, 0, width * 0.08));
	}

	private void setUpViewHbox() {
		viewHbox.setSpacing(width * 0.015);
	}

	private void setUpViewSelector() {
		viewSelector.getItems().addAll(crHandler.getAllViews());
		viewSelector.setPrefSize(width / 8, height / 20);
	}

	private void setUpOkButton() {
		okButton.setPrefSize(width / 30, height / 20);
	}

	private void setUpCustomQueryHbox() {
		customQueryHbox.setSpacing(width * 0.015);
	}

	private void setUpProcedureButton() {
		procedureButton.setPrefSize(width / 8, height / 20);
	}

	private void setUpQueryButton() {
		// deve-se usar prefSize para evitar movimento nas boxes ao dar hover
		queryButton.setPrefSize(width / 10, height / 20);
	}

	private void setUpTable() {
		StackPane.setMargin(table, new Insets(height * 0.3, width * 0.08, height * 0.07, width * 0.08));
		StackPane.setAlignment(table, Pos.BOTTOM_CENTER);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	// Event Listener on Button[#okButton].onAction
	@FXML
	public void okButton(ActionEvent event) {
		if (!viewSelector.getValue().equals("Escolher vista")) {
			ResultSet rs = crHandler.enterView(viewSelector.getValue());
			printTable(rs);
		}
	}

	// Event Listener on Button[#procedureButton].onAction
	@FXML
	public void execProcedure(ActionEvent event) {
		procedureWindow();
	}

	// Event Listener on Button[#queryButton].onAction
	@FXML
	public void execQuery(ActionEvent event) {
		queryWindow();
	}

	/**
	 * Imprime na tabela o conteudo de rs
	 * 
	 * @param rs - o ResultSet com a informacao pretendida
	 */
	protected void printTable(ResultSet rs) {
		table.getItems().clear();
		table.getColumns().clear();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				TableColumn<TableRow, String> tc = new TableColumn<>(rsmd.getColumnName(i));
				tc.setCellValueFactory(new PropertyValueFactory<>("col" + i));
				table.getColumns().add(tc);
			}
			while (rs.next()) {
				TableRow tr = new TableRow();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					switch (i) {
					case 1:
						tr.setCol1(rs.getString(i));
						break;
					case 2:
						tr.setCol2(rs.getString(i));
						break;
					case 3:
						tr.setCol3(rs.getString(i));
						break;
					case 4:
						tr.setCol4(rs.getString(i));
						break;
					default:
						tr.setCol5(rs.getString(i));
					}
				}
				table.getItems().add(tr);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao processar vista");
		}
	}

	/**
	 * Abre uma nova janela para a execucao de queries unicas
	 */
	private void queryWindow() {
		try {
			// criar novo stage
			Stage queryStage = new Stage();
			Image icon = new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png"));
			queryStage.getIcons().add(icon);
			queryStage.setTitle("Executar query");

			// carregar stackpane
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/consultarResidentes/JanelaQuery.fxml"));
			StackPane query = loader.load();

			// carregar controller
			JanelaQueryController controller = loader.<JanelaQueryController>getController();
			controller.setUp(this, crHandler);

			Scene scene = new Scene(query, width / 5, width / 5);
			scene.getStylesheets().add("/JavaFX.css");

			queryStage.setScene(scene);
			queryStage.show();
		} catch (Exception e) {
			System.out.println("Erro ao carregar janela de query unica");
		}
	}

	/**
	 * Abre uma nova janela para a execucao de procedimentos
	 */
	private void procedureWindow() {
		try {
			// criar novo stage
			Stage procedureStage = new Stage();
			Image icon = new Image(getClass().getResourceAsStream("/ribeiro_santos_fundo_branco.png"));
			procedureStage.getIcons().add(icon);
			procedureStage.setTitle("Executar procedimento");

			// carregar stackpane
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/consultarResidentes/JanelaProcedure.fxml"));
			StackPane query = loader.load();

			// carregar controller
			JanelaProcedureController controller = loader.<JanelaProcedureController>getController();
			controller.setUp(crHandler);

			Scene scene = new Scene(query, width / 3, height / 2);
			scene.getStylesheets().add("/JavaFX.css");

			procedureStage.setScene(scene);
			procedureStage.show();
		} catch (Exception e) {
			System.out.println("Erro ao carregar janela de execucao de procedimentos");
		}
	}
}