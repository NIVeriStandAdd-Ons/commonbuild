package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class GitHubRelease extends AbstractStep {
   
   def releaseBranches
   def lvVersion
   
   GitHubRelease(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
      this.releaseBranches = mapStep.get('release_branches')
   }

   void executeStep(BuildConfiguration buildConfiguration) {
      script.githubRelease(releaseBranches, lvVersion)
   }
}
