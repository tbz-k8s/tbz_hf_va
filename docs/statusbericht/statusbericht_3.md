# Statusbericht 3

## Kubernetes API

Die Benutzung der Kubernetes API in Java hat sich viel schwieriger gestaltet als initial gedacht. Der offizielle Client und dazugehörige Models sind nur sehr Bedingt
für meinen Zweck geeignet, da Sie starr an die Kubernetes API Versionen gebunden sind.

Daher habe ich mich dann doch für den [Fabric8io](https://github.com/fabric8io/kubernetes-client) Client entschieden, der flexibler aufgebaut ist.
Leider ist er aber auch nicht all zu gut Dokumentiert. Es brauchte eine Weile um herauszufinden wie der Client genau funktioniert.
Dies habe ich jetzt aber einigermassen raus.

## Zeitmanagement

Zeittechnisch bin ich Leider noch weiter hinter meinen eigentlichen Zeitplan gefallen. In den Herbstferien hatte ich Arbeitsbeding (seit ein paar Monaten bin ich im Bereich Betrieb tätig) 2 60h Wochen, in denen ich eigentlich 2 Volle arbeitstagen an der VA geplant hatte.
Auch hat das wieder kennenlernen von Kubernetes doch mehr Zeit in anspruch genommen als initial gedacht.

Technisch sollte jetzt aber eigentlich alles klar sein und es muss "nur" noch fertig implementiert werden.

Aber ich denke nächste Woche sollte ein MVP der Applikation bereit stehen um es an einem Kubernetes Cluster zu testen.

## Vue.js

Nach ein paar Einstiegsschwierigkeiten, vor allem im Bereich IDE Support, konnte ich mich jetzt recht gut mit Vue anfreunden und habe die Repo Einstellungsmaske und Navigation so weit fertig.

## Database

Mit [Nitrite](https://github.com/dizitart/nitrite-database) habe ich eine Embedded NoSql DB mit guter Kotlin Integration gefunden, die alles erfüllt was ich brauche.
