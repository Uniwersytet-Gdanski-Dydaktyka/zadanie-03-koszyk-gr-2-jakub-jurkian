@echo off
setlocal

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%~dp0.mvn\maven\apache-maven-%MAVEN_VERSION%
set MAVEN_ZIP=%~dp0.mvn\maven\apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven nie znaleziony. Pobieranie Maven %MAVEN_VERSION%...
    if not exist "%~dp0.mvn\maven" mkdir "%~dp0.mvn\maven"
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'"
    if errorlevel 1 (
        echo BLAD: Nie udalo sie pobrac Mavena.
        exit /b 1
    )
    echo Rozpakowywanie...
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%~dp0.mvn\maven' -Force"
    del "%MAVEN_ZIP%"
    echo Maven gotowy.
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
