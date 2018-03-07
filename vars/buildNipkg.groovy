def call(payloadDir, baseVersion, buildNumber, stagingPath, lvVersion) {
   
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"

   // Read PROPERTIES from .nipkg control file.
   def controlFields = readProperties file: "control"
   def basePackageName = "${controlFields.get('Package')}"

   // Read TEXT from .nipkg control file 
   def controlFileText = readFile "control" // Read nipkg control file 

   // Using Semantic version scheme: MAJOR.MINOR.PATCH.BUILD
   def nipkgVersion = "${baseVersion}.${buildNumber}"

   // Replace {version} expressions with current VeriStand and .nipkg versions being built.
   def newControlFileText = controlFileText.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   finalControlFileText = newControlFileText.replaceAll("\\{nipkg_version\\}", "${nipkgVersion}")
   def newStagingPath = stagingPath.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageName = basePackageName.replaceAll("\\{veristand_version\\}", "${lvVersion}")

   echo "Building ${packageName} with control file attributes:"
   echo finalControlFileText

   // Copy package payload to nipkg staging directory. 
   bat "(robocopy \"${payloadDir}\" \"nipkg\\${packageName}\\data\\${newStagingPath}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   // Create .nipkg source files.
   writeFile file: "nipkg\\${packageName}\\debian-binary", text: "2.0"      
   writeFile file: "nipkg\\${packageName}\\control\\control", text: finalControlFileText

   // Build nipkg using NI Package Manager CLI pack command. 
   bat "\"${nipmAppPath}\" pack \"nipkg\\${packageName}\" \"${payloadDir}\"" 

}

