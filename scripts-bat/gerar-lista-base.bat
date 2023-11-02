@echo off

:: coment: set encoding UTF-8
chcp 65001

call :logTitle "Iniciando"

:: Valid Parameters
set pathSrc=%1
set pathDes=%2
set list=%3
set notificationsActive=%4
set extraOptions=%5

call :validAndPrintParameters

:: chamando .jar
call :log "Iniciando Programa..."
java -jar list-builder.jar --spring.profiles.active=PRD --tipo.lista=%list% --notifications.active=%notificationsActive% %extraOptions%
	
IF %ERRORLEVEL% NEQ 0 (
	call :finishError
)

call :log "Copiando Arquivo PDF..."
powershell -command "Copy-Item -Path (gci %pathSrc%*.pdf | sort LastWriteTime | select -last 1).fullname -Destination '%pathDes%'"

call :log "Copiando Arquivo Docx (Se houver)..."
powershell -command "Copy-Item -Path (gci %pathSrc%*.docx | sort LastWriteTime | select -last 1).fullname -Destination '%pathDes%'"

call :log "Copiando Arquivo PNG (Se houver)..."
powershell -command "Copy-Item -Path (gci %pathSrc%*.png | sort LastWriteTime | select -last 1).fullname -Destination '%pathDes%'"

call :logTitle "Finalizado com SUCESSO"

pause


:: ***********************
:: *** Utils Functions ***
:: ***********************
:validAndPrintParameters
IF %list% == "" (
	echo "Parametro 'Lista' obrigatorio"
	call :finishError
)

IF %pathSrc% == "" (
	echo "Parametro 'Caminho Origem' obrigatorio"
	call :finishError
)

IF %pathDes% == "" (
	echo "Parametro 'Caminho Destino' obrigatorio"
	call :finishError
)

:: Removendo Aspas do ExtraOps (caso contrario, da problema no jar)
set extraOptions=%extraOptions:"=%

:: Log Parameters
call :log "Parametros"
echo "  Lista: %list%
echo "  Caminho Origem: %pathSrc%
echo "  Caminho Destino: %pathDes%
echo "  Extra Opcoes: %extraOptions%
echo.
EXIT /B 0

:finishError
call :logTitle "Finalizado com ERRO"
pause
exit
EXIT /B 0

:logTitle 
echo.
echo "********** %~1! **********
echo.
EXIT /B 0

:log
echo "*** %~1! ***
EXIT /B 0
