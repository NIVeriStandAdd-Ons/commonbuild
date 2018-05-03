def call(){

   echo "Fetching list of installed NI Packages."

   //Use NI Package Managet CLI to get list of installed NI Packages.
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "echo \"NI Package Versions installed:\" >nipm_version_manifest"
   bat "\"${nipmAppPath}\" list-installed >nipm_version_manifest"
   def installedNipkgList = readFile "nipm_version_manifest"

   //Add NIPM output to build log.
   def buildLog = readFile 'build_log'
   writeFile file: 'build_log', text: buildLog+"\r\n"+installedNipkgList

}
