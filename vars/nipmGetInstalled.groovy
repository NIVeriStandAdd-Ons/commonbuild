def call(){
   echo "Getting list of installed NI Packages."
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"

   bat "\"${nipmAppPath}\" list-installed >> build_log >NUL"
}
