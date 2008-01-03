#!/bin/bash

export OGLE_OSS_RESET_BUG=1

LIMMA_HOME=`dirname $0`

cd $LIMMA_HOME

LIBS=`find lib -iname *.jar | tr '\n' ':'`

echo "------ Starting limma ------"
echo "LIMMA_HOME=${LIMMA_HOME}"
echo "LIBS=${LIBS}"


java -Xmx256m -Xms64m -classpath $LIBS limma.Main
