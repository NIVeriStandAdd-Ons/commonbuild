import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(packageDestination, version, stagingPathMap, lvVersion) {

   cloneCommonbuildConfiguration(lvVersion)

   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']

   def buildNumber = getBuildNumber(componentName, lvVersion)
   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   switch(componentBranch) {
      case 'master': flag = ""; break;
      case 'develop': flag = "-beta"; break;
      default: flag = "-alpha"; break;
   }
   def nipkgVersion = version+flag+"+${paddedBuildNumber}"

   // Replace {version} expressions with current VeriStand and .nipkg versions being built.
   def replacementExpressionMap = ['labview_version': lvVersion, 'veristand_version': lvVersion, 'nipkg_version': nipkgVersion]
   def controlFileText = readFile "control"
   def instructionsFileText = readFile "instructions"

   replacementExpressionMap.each { replacementExpression, replacementValue ->
      controlFileText = controlFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
      if(fileExists('instructions')) {
         instructionsFileText = instructionsFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
      }
   }

   // Read PROPERTIES from .nipkg control file.
   def controlFields = readProperties file: "control"
   def basePackageName = "${controlFields.get('Package')}"
   def packageName = basePackageName.replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageFilename = "${packageName}_${nipkgVersion}_windows_x64.nipkg"
   def packageFilePath = "${packageDestination}\\${packageFilename}"

   // Copy package source files to package to nipkg staging directories after evaluating expressions.
   stagingPathMap.each { sourceDir, destDir ->
      sourceDir = sourceDir.replaceAll("\\{veristand_version\\}", "${lvVersion}")
      destDir = destDir.replaceAll("\\{veristand_version\\}", "${lvVersion}")
      bat "(robocopy \"${sourceDir}\" \"nipkg\\${packageName}\\data\\${destDir}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"
   }

   // Create .nipkg source files.
   writeFile file: "nipkg\\${packageName}\\debian-binary", text: "2.0"
   writeFile file: "nipkg\\${packageName}\\control\\control", text: controlFileText
   if(fileExists('instructions')) {
      writeFile file: "nipkg\\${packageName}\\data\\instructions", text: instructionsFileText
	}

   // Build nipkg using NI Package Manager CLI pack command.
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "\"${nipmAppPath}\" pack \"nipkg\\${packageName}\" \"${packageDestination}\""

   // Write build properties to properties file and build log.
   ['build_properties','build_log'].each { logfile ->
      writeFile file: "$logfile", text: "PackageName: ${packageName}\nPackageFileName: ${packageFilename}\nPackageFileLoc: ${packageDestination}\nPackageVersion: ${nipkgVersion}\nPackageBuildNumber: $buildNumber\n"
   }

   vipmGetInstalled(lvVersion)
   nipmGetInstalled()
   configPush(buildNumber, componentName, lvVersion)

   return buildNumber
}
