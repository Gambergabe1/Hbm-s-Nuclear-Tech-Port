@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "PROJECT_ROOT=%SCRIPT_DIR%.."
set "OUTPUT_ROOT=%PROJECT_ROOT%\migration\1.21.1"
set "SUMMARY_FILE=%OUTPUT_ROOT%\reports\summary.txt"
set "SHOULD_PAUSE=1"

for %%I in ("%OUTPUT_ROOT%") do set "OUTPUT_ROOT=%%~fI"
for %%I in ("%SUMMARY_FILE%") do set "SUMMARY_FILE=%%~fI"

if /I "%~1"=="--no-pause" (
    set "SHOULD_PAUSE=0"
    shift
)

echo.
echo ==========================================
echo   HBM 1.21.1 Migration Starting
echo ==========================================
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%migrate_1_21_1.ps1" %*
set "EXIT_CODE=%ERRORLEVEL%"

echo.
if not "%EXIT_CODE%"=="0" (
    echo ==========================================
    echo   Migration FAILED
    echo ==========================================
    echo Exit code: %EXIT_CODE%
    echo.
    echo Check the PowerShell error output above.
    echo.
    if "%SHOULD_PAUSE%"=="1" pause
    endlocal & exit /b %EXIT_CODE%
)

echo ==========================================
echo   Migration Complete
echo ==========================================
echo Output folder:
echo %OUTPUT_ROOT%
echo.

if exist "%SUMMARY_FILE%" (
    echo Summary:
    type "%SUMMARY_FILE%"
    echo.
)

echo Generated templates:
echo %OUTPUT_ROOT%\templates
echo.
echo Generated resources:
echo %OUTPUT_ROOT%\src\main\resources
echo.

if "%SHOULD_PAUSE%"=="1" pause

endlocal
