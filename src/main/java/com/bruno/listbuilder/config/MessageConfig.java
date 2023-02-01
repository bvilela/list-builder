package com.bruno.listbuilder.config;

public abstract class MessageConfig {
	
	private MessageConfig() {
	}
	
	// Generic
	public static final String MSG_ERROR_MIDWEEK_DAY_NOT_FOUND = "Dia da Reunião de Meio de Semana está VAZIO!";
	public static final String MSG_ERROR_WEEKEND_DAY_NOT_FOUND = "Dia da Reunião de Fim de Semana está VAZIO!";
	public static final String LAST_DATE_REQUIRED = "Campo 'ultimaData' da Lista Anterior é obrigatório!";
	public static final String LAST_DATE_INVALID = "Campo 'ultimaData' não é uma data válida!";
	public static final String LIST_DATE_EMPTY = "Lista de Datas VAZIA!";
	
	// File Messages Errors
	public static final String FILE_NOT_FOUND = "Erro ao ler arquivo - Arquivo não encontrado";
	public static final String FILE_SYNTAX_ERROR = "Erro ao ler arquivo - Arquivo não é um JSON válido";
	
	// Christian Life
	public static final String PARTICIPANTS_REQUIRED = "Campo 'participantes' é obrigatório!";
	
	// Designations
	public static final String PRESIDENT_REQUIRED = "Campo 'presidente' é obrigatório!";
	public static final String AUDIOVIDEO_REQUIRED = "Campo 'audioVideo' é obrigatório!";
	public static final String READER_REQUIRED = "Campo 'leitor' é obrigatório!";
	public static final String READER_WATCHTOWER_REQUIRED = "Campo 'asentinela' é obrigatório!";
	public static final String READER_BIBLESTUDY_REQUIRED = "Campo 'estudoBiblico' é obrigatório!";
	public static final String INDICATOR_REQUIRED = "Campo 'indicador' é obrigatório!";
	public static final String INDICATOR_ELEMENT_REQUIRED = "Item da lista de 'indicador' não pode ser Nulo/Vazio/Branco!";
	public static final String MICROPHONE_REQUIRED = "Campo 'microfone' é obrigatório!";
	public static final String MICROPHONE_ELEMENT_REQUIRED = "Item da lista de 'microfone' não pode ser Nulo/Vazio/Branco!";
	
	// Designations Generic
	public static final String LIST_REQUIRED = "Campo 'lista' é obrigatório!";
	public static final String LIST_ELEMENT_REQUIRED = "Item da lista não pode ser Nulo/Vazio/Branco!";
	public static final String LAST_REQUIRED = "Campo 'ultimo' é obrigatório!";
	public static final String LAST_INVALID = "Último %s informado não corresponde a nenhum da Lista!";
	
	// Clearing
	public static final String LAST_GROUP_INVALID = "Último Grupo informado não corresponde a nenhum Grupo da Lista!";
	
	// Discourse
	public static final String LIST_SEND_REVEICE_NULL = "Lista 'Receber' e 'Enviar' estão vazias!";
    public static final String THEMES_REQUIRED = "Temas não pode ser vazio";

}
