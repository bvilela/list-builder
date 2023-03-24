package br.com.bvilela.listbuilder.exception.listtype;

public class RequiredListTypeException extends Exception {

	private static final long serialVersionUID = 2653958924425254811L;

	public RequiredListTypeException() {
		super("Parametro 'tipo.lista' NÃO definido ou VAZIO!");
    }

}
