@echo on

SET commitMessage=%1

CD commonbuild-configuration

git commit -a -m %commitMessage%

:Wait
TIMEOUT /T 10
git pull origin master

git push --set-upstream origin master && (
   echo git push was successful
) || (
   echo trying git pull again after wait
   GOTO Wait
)

@echo on
