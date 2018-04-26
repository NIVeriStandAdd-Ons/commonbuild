def call(seqPath, tsVersion) {
   echo "Running test $seqPath with TestStand $tsVersion"
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def seqEditorPath = "C:\\Program Files (x86)\\National Instruments\\TestStand ${tsVersion}\\Bin\\SeqEdit.exe"
   def sequencePath = "${WORKSPACE}\\${seqPath}"
   def nipkgFilePath = readFile file: "build_log"

   bat "\"${nipmAppPath}\" -y \"${nipkgFilePath}\""
   bat "\"${seqEditorPath}\" /outputToStdIO /run MainSequence \"${sequencePath}\" /quit"
}