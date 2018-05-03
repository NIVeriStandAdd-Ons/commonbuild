def call(vipbPath, outputPath, lvVersion) {
        echo "Build the package at ${vipbPath}"
        bat "labview-cli --kill commonbuild\\lv\\vipm\\vipbBuild.vi -- \"${vipbPath}\" \"${outputPath}\" \"${WORKSPACE}\""  
}
