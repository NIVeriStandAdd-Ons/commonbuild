import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(releaseConfiguration, lvVersion) {

   def repo = getComponentParts()['repo']
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def releaseBranchesKey = "${lvVersion}_release_branches"
   def releaseBranches = releaseConfiguration.find{ it.key =="$releaseBranchesKey" }?.value
   def globalReleaseBranches

   def buildLog = readProperties file: "build_properties"
   def packageVersion = buildLog.get('PackageVersion')
   def packageName = buildLog.get('PackageName')
   def packageFileLoc = buildLog.get('PackageFileLoc')
   def packageFileName = buildLog.get('PackageFileName')
   def packageFilePath = "$packageFileLoc\\$packageFileName"
   
   def tagString = "${lvVersion}-${packageVersion}"
   def releaseName = "${packageName}_${packageVersion}"
   def description = "$releaseName built from branch $branch."

   configurationJsonFile = readJSON file: "configuration_${lvVersion}.json"
   configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
   def componentConfiguration = configurationMap.repositories.get(repo)

   if(componentConfiguration.containsKey('release_branches')) {
      globalReleaseBranches = componentConfiguration.get('release_branches')
      echo "Branches configured in commonbuild-configuration for release: $globalReleaseBranches"
   } else {
      echo "No branches configured in commonbuild-configuration for GitHub releases."
   }

   if(releaseBranches != null && releaseBranches.contains(branch) || (globalReleaseBranches != null && globalReleaseBranches.contains(branch))) {
      echo "Releasing branch \'${branch}\' at www.github.com\${org}\${repo}."
      if(branch == 'master') {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\""
      } else {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\" --pre-release"
      }
      bat "github-release upload --user $org --repo $repo --name \"${releaseName}_version_manifest\" --tag $tagString --file build_log"
      bat "github-release upload --user $org --repo $repo --name \"${packageFileName}\" --tag $tagString --file \"${packageFilePath}\""
   }
   else {
      echo "Branch \'${branch}\' is not configured for release."
   }
}
