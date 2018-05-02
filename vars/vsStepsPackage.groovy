import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(typesVersion, tsVersions, lvVersion) {

   def baseVersion = typesVersion
   def vsVersion = lvVersion
   if(vsVersion == "2015") {
      vsVersion = "2015sp1"
   }

   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def controlFileText = readFile "control"
   def instructionsFileText = readFile "instructions"
   
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

   def nipkgName = "veristand-${vsVersion}-steps-for-teststand"
   def programFilesStagingDirectory = "data\\ProgramFiles_32\\VeriStand Steps for TestStand"
   def replacementExpressionMap = ['veristand_version': vsVersion, 'nipkg_version': nipkgVersion] 
   def programFilesStagingSource = "built\\programFiles_32"
   def programFilesStagingDest = "nipkg\\${nipkgName}\\data\\programFiles_32"
   def documentsStagingSource = "built\\documents"
   def documentsStagingDest = "nipkg\\${nipkgName}\\data\\documents"
   def updatedControlText = controlFileText
   def packageFileName = "${nipkgName}-${nipkgVersion}_windows_x64.nipkg"
   def packageFilePath = "built\\$packageFileName"

   bat "(robocopy \"${programFilesStagingSource}\" \"${programFilesStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"
   bat "(robocopy \"${documentsStagingSource}\" \"${documentsStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   replacementExpressionMap.each { replacementExpression, replacementValue ->
      updatedControlText = updatedControlText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
   }

   dir("nipkg\\${nipkgName}"){
      writeFile file:'control\\control', text: updatedControlText
      writeFile file:'data\\instructions', text: instructionsFileText
      writeFile file:'debian-binary', text: "2.0"
   }

   bat "\"${nipmAppPath}\" pack \"$WORKSPACE\\nipkg\\$nipkgName\"  built"  

   writeFile file: "build_log", text: "PackageName: ${packageName}\nPackageFileName: ${packageFilename}\nPackageFileLoc: ${payloadDir}\nPackageVersion: ${nipkgVersion}"
        
   configUpdate(configurationJSON, lvVersion)
   nipmGetInstalled()
   vipmGetInstalled(lvVersion)
   lvGetInstalledNISoftware(lvVersion)

   echo "Updating build number for ${componentName} (${lvVersion}) to ${buildNumber} in commonbuild-configuration repository."
   def commitMessage = "Updating ${componentName} for VeriStand ${lvVersion} to build number ${buildNumber}."
   bat "commonbuild\\resources\\configPush.bat \"$commitMessage\""
}
