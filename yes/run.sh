#!/bin/bash
j_flag='false';
o_flag='app';
file=$1
while getopts "jo:" flag
do
    case "${flag}" in
        j) j_flag='true';;
        o) o_flag=${OPTARG};;
    esac
done
if [ $j_flag = 'true' ] || [ ! -d ./compiler ];
then
    mvn package;
    mkdir compiler
    mv target/yes-1.0-SNAPSHOT.jar compiler;
    mv target/libs compiler
fi
java -jar ./compiler/yes-1.0-SNAPSHOT.jar $file
gcc out.c -o $o_flag
./$o_flag
