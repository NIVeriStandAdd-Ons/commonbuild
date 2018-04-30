package ni.vsbuild.stages

class Release extends AbstractStepStage {

   Release(script, configuration, lvVersion) {
      super(script, 'Test', configuration, lvVersion)
   }

   void executeStage() {
      executeSteps(configuration.release)
   }
}
