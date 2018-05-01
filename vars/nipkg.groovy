import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(payloadDir, devXmlPath, stagingPath, lvVersion) {
   
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def nipkgVersion
   def buildNumber
   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   // Read PROPERTIES from .nipkg control file.
   def controlFields = readProperties file: "control"
   def basePackageName = "${controlFields.get('Package')}"

   // Read TEXT from .nipkg control file 
   def controlFileText = readFile "control"
   
   switch(componentBranch) {
      case 'master': nipkgVersion = baseVersion+"+$paddedBuildNumber"; break;
      case 'develop': nipkgVersion = baseVersion+"-beta+$paddedBuildNumber"; break;
      default: nipkgVersion = baseVersion+"-alpha+$paddedBuildNumber"; break;
   }

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
   
   writeFile file: "build_log", text: "Package Name: ${packageName}\nPackage File Name: ${packageFilename}\nPackage File Path: ${packageFilePath}\nPackage Version: ${nipkgVersion}"

   configUpdate(configurationJSON, lvVersion)
   vipmGetInstalled(lvVersion)
   lvGetInstalledNISoftware(lvVersion)
   nipmGetInstalled()

}
