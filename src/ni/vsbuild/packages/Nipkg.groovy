package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic

class Nipkg extends AbstractPackage {

   def releaseVersion
   def stagingPath
   def devXmlPath
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.releaseVersion = packageInfo.get('release_version')
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Package version: $releaseVersion
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         """.stripIndent()
      
      script.cloneCommonbuildConfiguration()
      script.configSetup()

      def versionConfigJsonFile = script.readJSON file: 'configuration.json'
      def convertedVersionConfigJson = new JsonSlurperClassic().parseText(versionConfigJsonFile.toString())
      
      def repo = script.getComponentParts()['repo']
      def branch = script.getComponentParts()['branch']

      def componentID = "$repo-$branch"
      script.echo "Getting build version number for ${componentID}."
      
      def versionConfig = convertedVersionConfigJson.repositories.get('$repo-$branch')
      script.echo "$versionConfig"
      //def buildNumber = versionConfig.get('build')
      //script.echo "$buildNumber"


      script.buildNipkg(payloadDir, releaseVersion, stagingPath, devXmlPath, lvVersion)
      script.echo packageInfo
   }
}

