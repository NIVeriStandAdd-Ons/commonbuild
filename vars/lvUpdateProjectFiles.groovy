def call(packageInfo, lvVersion){

   echo "Updating project files."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\Source\\LV\\Build\\UpdateProjectFiles.vi\""

}
