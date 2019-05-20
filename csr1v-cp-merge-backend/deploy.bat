echo off

rem this will read properties from deploy.properties file.
For /F "tokens=1* delims==" %%A IN (deploy.properties) DO (
	IF "%%A"=="cloudseer.server.path" set cloud_seer_path=%%B
	IF "%%A"=="solution.name" set solution_name=%%B
	)

IF exist %cloud_seer_path% (
rem echo Deploying solution %solution_name%
echo Removed solution %cloud_seer_path%/main-ui/solutions/%solution_name%

rem copy files from unzipped solution archive to cloudseer/web-inf/lib.
robocopy ./libs %cloud_seer_path%/web-inf/lib /E /XO *
echo Copied files from libs folder to %cloud_seer_path%/web-inf/lib /E /XO *

rem copy files from application main-ui to cloudseer.
robocopy ./main-ui %cloud_seer_path%/main-ui/solutions/%solution_name% /E /XO *
echo Copied files from main-ui folder to %cloud_seer_path%/main-ui/solutions/%solution_name% /E /XO *

rem copy files from unzipped solution archive to cloudseer/web-inf/lib.
robocopy ./resources %cloud_seer_path%/main-ui/solutions/%solution_name% /E /XO *
echo Copied files from resources folder to %cloud_seer_path%/main-ui/solutions/%solution_name% /E /XO *

echo Solution deployed successfully
)else (
   echo %cloud_seer_path% Path is invalid
)