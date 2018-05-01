def call(releaseBranches, lvVersion) {

   echo "Test release stage: $releaseBranches"

   def repo = getComponentParts()['repo']
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['org']

   def buildLog = readProperties file: "build_log"
   def nipkgVersion = buildLog.get('Package Version')
   def nipkgName = buildLog.get('Package Name')
   def nipkgPath = buildLog.get('Package File Path')
   
   def tagString = "${lvVersion}-${nipkgVersion}"
   def releaseName = "${nipkgName}_${nipkgVersion}"
   def description = "$releaseName built from branch $branch."

   if(releaseBranches != null && releaseBranches.contains(branch)) {
      echo "Releasing branch \'${branch}\' at www.github.com\${org}\${repo}."
      if(branch == 'master') {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\""
      } else {
         bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\" --pre-release"
      }
      bat "github-release upload --user $org --repo $repo --name \"${releaseName}_windows_x64.nipkg\" --tag $tagString --file version_manifest"
      bat "github-release upload --user $org --repo $repo --name \"${releaseName}_windows_x64.nipkg\" --tag $tagString --file \"${nipkgPath}\""
   }
   else {
      echo "Branch \'${branch}\' is not configured for release."
   }
}
