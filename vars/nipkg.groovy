import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(payloadDir, devXmlPath, stagingPath, lvVersion) {
   
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def nipkgVersion
   def buildNumber
   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   // Read PROPERTIES from .nipkg control file.
   def controlFields = readProperties file: "control"
   def basePackageName = "${controlFields.get('Package')}"
   def controlFileText = readFile "control"
   def baseVersion = getDeviceVersion(devXmlPath, lvVersion)

   echo "Getting 'build_number' for ${componentName}."
   configurationJsonFile = readJSON file: "configuration_${lvVersion}.json"
   configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())

   if(configurationMap.repositories.containsKey(componentName)) {
      buildNumber = getBuildNumber(componentName, configurationMap)
      echo "Next build number: $buildNumber"
   } else { 
         configurationMap.repositories[componentName] = ['build_number': buildNumber] 
   }
   configurationJSON = readJSON text: JsonOutput.toJson(configurationMap)
   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   switch(componentBranch) {
      case 'master': nipkgVersion = baseVersion+"+$paddedBuildNumber"; break;
      case 'develop': nipkgVersion = baseVersion+"-beta+$paddedBuildNumber"; break;
      default: nipkgVersion = baseVersion+"-alpha+$paddedBuildNumber"; break;
   }

   // Replace {version} expressions with current VeriStand and .nipkg versions being built.
   def newControlFileText = controlFileText.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   finalControlFileText = newControlFileText.replaceAll("\\{nipkg_version\\}", "${nipkgVersion}")
   def newStagingPath = stagingPath.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageName = basePackageName.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageFilename = "${packageName}_${nipkgVersion}_windows_x64.nipkg"
   def packageFilePath = "$payloadDir\\$packageFilename"

   echo "Building ${packageName} with control file attributes:"
   echo finalControlFileText

   // Copy package payload to nipkg staging directory. 
   bat "(robocopy \"${payloadDir}\" \"nipkg\\${packageName}\\data\\${newStagingPath}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   // Create .nipkg source files.
   writeFile file: "nipkg\\${packageName}\\debian-binary", text: "2.0"      
   writeFile file: "nipkg\\${packageName}\\control\\control", text: finalControlFileText

   // Build nipkg using NI Package Manager CLI pack command. 
   bat "\"${nipmAppPath}\" pack \"nipkg\\${packageName}\" \"${payloadDir}\"" 
   
   writeFile file: "build_log", text: "PackageName: ${packageName}\nPackageFileName: ${packageFilename}\nPackageFileLoc: ${payloadDir}\nPackageVersion: ${nipkgVersion}"

   configUpdate(configurationJSON, lvVersion)
   nipmGetInstalled()
   vipmGetInstalled(lvVersion)
   lvGetInstalledNISoftware(lvVersion)

   echo "Updating build number for ${componentName} (${lvVersion}) to ${buildNumber} in commonbuild-configuration repository."
   def commitMessage = "Updating ${componentName} for VeriStand ${lvVersion} to build number ${buildNumber}."
   bat "commonbuild\\resources\\configPush.bat \"$commitMessage\""

}
