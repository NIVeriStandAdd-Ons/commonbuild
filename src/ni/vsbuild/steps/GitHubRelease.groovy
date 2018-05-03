package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class GitHubRelease extends AbstractStep {
   
   def releaseConfiguration
   def lvVersion
   
   GitHubRelease(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
      this.releaseConfiguration = mapStep
   }

   void executeStep(BuildConfiguration buildConfiguration) {
      script.githubRelease(releaseConfiguration, lvVersion)
   }
}
