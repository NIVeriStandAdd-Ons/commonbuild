import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(payloadDir, version, stagingPath, lvVersion) {

   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def nipkgVersion
   def buildNumber = 0
   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   // Read PROPERTIES from .nipkg control file.
   def controlFields = readProperties file: "control"
   def basePackageName = "${controlFields.get('Package')}"
   def controlFileText = readFile "control"
   if(fileExists: instructions) {
      def instructionsFileText = readFile "instructions"
   }

   buildNumber = getBuildNumber(componentName, lvVersion)
   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   switch(componentBranch) {
      case 'master': flag = ""; break;
      case 'develop': flag = "-beta"; break;
      default: flag = "-alpha"; break;
   }
   nipkgVersion = version+flag+"+paddedBuildNumber"

   // Replace {version} expressions with current VeriStand and .nipkg versions being built.
   def newControlFileText = controlFileText.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   finalControlFileText = newControlFileText.replaceAll("\\{nipkg_version\\}", "${nipkgVersion}")
   def newStagingPath = stagingPath.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageName = basePackageName.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageFilename = "${packageName}_${nipkgVersion}_windows_x64.nipkg"
   def packageFilePath = "$payloadDir\\$packageFilename"

   // Copy package payload to nipkg staging directory.
   bat "(robocopy \"${payloadDir}\" \"nipkg\\${packageName}\\data\\${newStagingPath}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   // Create .nipkg source files.
   writeFile file: "nipkg\\${packageName}\\debian-binary", text: "2.0"
   writeFile file: "nipkg\\${packageName}\\control\\control", text: finalControlFileText
   if(fileExists: instructions) {
      writeFile file: "nipkg\\${packageName}\\instructions\\instructions", text: instructionsFileText
	}

   // Build nipkg using NI Package Manager CLI pack command.
   bat "\"${nipmAppPath}\" pack \"nipkg\\${packageName}\" \"${payloadDir}\""

   // Write build properties to properties file and build log.
   ['build_properties','build_log'].each { logfile ->
      writeFile file: "$logfile", text: "PackageName: ${packageName}\nPackageFileName: ${packageFilename}\nPackageFileLoc: ${payloadDir}\nPackageVersion: ${nipkgVersion}\nPackageBuildNumber: $buildNumber\n"
   }
   vipmGetInstalled(lvVersion)
   nipmGetInstalled()

   echo "Updating build number for ${componentName} (${lvVersion}) to ${buildNumber} in commonbuild-configuration repository."
   def commitMessage = "Updating ${componentName} for VeriStand ${lvVersion} to build number ${buildNumber}."
   bat "commonbuild\\resources\\configPush.bat \"$commitMessage\""

   return buildNumber

}
