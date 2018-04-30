def call(){
   echo "Getting list of installed NI Packages."
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "echo \"NI Package Versions installed:\" >version_manifest"
   bat "\"${nipmAppPath}\" list-installed >version_manifest"
}
