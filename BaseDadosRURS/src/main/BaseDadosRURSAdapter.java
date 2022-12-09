package main;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import com.microsoft.sqlserver.jdbc.SQLServerException;

/**
 * Esta classe serve a funcao de adaptador entre a execucao do programa e a base
 * de dados RURS, sendo a unica classe que comunica diretamente com a base de
 * dados.
 * 
 * @author Joao Matos
 *
 */
public class BaseDadosRURSAdapter {

	/**
	 * Define se o utilizador tem permissoes de admin
	 */
	private boolean isGuest;

	/**
	 * Modo de autenticacao escolhido, true para integrada, false para sql
	 * authentication
	 */
	private boolean authMode;

	/**
	 * Username usado na ligacao
	 */
	private String username;

	/**
	 * Password usada na autenticacao
	 */
	private String password;

	/**
	 * Conexao a base de dados
	 */
	private Connection con;

	/**
	 * Objeto usado para execucao de statements SQL
	 */
	private Statement stmt;

	/**
	 * Instancia unica da classe
	 */
	private static BaseDadosRURSAdapter instance;

	/**
	 * Construtor singleton da classe
	 */
	private BaseDadosRURSAdapter() {
		// preparar valores de login para convidados
		username = "guest";
		password = "qwertyuiop";
	}

	/**
	 * Getter publico da classe
	 * 
	 * @return a instancia unica da classe
	 */
	public static BaseDadosRURSAdapter getBaseDadosRURSAdapter() {
		if (instance == null)
			instance = new BaseDadosRURSAdapter();
		return instance;
	}

	/**
	 * @return true se o user atual e um convidado, false caso contrario
	 */
	public boolean isGuest() {
		return isGuest;
	}

	/**
	 * @param isGuest - o valor a colocar em isGuest (true se o user atual e um
	 *                convidado, false caso contrario)
	 */
	public void setGuest(boolean isGuest) {
		this.isGuest = isGuest;
	}

	/**
	 * @return true se usa autenticacao integrada, false caso contrario
	 */
	public boolean getAuthMode() {
		return authMode;
	}

	/**
	 * @param authMode - o valor a colocar em authMode (true se usa autenticacao
	 *                 integrada, false caso contrario)
	 */
	public void setAuthMode(boolean authMode) {
		this.authMode = authMode;
	}

	/**
	 * @param username - username do utilizador
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password - password do utilizador
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Estabelece uma ligacao a base de dados
	 * 
	 * @return true se a ligacao sucedeu, false se falhou
	 */
	public boolean setupConnection() {
		try {
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("config.properties");
			prop.load(fis);
			if (authMode)
				con = DriverManager
						.getConnection("jdbc:sqlserver://" + prop.getProperty("server") + ":" + prop.getProperty("port")
								+ ";databaseName=RURS;integratedSecurity=true;trustServerCertificate=true");
			else
				con = DriverManager.getConnection("jdbc:sqlserver://" + prop.getProperty("server") + ":"
						+ prop.getProperty("port") + ";databaseName=RURS;trustServerCertificate=true;", username,
						password);
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Não foi possivel ligar à base de dados, verifique as suas informacoes de login");
			return false;
		}
		return true;
	}

	/**
	 * Tenta fechar a conexao a base de dados, caso esta exista
	 */
	public void closeConnection() {
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Erro ao fechar a ligacao.");
			}
	}

	/**
	 * Obtem o nome de todas as views definidas na base de dados
	 * 
	 * @return um ResultSet contendo os nomes de todas as views
	 */
	public ResultSet getAllViews() {
		try {
			return stmt.executeQuery("SELECT o.name FROM sys.objects as o WHERE o.type = 'V';");
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter as views guardadas na base de dados");
		}
		return null;
	}

	/**
	 * Executa a query de visualizacao de uma vista na base de dados
	 * 
	 * @param name - nome da vista selecionada
	 * @return um ResultSet com o resultado da view ou null em caso de erro
	 */
	public ResultSet execView(String name) {
		try {
			if (name.equals("hierarquia"))
				return stmt.executeQuery("SELECT * FROM " + name + " ORDER BY hierarquia;");
			return stmt.executeQuery("SELECT * FROM " + name + ";");
		} catch (SQLException e) {
			System.out.println("Erro ao carregar vista " + name + ". Talvez nao exista na base de dados.");
		}
		return null;
	}

	/**
	 * Obtem todos os procedures definidas na base de dados
	 * 
	 * @return um ResultSet contendo os nomes de todos os procedures
	 */
	public ResultSet getAllProcedures() {
		try {
			return stmt.executeQuery("SELECT DISTINCT SO.name\r\n" + "FROM sys.objects AS SO\r\n"
					+ "INNER JOIN sys.parameters AS P ON SO.OBJECT_ID = P.OBJECT_ID\r\n"
					+ "WHERE SO.name NOT LIKE '[f][n][_]%'\r\n" + "AND SO.name NOT LIKE '[s][p][_]%';");
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter os procedures guardados na base de dados");
		}
		return null;
	}

	/**
	 * Obtem o nome e tipo dos parametros do procedure especificado, na mesma ordem
	 * a que lhe devem ser passados
	 * 
	 * @param name - o nome do procedure
	 * @return um ResultSet com os resultados (col 1 nome, col 2 tipo)
	 */
	public ResultSet getProcedureParams(String name) {
		try {
			return stmt.executeQuery("SELECT P.name, TYPE_NAME(P.user_type_id)\r\n" + "FROM sys.objects AS SO\r\n"
					+ "INNER JOIN sys.parameters AS P ON SO.OBJECT_ID = P.OBJECT_ID\r\n" + "WHERE SO.name = '" + name
					+ "'\r\n" + "ORDER BY P.parameter_id;");
		} catch (SQLException e) {
			System.out.println("Nao foi possivel obter os parametros do procedure");
		}
		return null;
	}

	/**
	 * Executa o procedimento escolhido na base de dados com os parametros de
	 * entrada especificados em args. Estes parametros devem ser passados ao metodo
	 * na ordem correta e sao suportados os tipos int, string, float (double em
	 * java), bit e date. Podem ser passados tipos NULL atraves de string no formato
	 * "null TIPO"
	 * 
	 * @param name - nome do procedimento selecionado
	 * @param args - (opcional) argumentos a aplicar ao procedimento
	 * @return um ResultSet com o resultado do procedimento ou null caso este seja
	 *         void
	 * @throws IllegalArgumentException se forem fornecidos argumentos ilegais
	 */
	public ResultSet execProcedure(String name, Object... args) throws IllegalArgumentException {
		ResultSet rs = null;
		try {
			String statement = "{call dbo." + name + "(";
			for (int i = 0; i < args.length; i++) { // construir statement para n args
				if (i == 0)
					statement += "?";
				else
					statement += ", ?";
			}
			statement += ")}";
			PreparedStatement pstmt = con.prepareStatement(statement);
			int position = 1;
			for (Object obj : args) {
				if (obj instanceof Integer)
					pstmt.setInt(position, (Integer) obj);
				else if (obj instanceof Double)
					pstmt.setDouble(position, (Double) obj);
				else if (obj instanceof Date)
					pstmt.setDate(position, (Date) obj);
				else if (obj instanceof Boolean)
					pstmt.setBoolean(position, (Boolean) obj);
				else if (obj instanceof String) {
					String s = (String) obj;
					if (s.startsWith("null")) { // verificar null
						String type = s.substring(s.indexOf(' ') + 1);
						switch (type) {
						case "int":
							pstmt.setNull(position, Types.INTEGER);
							break;
						case "varchar":
							pstmt.setNull(position, Types.VARCHAR);
							break;
						case "float":
							pstmt.setNull(position, Types.FLOAT);
							break;
						case "bit":
							pstmt.setNull(position, Types.BIT);
							break;
						default:
							pstmt.setNull(position, Types.DATE);
						}
					} else {
						pstmt.setString(position, s);
					}
				} else
					throw new IllegalArgumentException();
				position++;
			}
			rs = pstmt.executeQuery();
			return rs;
		} catch (SQLServerException s) { // caso o procedimento seja void e lancada esta excecao e retornado null
			return null;
		} catch (SQLException e) {
			System.out.println("Erro ao executar o procedimento " + name + ". Talvez nao exista na base de dados.");
		}
		return rs;
	}

	/**
	 * Executa a query especificada
	 * 
	 * @param name - nome da vista selecionada
	 * @return um ResultSet com o resultado da view ou null em caso de erro ou de
	 *         query void
	 */
	public ResultSet execCustomQuery(String query) {
		try {
			return stmt.executeQuery(query);
		} catch (SQLServerException s) { // caso seja void
			return null;
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query especificada");
		}
		return null;
	}
}
