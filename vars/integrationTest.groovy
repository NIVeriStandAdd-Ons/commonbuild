def call(seqPath, tsVersion, includeTestStand64) {
   echo "Running test $seqPath with TestStand $tsVersion"

   def seqEditorPath = "C:\\Program Files (x86)\\National Instruments\\TestStand ${tsVersion}\\Bin\\SeqEdit.exe"
   def seqEditor64Path = "C:\\Program Files\\National Instruments\\TestStand ${tsVersion}\\Bin\\SeqEdit.exe"
   def tsVersionSelectorPath = "C:\\Program Files (x86)\\National Instruments\\Shared\\TestStand Version Selector\\TSVerSelect.exe"
   def sequencePath = "${WORKSPACE}\\${seqPath}"
   def buildLog = readProperties file: "build_properties"
   def packageFileLoc = buildLog.get('PackageFileLoc')
   def packageFileName = buildLog.get('PackageFileName')
   def packageFilePath = "$packageFileLoc\\$packageFileName"

   formattedTSVersion = tsVersion.substring(2,4)+".0"

   bat "C:\\github-release\\elevate-1.3.0-x86-64\\elevate.exe -k commonbuild\\resources\\installNipkg.bat \"${packageFilePath}\""
   bat "\"${tsVersionSelectorPath}\" /version ${formattedTSVersion} /installing /noprompt"

   bat "\"${seqEditorPath}\" /outputToStdIO /run MainSequence \"${sequencePath}\" /quit"

   if (includeTestStand64) {
      bat "\"${seqEditor64Path}\" /outputToStdIO /run MainSequence \"${sequencePath}\" /quit"
   }
}
