@echo off
REM Run the transmitter using the bundled Windows x64 JRE
REM Usage: run-transmitter-win_x64.bat path\to\properties.file

setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"

if "%~1"=="" (
  echo Usage: %~n0 path\to\properties.file 1>&2
  exit /b 1
)
set "PROPS=%~1"

REM Find the platform-specific JRE directory (versioned folder matching *-win_x64)
set "JRE_DIR="
for /d %%D in ("%SCRIPT_DIR%*-win_x64") do (
  if exist "%%~fD\bin\java.exe" (
    set "JRE_DIR=%%~fD"
    goto :foundJre
  )
)
:foundJre
if "%JRE_DIR%"=="" (
  echo Bundled JRE for win_x64 not found next to this script. 1>&2
  exit /b 3
)
set "JAVA_BIN=%JRE_DIR%\bin\java.exe"
if not exist "%JAVA_BIN%" (
  echo java executable not found at: %JAVA_BIN% 1>&2
  exit /b 4
)

REM Locate the fat JAR
set "JAR_FILE="
for %%F in ("%SCRIPT_DIR%*-jar-with-dependencies.jar") do (
  if exist "%%~fF" (
    set "JAR_FILE=%%~fF"
    goto :foundJar
  )
)
:foundJar
if "%JAR_FILE%"=="" (
  echo Fat JAR (*-jar-with-dependencies.jar) not found next to this script. 1>&2
  exit /b 5
)

"%JAVA_BIN%" -jar "%JAR_FILE%" --properties="%PROPS%"
exit /b %errorlevel%
