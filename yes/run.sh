#!/bin/bash
#RUN IN ./YES/yes directory
j_flag='false';
o_flag='app';
r_flag='false';
file=$1
while getopts "jo:" flag
do
    # shellcheck disable=SC2220
    case "${flag}" in
        j) j_flag='true';;
        o) o_flag=${OPTARG};;
        r) r_flag='true';;
    esac
done
if [ $j_flag = 'true' ] || [ ! -d ./compiler ];
then
    mvn package;
    mkdir compiler;
    mv target/yes-1.0-SNAPSHOT.jar compiler;
    mv target/libs compiler;
fi
cp -r ../std compiler
java -jar ./compiler/yes-1.0-SNAPSHOT.jar $file ./compiler/std
gcc out.c -std=gnu99 -o $o_flag
if [ $r_flag = 'true' ]
then
    ./$o_flag
f