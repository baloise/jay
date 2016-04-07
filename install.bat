@echo off
echo    __  __  _  _ 
echo  _(  )/ _\( \/ )
echo / \) /    \)  / 
echo \____\_/\_(__/  
echo.

set JAY_VERSION=-SNAPSHOT
call:download "https://jitpack.io/com/github/baloise/jay/jay/%JAY_VERSION%/jay-%JAY_VERSION%.jar" "%USERPROFILE%/jay.jar"

::--------------------------------------------------------
::-- Functions
::--------------------------------------------------------
goto:eof

:download
    echo downloading %~2
    powershell -Command "$proxy = [System.Net.WebRequest]::GetSystemWebProxy();$proxy.Credentials = [System.Net.CredentialCache]::DefaultCredentials;$wc = new-object system.net.WebClient;$wc.proxy = $proxy;$wc.DownloadFile('%~1', '%~2');"
goto:eof
