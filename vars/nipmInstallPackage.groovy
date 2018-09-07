def call(packagePath) {

   echo "Using NI Package Manager to install $packagePath"
   bat """call python.exe commonbuild\\resources\\addRemoveNipkg.py install \"${packagePath}\"
   exit %ERRORLEVEL%"""
}
