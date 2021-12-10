# Prodotto
Jcomedilib 

# Descrizione del prodotto
Interfaccia Java per la libreria COMEDI.
Questa interfaccia è utilizzata all'interno della componente periferico per la connessione agli analizzatori collegati alle schede A/D.

# Configurazioni iniziali 
## Inizializzazione delle schede di IO tramite comedi

Il comando `comedi_config` serve per effettuare il binding tra i device comedi e le schede di IO.
Esempio:  

```
comedi_config /dev/comedi0 pci1710hg
```

Prima di eseguire il binding occorre che il modulo kernel comedi e i moduli kernel delle schede che si intendono usare siano caricati.
Pertanto si puo' procedere nei modi seguenti:
* caricare i moduli necessari con modprobe, prima di eseguire comedi_config (vedere script `advantechstart.sh`)
* configurare in modo opportuno modprobe.conf (vedere l'esempio `modprobe.conf`)


# Getting Started 
Sostituire nel file `buildfiles/Makefile` il path del proprio JDK sostituendo il valore della variabile `JAVA_HOME`:

```
JAVA_HOME = /usr/lib/jvm/java-8-oracle

```


Eseguire poi il target **release** di ANT (tramite OpenJDK 1.8) su una macchina Linux su cui e' necessario che sia installato il comando `make`.

# Prerequisiti di sistema 
Fare riferimento al file BOM.csv per verificare l'elenco delle librerie esterne utilizzate in questo software.

# Versioning
Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

# Authors
La lista delle persone che hanno partecipato alla realizzazione del software sono  elencate nel file AUTHORS.txt.

# Copyrights
L'elenco dei titolari del software sono indicati nel file Copyrights.txt

# License 
SPDX-License-Identifier: LGPL-2.1

Vedere il file LICENSE per i dettagli.