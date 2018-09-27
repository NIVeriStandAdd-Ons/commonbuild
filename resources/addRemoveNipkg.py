import os
import sys
import subprocess

operation = sys.argv[1]
currentDir = os.getcwd()
if len(sys.argv) > 2:
	nipkgPath = sys.argv[2]
	nipkgRelPath = currentDir + "\\" + nipkgPath

nipmAppPath = os.environ["ProgramW6432"]+"\\National Instruments\\NI Package Manager\\nipkg.exe"

if operation == 'install' or operation == 'uninstall':
	try:
		print(operation, " .nipkg file:\n", nipkgPath)
		print(f'\'{nipkgRelPath}\'')
		subprocess.run([nipmAppPath, operation, "-y", nipkgRelPath], stderr=subprocess.STDOUT, shell=True, check=True)

	except subprocess.CalledProcessError as err:
		print(err.args)
		sys.exit(err.args[0])

if operation == 'list-installed':
	file = open("build_log", "a")
	file.write("\n\n NI Packages Installed:\n\n")
	try:
		proc = subprocess.Popen([nipmAppPath, operation], stderr=subprocess.STDOUT,  stdout=subprocess.PIPE, shell=True)
		output = proc.stdout.read().decode("utf-8")
		file.write(output)

	except subprocess.CalledProcessError as err:
		print(err.args)
		sys.exit(err.args[0])
