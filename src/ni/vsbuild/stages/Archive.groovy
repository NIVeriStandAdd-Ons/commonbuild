package ni.vsbuild.stages

import ni.vsbuild.BuildConfiguration

class Archive extends AbstractStage {

   private String archiveLocation

   Archive(script, configuration, lvVersion) {
      super(script, 'Archive', configuration, lvVersion)
   }

   void executeStage() {
      setArchiveLocation()

      script.echo "Archiving build to $archiveLocation"
      def buildOutputDir = configuration.archive.get('build_output_dir')
      def currentBuildNumber = script.currentBuild.number

      if(script.fileExists(BuildConfiguration.STAGING_DIR)) {
         buildOutputDir = BuildConfiguration.STAGING_DIR
      }

      // If configured for GitHub build number tracking then get build number from  configuration file instead of Jenkins.
      if(configuration.release.steps.type=='githubRelease') {
         def buildProperties = readProperties file: "build_properties"
         currentBuildNumber = buildProperties.get('Package Build Number')
      }

      script.bat "(robocopy \"$buildOutputDir\" \"$archiveLocation\\$lvVersion\" /s) ^& IF %ERRORLEVEL% LSS 8 SET ERRORLEVEL=0"

      setArchiveVar()
   }

   // Builds a string of the form <archiveLocation>\\export\\<branch>\\<build_number>
   private void setArchiveLocation() {
      archiveLocation = configuration.archive.get('archive_location') +
                "\\export\\${script.env.BRANCH_NAME}\\" +
                "Build ${currentBuildNumber}"
   }

   // Set an env var that points to the archive so dependents can find it
   private void setArchiveVar() {
      def component = script.getComponentParts()['repo']
      def depDir = "${component}_DEP_DIR"
      script.env."$depDir" = archiveLocation
   }
}
