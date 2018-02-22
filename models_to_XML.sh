#!/bin/bash
for filename in $1/*.$2; do
  echo "<string name=\"model_"$2"_"$(basename $filename "."$2)\"">"$(basename $filename)"</string>"
done
echo "<array name=\"models_"$2\"">"
for filename in $1/*.$2; do
  echo "    <item>@string/model_"$2"_"$(basename $filename "."$2)"</item>"
done
echo "</array>"

