def call(packagePath) {

   echo "Using NI Package Manager to remove $packagePath"
   bat """call python.exe commonbuild\\resources\\addRemoveNipkg.py uninstall \"${packagePath}\"
   exit %ERRORLEVEL%"""
}
