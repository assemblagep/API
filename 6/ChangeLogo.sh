#!/bin/bash
echo "Copy logo"

if [[ $(date +%u) -gt 5 ]] ; then
 cp Weekend/logo.jpeg Target/logo.jpeg
else
 cp BusinessDays/logo.jpeg Target/logo.jpeg 
fi


