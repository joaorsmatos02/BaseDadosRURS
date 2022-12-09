package main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Esta classe representa a execucao principal do programa, servindo como portal
 * de acesso aos seus casos de uso.
 * 
 * Tem como objetivo principal estabelecer uma ligacao a base de dados RURS
 * local no porto 1433 (default), usando um driver JDBC para Java 11 e uma DLL
 * de autenticacao para processadores de 64 bits e 32 bits.
 * 
 * De forma a estabelecer uma ligacao com sucesso e necessario que a comunicacao
 * por TCP/IP esteja habilitada na seccao "SQL Server Network Configuration" do
 * SQL Server Configuration Manager. Para poder usar ambos os modos de
 * autenticacao, tambem a opcao "SQL Server and Windows Authentication mode"
 * deve estar habilitada na seccao "Security" das propriedades do servidor.
 * 
 * @author Joao Matos
 *
 */
public class Main extends Application {

	/**
	 * Handler de consulta a base de dados RURS
	 */
	private static LoginHandler loginHandler;

	/**
	 * Metodo principal
	 * 
	 * @param args - nao usado
	 */
	public static void main(String[] args) {
		loginHandler = new LoginHandler();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loginHandler.launchGUI(primaryStage);
	}
}
