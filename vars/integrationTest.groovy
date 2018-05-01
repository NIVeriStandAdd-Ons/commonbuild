def call(seqPath, tsVersion) {
   echo "Running test $seqPath with TestStand $tsVersion"
   def seqEditorPath = "C:\\Program Files (x86)\\National Instruments\\TestStand ${tsVersion}\\Bin\\SeqEdit.exe"
   def sequencePath = "${WORKSPACE}\\${seqPath}"
   def buildLog = readProperties file: "build_log"
   def nipkgFileLoc = buildLog.get('PackageFileLoc')
   def nipkgFileName = buildLog.get('PackageFileName')
   def nipkgFilePath = "$nipkgFileLoc\\$nipkgFileName"

   bat "C:\\github-release\\elevate-1.3.0-x86-64\\elevate.exe -k commonbuild\\resources\\installNipkg.bat \"${nipkgFilePath}\""
   bat "\"${seqEditorPath}\" /outputToStdIO /run MainSequence \"${sequencePath}\" /quit"
}
