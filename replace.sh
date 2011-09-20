#!/bin/bash


#echo "Please enter the text you want to replace: "

#read textToReplace

echo "Please enter the package name for this app (ie. com.xtremelabs.app.<packagename_here>): "

read replacementText

####################################
#REPLACE APP_PACKAGENAME


for NAME in $(grep -i -R -l "barebone_changename" */*)

do

	echo "FOUND barebone_changename IN FILE: "$NAME
	sed -i -b s/barebone_changename/$replacementText/gi $NAME
done

for FILENAME in $(find . -iname "*barebone_changename*")

do

	echo "FOUND barebone_changename IN FILENAME:" $FILENAME
	mv $FILENAME $(echo $FILENAME | sed s/barebone_changename/$replacementText/gi)
done



#App Name
echo "Please enter the name of this app (ie. will appear as <AppNameHere>Application): "

read replacementText

###########################################
#REPLACE APP NAME

for NAME in $(grep -i -R -l "BareBone" */*)

do

	echo "FOUND BareBone IN FILE: "$NAME
	sed -i -b s/BareBone/$replacementText/gi $NAME
done

for NAME in $(grep -i -R -l "BareBone" */*)

do

	echo "FOUND BareBone IN FILE: "$NAME
	sed -i -b s/BareBone/$replacementText/gi $NAME
done


for FILENAME in $(find . -iname "*BareBone*")

do

	echo "FOUND BareBone IN FILENAME:" $FILENAME
	mv $FILENAME $(echo $FILENAME | sed s/BareBone/$replacementText/gi)
done



#########################################
#COMPANY NAME
echo "Please enter the client company package name (ie. com.<companynamehere>.app....): "

read replacementText

#REPLACE COMPANY NAME
set textToReplace = $"changeme"

for NAME in $(grep -i -R -l "changeme" */*)

do

	echo "FOUND changeme IN FILE: "$NAME
	sed -i -b s/changeme/$replacementText/gi $NAME
done

for FILENAME in $(find . -iname "*changeme*")

do

	echo "FOUND changeme IN FILENAME:" $FILENAME
	mv $FILENAME $(echo $FILENAME | sed s/changeme/$replacementText/gi)
done