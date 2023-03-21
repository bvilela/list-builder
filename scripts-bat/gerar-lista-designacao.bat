:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Designacao_"
set caminhoDestino="[YOUR-PATH]"
set lista="DESIGNACAO"
set opcoesExtra="--notif.active=true"

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %opcoesExtra%