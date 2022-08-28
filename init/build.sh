#!/bin/bash

model="initd"

echo "Set Env..."

function setenv() {
  case $1 in
  n1 | N1)
    #export TARGET=root@192.168.1.100:/opt/$model
    export GOOS=linux
    export GOARCH=arm64
    export CGO_ENABLED=1
    export CC=/usr/bin/aarch64-linux-gnu-gcc
    export CXX=/usr/bin/aarch64-linux-gnu-g++
    echo "set env of N1!"
    ;;
  wky)
    #export TARGET=root@192.168.1.10:/opt/$model
    export GOOS=linux
    export GOARCH=arm
    export GOARM=7
    echo "set env of wky!"
    ;;
  k3)
    #export TARGET=root@192.168.10.1:/opt/$model
    export GOOS=linux
    export GOARM=5
    export GOARCH=arm
    ;;
  *)
    echo "Other command!"
    ;;
  esac
  return
}

setenv "$1"

echo "Clear..."
rm -rf $model
rm -rf deploy

echo "Building..."
go build -o $model -ldflags "-s -w"

echo "Compression by upx..."
#upx --brute $model

echo "Make directory..."
mkdir -p deploy

echo "Copy file to deploy..."
mv $model deploy
cp -rf conf start.sh deploy
tar -zvcf deploy.tgz deploy/*
mv deploy.tgz deploy

echo "Building Complete..."

if [ $TARGET ]; then
  scp -r deploy/* $TARGET
fi
