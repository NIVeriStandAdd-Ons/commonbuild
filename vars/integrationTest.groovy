def call(seqPath, tsVersion, tsBitness) {
   echo "Running test $seqPath with TestStand $tsVersion"

   def programFiles = "C:\\Program Files"

   if (tsBitness == '32') {
      programFiles = programFiles+' (x86)'
   } 

   def seqEditorPath = programFiles+"\\National Instruments\\TestStand ${tsVersion}\\Bin\\SeqEdit.exe"

   def tsVersionSelectorPath = "C:\\Program Files (x86)\\National Instruments\\Shared\\TestStand Version Selector\\TSVerSelect.exe"
   def sequencePath = "${WORKSPACE}\\${seqPath}"

   //Read information about the package under test from the build_properties file created upstream in the pipeline.
   def buildLog = readProperties file: "build_properties"
   def packageFileLoc = buildLog.get('PackageFileLoc')
   def packageFileName = buildLog.get('PackageFileName')
   def packageFilePath = "$packageFileLoc\\$packageFileName"

   formattedTSVersion = tsVersion.substring(2,4)+".0"

   nipmInstallPackage(packageFilePath)

   bat "\"${tsVersionSelectorPath}\" /version ${formattedTSVersion} /installing /noprompt"
   bat "\"${seqEditorPath}\" /outputToStdIO /run MainSequence \"${sequencePath}\" /quit"
   
   nipmRemovePackage(packageFilePath)

   sleep(30)

}
