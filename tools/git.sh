list="Algorithm
backends
Webpage
comm_lib
Spider
TaobaoOpenPythonSDK
"
for repo in `echo $list`
do
    echo "handle repo $repo"
    #git clone git@github.com:luoyan/$repo.git
    cd $repo
    #git remote add Maimiaotech git@github.com:Maimiaotech/$repo.git
    #git pull Maimiaotech
    #git pull origin 2013:2013
    #git pull origin querydb:querydb
    #git checkout master
    #git checkout 2013
    #git checkout querydb
    git branch -a
    cd -
done
