@echo off

:: coment: set encoding UTF-8
chcp 65001

:: Parametros
set caminhoOrigem="arquivos\saida\Discurso_"
set caminhoDestino="[YOUR-PATH]"
set lista="DISCURSO"
set notificacoesAtivas=true
set opcoesExtra=""

:: Chamando Programa
call gerar-lista-base.bat %caminhoOrigem% %caminhoDestino% %lista% %notificacoesAtivas% %opcoesExtra%