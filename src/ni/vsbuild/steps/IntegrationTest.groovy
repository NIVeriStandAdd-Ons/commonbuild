package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class IntegrationTest extends AbstractStep {

   def seqPath
   def tsVersion
   def includeTestStand64
   def lvVersion

   IntegrationTest(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.seqPath = mapStep.get('sequence_path')
      this.tsVersion = mapStep.get('teststand_version')
      this.includeTestStand64 = mapstep.get('include_ts_64')
      this.lvVersion = lvVersion
   }

   void executeStep(BuildConfiguration buildConfiguration) {
      script.integrationTest(seqPath, tsVersion, includeTestStand64)
   }
}
