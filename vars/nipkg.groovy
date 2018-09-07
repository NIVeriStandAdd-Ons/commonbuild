import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(packageDestination, version, stagingPathMap, lvVersion) {

   // Clone the commonbuild-configuration repository to workspace.
   // This repo contains build configuration info, e.g. current build number.
   cloneCommonbuildConfiguration(lvVersion)

   componentName = getComponentParts()['repo']
   componentBranch = getComponentParts()['branch']
   componentOrg = getComponentParts()['org']

   // Read the current build number from configuration file.
   // If successful getBuildNumber() increments the build number and saves it back to TOML file.
   def buildNumber = getBuildNumber(componentName, lvVersion)
   def paddedBuildNumber = "$buildNumber".padLeft(3,'0')

   // Use pre-release tags if building from develop or a feature branch.
   switch(componentBranch) {
      case 'master': flag = ""; break;
      case 'develop': flag = "-beta"; break;
      default: flag = "-alpha"; break;
   }
   def nipkgVersion = version+flag+"+${paddedBuildNumber}"

   // Used to replace {version} expressions with current VeriStand, LabVIEW and .nipkg versions being built.
   def replacementExpressionMap = ['labview_version': lvVersion, 'veristand_version': lvVersion, 'nipkg_version': nipkgVersion]
   def controlFileText = readFile "control" // Read .nipkg control file from workspace
   def instructionsExist = fileExists('instructions') // Check if .nipkg instructions file exists in workspace
   def instructionsFileText = ""
   if(instructionsExist) {
      instructionsFileText = readFile "instructions"
   }

   // Iterate over replacement expression map to replace regular expressions in .nipkg control and instructions files.
   replacementExpressionMap.each { replacementExpression, replacementValue ->
      controlFileText = controlFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
      if(instructionsExist) {
         instructionsFileText = instructionsFileText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
      }
   }

   // Read properties from .nipkg control file. These are used to create the package name.
   def controlFields = readProperties file: "control"
   def packageName = "${controlFields.get('Package')}".replaceAll("\\{veristand_version\\}", "${lvVersion}")
   def packageFilename = "${packageName}_${nipkgVersion}_windows_x64.nipkg"
   def packageFilePath = "${packageDestination}\\${packageFilename}"

   // Copy package payload source files to .nipkg staging directories.
   stagingPathMap.each { sourceDir, destDir ->
      sourceDir = sourceDir.replaceAll("\\{veristand_version\\}", "${lvVersion}")
      destDir = destDir.replaceAll("\\{veristand_version\\}", "${lvVersion}")
      bat "(robocopy \"${sourceDir}\" \"nipkg\\${packageName}\\data\\${destDir}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"
   }

   // Create required .nipkg control and instructions files using updated text.
   writeFile file: "nipkg\\${packageName}\\debian-binary", text: "2.0"
   writeFile file: "nipkg\\${packageName}\\control\\control", text: controlFileText
   if(instructionsExist) {
      writeFile file: "nipkg\\${packageName}\\data\\instructions", text: instructionsFileText
	}

   // Build nipkg using NI Package Manager CLI pack command.
   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "\"${nipmAppPath}\" pack \"nipkg\\${packageName}\" \"${packageDestination}\""

   // Write build properties to build properties file and build log.
   // build_properties: used by downstream build steps.
   // build_log: distributed with .nipkg to document build configuration
   ['build_properties','build_log'].each { logfile ->
      writeFile file: "$logfile", text: "PackageName: ${packageName}\nGitHub Organization: ${componentOrg}\nBranch: ${componentBranch}\nPackageFileName: ${packageFilename}\nPackageFileLoc: ${packageDestination}\nPackageVersion: ${nipkgVersion}\nPackageBuildNumber: $buildNumber\n"
   }

   // Invoke VI Package Manager API and NI Package Manager CLI to log installed packages to build_log.
   vipmGetInstalled(lvVersion)
   nipmGetInstalled()

   // If package is built successfully push the updated build configuration file back to commonbuild-configuration repository.
   configPush(buildNumber, componentName, lvVersion)

   return buildNumber
}
