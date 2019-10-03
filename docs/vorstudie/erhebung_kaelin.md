# Gesräch Thomas Kälin

## Wichtige Punkte

### Entry Point Login Link generieren

Nach dem Aufsetzen einer Instanz soll ein Liste von Links für jede Instanz generiert werden.
Diese soll dem Lehrer dazu dienen die Instanzen den Schülern zu zu teilen.

### Auflistung aller Installationen

Es sollen alle Installationen auf einer Seite aufgelistet werden.

### Detail Seite einer Installation

Jede Installation sollte eine Detail seite haben, mit den wichtigsten Informationen drauf:

* Anzahl Pods
* Liveliness probe abragen

Auch sollten verschiedene Aktionen ausgeführt werden können:

* Löschfunktion
* Start / Stop
* Restart eines einzelnen Pods

#### Log der Instanz

Wenn möglich sollte das Log der Instanz auf der Detailseite dargestellt werden, damit es bei Problemen schnell überprüft werden kann

## Aufsetzen einer Installation

Möglicherweise wäre es noch praktisch die Version der gewählten Installation beim deployen verändern zu können. Dann muss nicht jedes mal das File im Repo angepasst werden

Es soll konfigurierbar sein ob beim runterfahren alles gelöscht werden.

## Nice to have

### Möglichkeit ende des Tages löschen

Es könnte noch praktisch sein, eine Installation automatisch am Ende des Tages wieder löschen zu können.

### Login password automatisch setzten

Ein Service sollte mit verschiedenen Passwörtern für jede Instanz ausgeliefert werden, damit sich nicht alle Schüler auf allen Instanzen einloggen können.

### Super nice to have

Anzahl Pods darstellung
