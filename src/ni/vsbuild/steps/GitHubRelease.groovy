package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class GitHubRelease extends AbstractStep {

   def releaseBranches = [:]
   def currentBranch
   
   GitHubRelease(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.releaseBranches = mapStep.get('release_branches')
      this.currentBranch = getComponentParts()['branch']
   }

   void executeStep(BuildConfiguration buildConfiguration) {
      script.githubRelease(currentBranch, releaseBranches)
   }
}
