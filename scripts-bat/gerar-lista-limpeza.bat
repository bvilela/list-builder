:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Limpeza_"
set caminhoDestino="[YOUR-PATH]"
set lista="LIMPEZA"
set opcoesExtra="--layout.limpeza=2 --notif.active=true"

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %opcoesExtra%