package ni.vsbuild.packages

class ViPackage extends AbstractPackage {

   def vipbPath
   def outputPath

   ViPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.vipbPath = packageInfo.get('vipb_path')
      this.outputPath = packageInfo.get('output_path')
   }

   void buildPackage(lvVersion) {
 
      def packageInfo = """
         Building package $name from $payloadDir
         LabVIEW/VeriStand version: $lvVersion
         VIPB Path: $vipbPath
         Output Path: $outputPath
         """.stripIndent()

      script.viPackage(vipbPath, outputPath, lvVersion)

   }
}
