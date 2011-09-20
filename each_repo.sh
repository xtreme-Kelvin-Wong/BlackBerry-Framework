#!/bin/bash
ROOT=`dirname $_`
GE=$ROOT/.git_externals

pushd $GE/src/com/xtremelabs/xtremeutil &&
  $* ;
  popd

pushd $GE/src/rimunit &&
  $* ;
  popd

pushd $GE/test/com/xtremelabs/xtremeutil &&
  $* ;
  popd

pushd $GE/test/rimunit &&
  $* ;
  popd

pushd $ROOT &&
  $* ;
  popd

