package main;

import javafx.stage.Stage;

/**
 * Esta classe define a template geral que todos os hanlder da aplicação devem
 * seguir, pelo que todos a devem extender
 * 
 * @author Joao Matos
 *
 */
public abstract class HandlerTemplate {

	/**
	 * Inicia a GUI associada ao caso de uso
	 * 
	 * @param primaryStage - o stage a usar
	 */
	public abstract void launchGUI(Stage primaryStage);

}
