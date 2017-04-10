#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 file.boa output-dir/ [options]"
    $(dirname "$0")/boa.sh -e
    exit -1
fi

if [ ! -f $1 ]; then
    echo "input '$1' is not a file"
    echo "Usage: $0 file.boa output-dir/ [options]"
    $(dirname "$0")/boa.sh -e
    exit -2
fi

$(dirname "$0")/boa.sh -e -d dataset/ -i $1 -o $2 $*
