import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(typesVersion, tsVersions, payloadDir, lvVersion) {

   def baseVersion = typesVersion
   def vsVersion = lvVersion
   def buildNumber = 0
   if(vsVersion == "2015") {
      vsVersion = "2015sp1"
   }

   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   buildNumber = getBuildNumber(componentName, lvVersion)
   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   def controlFields = readProperties file: "control"

   switch(componentBranch) {
      case 'master': nipkgVersion = baseVersion+"+$paddedBuildNumber"; break;
      case 'develop': nipkgVersion = baseVersion+"-beta+$paddedBuildNumber"; break;
      default: nipkgVersion = baseVersion+"-alpha+$paddedBuildNumber"; break;
   }

   def basePackageName = "${controlFields.get('Package')}"
   def packageName = basePackageName.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def programFilesStagingSource = "built\\programFiles_32"
   def programFilesStagingDest = "nipkg\\${packageName}\\data\\programFiles_32"
   def documentsStagingSource = "built\\documents"
   def documentsStagingDest = "nipkg\\${packageName}\\data\\documents"
   def packageFileName = "${packageName}_${nipkgVersion}_windows_x64.nipkg"

   bat "(robocopy \"${programFilesStagingSource}\" \"${programFilesStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"
   bat "(robocopy \"${documentsStagingSource}\" \"${documentsStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   def replacementExpressionMap = ['veristand_version': vsVersion, 'nipkg_version': nipkgVersion]
   def controlFileText = readFile "control"
   def instructionsFileText = readFile "instructions"
   replacementExpressionMap.each { replacementExpression, replacementValue ->
      controlFileText = controlFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
      instructionsFileText = instructionsFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
   }

   dir("nipkg\\${packageName}"){
      writeFile file:'control\\control', text: controlFileText
      writeFile file:'data\\instructions', text: instructionsFileText
      writeFile file:'debian-binary', text: "2.0"
   }

   bat "\"${nipmAppPath}\" pack \"$WORKSPACE\\nipkg\\$packageName\"  built"

   writeFile file: "build_properties", text: "PackageName: ${packageName}\nPackageFileName: ${packageFileName}\nPackageFileLoc: ${payloadDir}\nPackageVersion: ${nipkgVersion}\nBuildNumber: ${buildNumber}\n"

   vipmGetInstalled(lvVersion)
   nipmGetInstalled()

   echo "Updating build number for ${componentName} (${lvVersion}) to ${buildNumber} in commonbuild-configuration repository."
   def commitMessage = "Updating ${componentName} for VeriStand ${lvVersion} to build number ${buildNumber}."
   bat "commonbuild\\resources\\configPush.bat \"$commitMessage\""

   return buildNumber

}
