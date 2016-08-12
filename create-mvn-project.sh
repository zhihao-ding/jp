#!/bin/bash
# Created Time:    2016-07-15 03:42:59
# Modified Time:   2016-08-12 02:01:25
#set -e # 这告诉bash一但有任何一个语句返回非真的值，则退出bash。

if [ "$1" == "" ]; then
    echo "Useage: $0 <project_name>";
    exit 1;
fi

mvn archetype:generate \
    -DgroupId=cn.aliate \
    -DartifactId=$1 \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DinteractiveMode=false \
    -DarchetypeCatalog=internal 

# archetype-catalog.xml

