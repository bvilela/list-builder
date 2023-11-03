:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Assistencia_"
set caminhoDestino="[YOUR-PATH]"
set lista="ASSISTENCIA"
set notificacoesAtivas=true
set opcoesExtra="--audience.layout=COMPACT"

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %notificacoesAtivas% %opcoesExtra%