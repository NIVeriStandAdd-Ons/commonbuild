def call(){

   echo "Fetching list of installed NI Packages."

   //Use NI Package Managet CLI to get list of installed NI Packages.
   bat """call python.exe commonbuild\\resources\\addRemoveNipkg.py list-installed
   exit %ERRORLEVEL%"""
}
