#!/usr/bin/env bash

# Creation des repertoires et generation du nom de fichier
datetime=`date '+%Y-%m-%d_%H-%M-%S'`
folder_heapdump=extract/
file_heapdump="${folder_heapdump}heapdump-${datetime}"
PID=`pgrep java`

# On verifie que le fichier pid existe et que le process également puis on lance le jmap
if [ -n "${PID}" ]; then
  $(ps -p ${PID} >/dev/null)
  if [ "$?" -eq 0 ]; then
    echo "Lancement de la commande : jmap -dump:format=b,file=${file_heapdump} ${PID}"
    jmap -dump:format=b,file=${file_heapdump} ${PID}
    echo "Le fichier a ete genere dans ${file_heapdump}"
    echo "Merci de le supprimer une fois recupere"
  else
    echo "Impossible de realiser le heapdump: le processus ${PID} est introuvable"
  fi
else
  echo "Impossible de realiser le heapdump: le fichier PID ${PID_FILE} n'existe pas"
fi
