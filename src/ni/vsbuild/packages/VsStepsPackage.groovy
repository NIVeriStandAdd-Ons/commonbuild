package ni.vsbuild.packages

class VsStepsPackage extends AbstractPackage {

   def typesVersion
   def packageDestination

   VsStepsPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.typesVersion = packageInfo.get('types_version')
      this.packageDestination = payloadDir
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Step Types Version: $typesVersion
         """.stripIndent()

      script.echo packageInfo
      def stagingPathMap = ['built\\programFiles_32':'programFiles_32', 'built\\documents': 'documents']
      script.currentBuild.displayName = "$lvVersion\\#" + script.vsStepsPackage(typesVersion, stagingPathMap, packageDestination, lvVersion)
   }
}
