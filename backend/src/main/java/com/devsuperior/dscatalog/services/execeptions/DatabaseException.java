/* Classe responsável por tratar de maneira apropriada o erro
 * de Exclusão de Objeto com depencia relacional além de poder
 * personalizar o body da resposta.
 */
package com.devsuperior.dscatalog.services.execeptions;

public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//Contrutor que passa a mensagem de erro para o contrutuor da
	//super classe RunTimeExcetion
	public DatabaseException(String msg) {
		super(msg);
	}

}
