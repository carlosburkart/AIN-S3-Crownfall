# Crownfall

Ein rundenbasiertes Strategiespiel für zwei Spieler, entwickelt in Scala 3 im Rahmen der Vorlesung Software Engineering.

## Spielidee

Zwei Armeen aus Nahkampf- und Fernkampfeinheiten treten auf einem 8×8-Spielfeld gegeneinander an. Ähnlich wie beim Schach ziehen die Spieler abwechselnd. Anders als beim Schach haben Einheiten Werte wie Lebenspunkte, Angriff und Reichweite und können teils aus der Entfernung angreifen. Gewonnen hat, wer die gegnerische Armee vollständig besiegt.

## Starten

```
sbt run
```

## Tests ausführen

```
sbt test
```

Mit Testabdeckung (Coverage):

```
sbt clean coverage test coverageReport
```

Der Bericht liegt danach unter `target/scala-<version>/scoverage-report/index.html`.

## Projektstruktur

```
src/main/scala/crownfall/
├── model/        Spiellogik: Spielfeld, Einheiten, Regeln
├── controller/   Verbindet Eingaben mit der Spiellogik
├── aview/        Darstellung: Text (tui) und Grafik (gui)
└── util/         Hilfsklassen
```

## Team

- Mamoon Akhter
- Carlos Burkart
