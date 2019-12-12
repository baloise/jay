@echo off
echo    __  __  _  _ 
echo  _(  )/ _\( \/ )
echo / \) /    \)  / 
echo \____\_/\_(__/  
echo.

if not exist %userprofile%\.jay mkdir %userprofile%\.jay
call:download "http://jitpack.io/com/github/baloise/jay/jay/-SNAPSHOT/jay-SNAPSHOT.jar" "%USERPROFILE%/.jay/jay.jar"

::--------------------------------------------------------
::-- Functions
::--------------------------------------------------------
goto:eof

:download
    echo downloading %~2
    powershell -Command "$proxy = [System.Net.WebRequest]::GetSystemWebProxy();$proxy.Credentials = [System.Net.CredentialCache]::DefaultCredentials;$wc = new-object system.net.WebClient;$wc.proxy = $proxy;$wc.DownloadFile('%~1', '%~2');"
goto:eof
