package ni.vsbuild.stages

class Release extends AbstractStepStage {

   Release(script, configuration, lvVersion) {
      super(script, 'Release', configuration, lvVersion)
   }

   void executeStage() {
      executeSteps(configuration.release)
   }
}
