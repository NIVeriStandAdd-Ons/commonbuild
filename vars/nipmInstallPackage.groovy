def call(packagePath) {
   
   echo "Using NI Package Manager to install $packagePath"

   //Use elevated command line session to install NI Package using NIPM.
   bat "C:\\github-release\\elevate-1.3.0-x86-64\\elevate.exe -k commonbuild\\resources\\installNipkg.bat \"${packagePath}\""
   
}