#/bin/sh
file="./deploy.properties"

if [ -f "$file" ]
then
  
  while IFS='=' read -r key value
  do
    key=$(echo $key | tr '.' '_')
    eval ${key}=\${value}
  done < "$file"
  
else
  echo "$file not found."
fi

if [ -d "${cloudseer_server_path}" ]; then
echo " Deploying solution ${solution_name}"

rm -rf ${cloudseer_server_path}/main-ui/solutions/${solution_name}
echo Removed solution ${cloudseer_server_path}/main-ui/solutions/${solution_name}
rsync -a ./libs/* ${cloudseer_server_path}/WEB-INF/lib/
echo Copied files from libs folder to ${cloudseer_server_path}/WEB-INF/lib/
rsync -a ./main-ui/* ${cloudseer_server_path}/main-ui/solutions/${solution_name}/
echo Copied files from main-ui folder to ${cloudseer_server_path}/main-ui/solutions/${solution_name}/
rsync -a ./resources/* ${cloudseer_server_path}/main-ui/solutions/${solution_name}/
echo Copied files from resources folder to ${cloudseer_server_path}/main-ui/solutions/${solution_name}/

echo Solution deployed successfully

else
  echo Path is invalid ${cloudseer_server_path} 
 
fi  