def call(packagePath) {

   echo "Using NI Package Manager to install $packagePath"
   bat "commonbuild\\resources\\installNipkg.bat \"${packagePath}\""
}
