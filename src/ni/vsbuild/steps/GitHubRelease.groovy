package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class GitHubRelease extends AbstractStep {
   
   GitHubRelease(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.releaseBranches = mapStep.get('release_branches')
   }

   void executeStep(BuildConfiguration buildConfiguration) {
      script.githubRelease()
   }
}
