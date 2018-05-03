package ni.vsbuild.packages

class VsStepsPackage extends AbstractPackage {

   def typesVersion
   def tsVersions

   VsStepsPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.typesVersion = packageInfo.get('types_version')
      this.tsVersions = packageInfo.get('teststand_versions')
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Step Types Version: $typesVersion
         TestStand versions: $tsVersions
         """.stripIndent()

      script.echo packageInfo
      script.vsStepsPackage(typesVersion, tsVersions, lvVersion)
   }
}
