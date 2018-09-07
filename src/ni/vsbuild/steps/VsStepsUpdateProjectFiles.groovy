package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class VsStepsUpdateProjectFiles extends LvStep {

    VsStepsUpdateProjectFiles(script, mapStep, lvVersion) {
        super(script, mapStep, lvVersion)
    }

    void executeStep(BuildConfiguration configuration) {
        script.vsStepsUpdateProjectFiles(configuration.packageInfo, lvVersion)
    }
}
