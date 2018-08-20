def call(packagePath) {
   
   echo "Using NI Package Manager to install $packagePath"

   //Use elevated command line session to install NI Package using NIPM.
   bat "commonbuild\\resources\\installNipkg.bat \"${packagePath}\""
   
}