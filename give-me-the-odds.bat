@echo off
if "%~2"=="" goto usage

gradlew.bat build >nul 2>&1
java -jar .\cli\build\libs\cli.jar %1 %2 > temp.txt

echo Output:
set "print="

for /f "tokens=*" %%i in (temp.txt) do (
    if defined print echo %%i
    echo %%i | find "The odds are:" > nul && set print=1
)

del temp.txt
goto end

:usage
echo Usage: give-me-the-odds.bat ^<path-to-empire.json^> ^<path-to-config.json^>
:end
