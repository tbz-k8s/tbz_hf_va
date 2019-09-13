# Statusbericht

Der erste Statusbericht wirde nach Rücksprache mit Marcel etwas kürzer ausfallen, da ich das Kickoff erst vor 2 Tagen hatte.
Trotzdem habe ich mir schon ein paar Gedanken zur Umsetzung des Projektes gemacht.

## Kickoff

Das kurze Kickoffmeeting am 11.09.2019 ist meiner Meinung nach sehr schnell und effizient abgelaufen.
Ich denke ich habe alle Informationen zum Projektumfang und Ablauf.

## Herausforderungen

Ich denke das Erstellen der Applikation selber sollte nicht all zu viele Probleme bereiten. Die grösste Herausforderung werden
meiner Meinung nach die vielen Umsysteme, die angesprochen werden müssen.

Wobei die bisherige Erfahrung welche ich mit Kubernetes gemacht habe waren durchaus positiv, von der Dokumentationsseite her.
Da Kubernetes schon fast zu einem Industriestandard geworden ist sollte bereits genug tooling vorhanden sein.

## Tools

Die Umsetzung der Applikation werde ich voraussichtlich mit Java / Spring Boot (möglicherweise auch mit [Ktor](https://ktor.io/)) und Vue.js machen.
Java kenne ich bereits gut und Vue.js wollte ich schon seit einer Weile mal für ein Projekt einsetzen, daher wohl eine gute kombo.

## Applikation

Die Applikation werde ich vermutlich so stateless wie nur möglich halten. Dadurch sollte das Deployment überhaupt kein Problem sein.
Weiter wiederspiegelt die Applikation damit garantiert immer den aktuellen Zustand, der von den Systemen angegeben wird.
