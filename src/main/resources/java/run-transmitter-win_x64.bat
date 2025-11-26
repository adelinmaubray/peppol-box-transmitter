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
for /f "delims=" %%D in ('dir /b /ad "%SCRIPT_DIR%*-win_x64" 2^>nul') do if exist "%SCRIPT_DIR%%%~D\bin\java.exe" set "JRE_DIR=%SCRIPT_DIR%%%~D" & goto :foundJre
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

echo "%JRE_DIR%"

REM Locate the fat JAR
set "search_pattern=*-jar-with-dependencies.jar"
set "JAR_FILE="
for /f "delims=" %%f in ('dir /b /s "%search_pattern%" 2^>nul') do (
    set "JAR_FILE=%%f"
    goto :found
)

if "%JAR_FILE%"=="" (
    echo Error: File matching pattern "%search_pattern%" not found.
    exit /b 1
)

:found

"%JAVA_BIN%" -jar "%JAR_FILE%" --properties="%PROPS%"
exit /b %errorlevel%
