import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(releaseBranches, lvVersion) {

   echo "Test release stage: $releaseBranches"

   def repo = getComponentParts()['repo']
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def globalReleaseBranches

   def buildLog = readProperties file: "build_log"
   def nipkgVersion = buildLog.get('PackageVersion')
   def nipkgName = buildLog.get('PackageName')
   def nipkgFileLoc = buildLog.get('PackageFileLoc')
   def nipkgFileName = buildLog.get('PackageFileName')
   def nipkgFilePath = "$nipkgFileLoc\\$nipkgFileName"
   
   def tagString = "${lvVersion}-${nipkgVersion}"
   def releaseName = "${nipkgName}_${nipkgVersion}"
   def description = "$releaseName built from branch $branch."

   configurationJsonFile = readJSON file: "configuration_${lvVersion}.json"
   configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
   def componentConfiguration = configurationMap.repositories.get(repo)

   if(componentConfiguration.containsKey('release_branches')) {
      globalReleaseBranches = componentConfiguration.get('release_branches')
      echo "Branches configured globally in commonbuild-configuration for release: $globalReleaseBranches"
   } else {
      echo "No branches configured globally in commonbuild-configuration for GitHub releases."
   }

   if(releaseBranches != null && releaseBranches.contains(branch) || (globalReleaseBranches != null && globalReleaseBranches.contains(branch))) {
      echo "Releasing branch \'${branch}\' at www.github.com\${org}\${repo}."
      if(branch == 'master') {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\""
      } else {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\" --pre-release"
      }
      bat "github-release upload --user $org --repo $repo --name \"${nipkgName}_version_manifest\" --tag $tagString --file version_manifest"
      bat "github-release upload --user $org --repo $repo --name \"${releaseName}_windows_x64.nipkg\" --tag $tagString --file \"${nipkgFilePath}\""
   }
   else {
      echo "Branch \'${branch}\' is not configured for release."
   }
}
