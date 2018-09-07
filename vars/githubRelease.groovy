import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(releaseConfiguration, lvVersion) {

   def repo = getComponentParts()['repo']
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']

   // Look for the '201x_release_branches' in the build configuration file.
   // The .nipkg will only be released to GitHub if this property is found and contains the current branch.
   def releaseBranchesKey = "${lvVersion}_release_branches"
   def releaseBranches = releaseConfiguration.find{ it.key =="$releaseBranchesKey" }?.value

   // Read package information from build properties file.
   def buildLog = readProperties file: "build_properties"
   def packageFileLoc = buildLog.get('PackageFileLoc')
   def packageFileName = buildLog.get('PackageFileName')
   def packageFilePath = "$packageFileLoc\\$packageFileName"

   // Build release information strings.
   // This script uses the github-relese utility.
   // github-release: https://github.com/aktau/github-release
   // github-release must be added to each build node's PATH environment variable.
   // github-release uses the GITHUB_TOKEN environment variable on each node as a GitHub credential.
   // GITHUB_TOKEN variable must be set manually.
   def tag = "${lvVersion}-${buildLog.get('PackageVersion')}"
   def releaseName = "${buildLog.get('PackageName')}_${buildLog.get('PackageVersion')}"
   def description = "$releaseName built from branch $branch."
   def createReleaseCommand = "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tag --description \"${description}\""
   if(branch != 'master') {
     createReleaseCommand = createReleaseCommand + " --pre-release"
   }

   // For now we're just uploading the .nipkg file and build log to the GitHub release.
   def uploadsMap = ["${releaseName}_version_manifest": 'build_log', "${packageFileName}": packageFilePath]

   if(releaseBranches != null && releaseBranches.contains(branch)) {
      echo "Releasing branch \'${branch}\' at www.github.com/${org}/${repo}."
      bat "${createReleaseCommand}"
      uploadsMap.each { uploadName, uploadSrc ->
        bat "github-release upload --user $org --repo $repo --tag $tag --name \"${uploadName}\" --file \"${uploadSrc}\""
      }
   } else {
      echo "Branch \'${branch}\' is not configured for release."
   }
}
