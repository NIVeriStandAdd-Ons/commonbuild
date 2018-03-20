@echo off

SET commitMessage=%1

CD commonbuild-configuration

git commit -m -a %commitMessage%
git pull origin master
git push --set-upstream origin master

@echo on