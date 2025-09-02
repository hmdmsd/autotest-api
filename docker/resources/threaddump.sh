#!/usr/bin/env bash

# Creation des repertoires et generation du nom de fichier
datetime=`date '+%Y-%m-%d_%H-%M-%S'`
folder_threaddump=extract/
file_threaddump="${folder_threaddump}threaddump-${datetime}"
PID=`pgrep java`

# On verifie que le fichier pid existe et que le process également puis on lance le jmap
if [ -n "${PID}" ]; then
  $(ps -p ${PID} >/dev/null)
  if [ "$?" -eq 0 ]; then
    echo "Lancement de la commande : jstack ${PID} > ${file_threaddump}"
    jstack ${PID} > ${file_threaddump}
    echo "Le fichier a ete genere dans ${file_threaddump}"
    echo "Merci de le supprimer une fois recupere"
  else
    echo "Impossible de realiser le threaddump: le processus ${PID} est introuvable"
  fi
else
  echo "Impossible de realiser le threaddump: le fichier PID ${PID_FILE} n'existe pas"
fi
