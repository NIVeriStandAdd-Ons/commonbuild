def call(path, lvVersion) {
        echo "Build the package at ${path}"
        bat "labview-cli --kill commonbuild\lv\vipm\vipbBuild.vi -- \"${path}\" \"build_temp\" \"${WORKSPACE}\""  
}
