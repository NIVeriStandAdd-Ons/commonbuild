def call(packageInfo, lvVersion){

   // UpdateProjectFiles.vi makes updates to several VeriStand Steps for TestStand project files depending on the version being built.
   echo "Updating project files."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\Source\\LV\\Build\\UpdateProjectFiles.vi\""

}
