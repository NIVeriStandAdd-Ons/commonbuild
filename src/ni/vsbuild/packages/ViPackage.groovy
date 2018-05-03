package ni.vsbuild.packages

class ViPackage extends AbstractPackage {

   def vipbPath
   def outputPath

   VsStepsPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.vipbPath = packageInfo.get('vipb_path')
      this.outputPath = packageInfo.get('output_path')
   }
}
