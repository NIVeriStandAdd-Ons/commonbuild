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

      def componentConfigJsonFile = script.readJSON file: 'configuration.json'
      def componentConfigStringMap = new JsonSlurperClassic().parseText(componentConfigJsonFile.toString())
      
      def repo = script.getComponentParts()['repo']
      def branch = script.getComponentParts()['branch']

      def componentID = repo+'-'+branch
      script.echo "Getting build version number for ${componentID}."
      
      def componentConfig = componentConfigStringMap.repositories.get(componentID)
      script.echo "$componentConfig"

      def buildNumber = componentConfig.get('build')
      script.echo "$buildNumber"

      componentConfig << [build:5]
      script.echo "$componentConfig"

      componentConfigStringMap.repositories << [componentID:componentConfig]
      script.echo "$componentConfigStringMap"

      def newComponentConfigFile = new File('configuration.txt')
      newComponentConfigFile.write '$componentConfigStringMap'

      script.buildNipkg(payloadDir, releaseVersion, stagingPath, devXmlPath, lvVersion)
      script.echo packageInfo
   }
}

