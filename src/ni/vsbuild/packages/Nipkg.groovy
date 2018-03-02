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

      def globalBuildConfigJsonFile = script.readJSON file: 'configuration.json'

      def convertedConfigJson = new JsonSlurperClassic().parseText(globalBuildConfigJsonFile.toString())
      def projectConfig = convertedConfigJson.repositories.get('scan_engine_cd-master')

      script.echo projectConfig[0]
      script.echo projectConfig.getAt('minor')
      script.echo projectConfig.get('patch')
      script.echo projectConfig.get('build')

      script.buildNipkg(payloadDir, releaseVersion, stagingPath, devXmlPath, lvVersion)
      script.echo packageInfo
   }
}

