package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	//GUARDA O ERRO DE CADA CAMPO DO FORMULARIO
	//PRIMEIRO STRING � O CAMPO E O SEGUNDO � A MENSAGEM DE ERRO
	private Map<String, String> errors = new HashMap<>();
	//CLASSE Q REPASSA O ERRO
	public ValidationException(String msg) {
		super(msg);
	}
	//METODOO PARA RETORNAR A LISTA DE ERROS
	public Map<String, String> getErrors (){
		return errors;
	}
	//METODO PARA ADICIONAR O ELEMENTRO NA COLE��O
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
	
}
