package consultarResidentes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.BaseDadosRURSAdapter;
import main.Main;

/**
 * Esta classe tem como proposito controlar o acesso a base de dados RURS do
 * ponto de vista da interface do utilizador. Controla o acesso a informacoes
 * dos residentes.
 * 
 * @author Joao Matos
 *
 */
public class ConsultaResidentesHandler extends main.HandlerTemplate {

	/**
	 * Objeto de comunicacao com a BD
	 */
	private BaseDadosRURSAdapter bdAdapter;

	/**
	 * Construtor da classe. Este construtor deve ser publico de forma a extender
	 * Application
	 */
	public ConsultaResidentesHandler() {
		this.bdAdapter = BaseDadosRURSAdapter.getBaseDadosRURSAdapter();
	}

	/**
	 * Informa se o utilizador e ou nao um convidado (residente)
	 * 
	 * @return true se for um residente, false caso contrario
	 */
	public boolean isGuest() {
		return bdAdapter.isGuest();
	}

	/**
	 * Retorna uma lista de todas as views disponiveis na base de dados
	 * 
	 * @return uma lista de strings com o nome de todas as views
	 */
	public List<String> getAllViews() {
		ResultSet rs = bdAdapter.getAllViews();
		List<String> ls = new ArrayList<>();
		try {
			while (rs.next())
				ls.add(rs.getString(1));
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter as views guardadas na base de dados");
		}
		return ls;
	}

	/**
	 * Executa a view especificada na base de dados
	 * 
	 * @param view - a view escolhida
	 * @return um ResultSet com os resultados da execucao da view
	 */
	public ResultSet enterView(String view) {
		return bdAdapter.execView(view);
	}

	/**
	 * Retorna uma lista de todas os procedures disponiveis na base de dados
	 * 
	 * @return uma lista de strings com o nome de todas os procedures
	 */
	public List<String> getAllProcedures() {
		ResultSet rs = bdAdapter.getAllProcedures();
		List<String> ls = new ArrayList<>();
		try {
			while (rs.next()) {
				ls.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter os procedures guardados na base de dados");
		}
		return ls;
	}

	/**
	 * Retorna uma lista de todas os parametros do procedure especificado
	 * 
	 * @param name - o procedure em causa
	 * @return uma lista com todos os parametros de name, no formato "nome: tipo"
	 */
	public List<String> getProcedureParams(String name) {
		ResultSet rs = bdAdapter.getProcedureParams(name);
		List<String> ls = new ArrayList<>();
		try {
			while (rs.next()) {
				ls.add(rs.getString(1) + ": " + rs.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter os parametros do procedure");
		}
		return ls;
	}

	/**
	 * Executa o procedure escolhido pelo utilizador com os argumentos fornecidos.
	 * Datas devem ser fornecidas em 1 parametro: "dia/mes/ano".
	 * 
	 * @param name - o nome do procedure
	 * @param args - os argumentos a aplicar
	 * @return o ResultSet resultante do procedimento (null se for void)
	 * @throws IllegalArgumentException se os argumentos fornecidos forem ilegais
	 */
	public ResultSet enterProcedure(String name, String... args) throws IllegalArgumentException {
		Object[] objs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			try {
				if (args[i].contains(".")) // float (double em java)
					objs[i] = Double.parseDouble(args[i]);
				else if (args[i].contains("/")) { // data ou ano letivo
					String[] dmy = args[i].split("/");
					long l = new GregorianCalendar(Integer.parseInt(dmy[2]), Integer.parseInt(dmy[1]) - 1,
							Integer.parseInt(dmy[0])).getTimeInMillis();
					objs[i] = new java.sql.Date(l);
				} else // tentar integer
					objs[i] = Integer.parseInt(args[i]);
			} catch (Exception e) {
				if (args[i].equals("true")) // bits sao passados como boolean
					objs[i] = true;
				else if (args[i].equals("false"))
					objs[i] = false;
				objs[i] = args[i]; // se nao, string
			}
		}
		return bdAdapter.execProcedure(name, objs);
	}

	/**
	 * Executa a query especificada na base de dados
	 * 
	 * @param query - a query a realizar
	 * @return o ResultSet resultante da query (null se for void)
	 * @throws IllegalArgumentException numa tentativa de execucao de queries não
	 *                                  SELECT no modo não previlegiado
	 */
	public ResultSet enterQuery(String query) throws IllegalArgumentException {
		if (bdAdapter.isGuest() && !query.split(" ")[0].equalsIgnoreCase("select"))
			throw new IllegalArgumentException();
		return bdAdapter.execCustomQuery(query);
	}

	@Override
	public void launchGUI(Stage primaryStage) {
		try {
			// carregar stackpane e alterar cena
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/consultarResidentes/EcraConsulta.fxml"));
			StackPane dbCheck = loader.load();

			EcraConsultaController controller = loader.<EcraConsultaController>getController();
			controller.setUp(this);

			Scene scene = new Scene(dbCheck, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
			scene.getStylesheets().add("/JavaFX.css");

			primaryStage.setScene(scene);
		} catch (Exception e) {
			System.out.println("Erro ao carregar pagina de login");
		}
	}
}
