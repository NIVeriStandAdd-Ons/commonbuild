@echo off
rem This batch file uses the Git CLI to push the updated build configuration files
rem back to the commonbuild-configuration repository.
rem This may be running at the same time as another node which uses the same configuration repositry.
rem Therefore if the Push is unsuccesful then wait and perform a Pull before re-trying.

SET commitMessage=%1

CD commonbuild-configuration

git commit -a -m %commitMessage%

:Wait
ping 127.0.0.1 -n 6 > nul

git pull origin master
git push --set-upstream origin master && (
   echo git push was successful
) || (
   echo trying git pull again after wait
   GOTO Wait
)

@echo on
