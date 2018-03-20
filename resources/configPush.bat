@echo off

SET commitMessage= "%1"

CD commonbuild-configuration

git commit -a -m "%commitMessage%"
git pull origin master
git push --set-upstream origin master

@echo on