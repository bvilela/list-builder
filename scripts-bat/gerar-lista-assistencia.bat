:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Assistencia_"
set caminhoDestino="[YOUR-PATH]"
set lista="ASSISTENCIA"
set opcoesExtra="--notif.active=true"

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %opcoesExtra%