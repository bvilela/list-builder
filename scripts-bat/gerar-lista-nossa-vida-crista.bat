:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Vida_crista_"
set caminhoDestino="[YOUR-PATH]"
set lista="VIDA_CRISTA"
set notificacoesAtivas=true
set opcoesExtra=""

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %notificacoesAtivas% %opcoesExtra%