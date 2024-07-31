#!/bin/bash

name="arch"

function is_arm() {
  local goarch=$1
  local goarm=$2

  if [[ "$goarch" == "arm"* ]] && [[ "$goarm" =~ ^[5-7]$ ]]; then
    return 0
  fi

  return 1
}

function setenv() {
  local goos=$1
  local goarch=$2
  local goarm=$3

  export GOOS="$goos"
  export GOARCH="$goarch"

  if is_arm "$goarch" "$goarm"; then
    export GOARM="$goarm"
  else
    unset GOARM
  fi
}

function build() {
  setenv "$@"

  local goos=$1
  local goarch=$2
  local goarm=$3

  if is_arm "$goarch" "$goarm"; then
    goarm="-arm$goarm"
  else
    goarm=""
  fi

  go build -o "$name-$goos-$goarch$goarm"
}

build linux arm 5
build linux arm 6
build linux arm 7
build linux arm64 5
build linux arm64 6
build linux arm64 7

build linux mips
build linux mips64
build linux mipsle
build linux mips64le
