# Analyzers

---

Analyzers are peripherals which are available if [Forestry](http://forestry.sengir.net) is installed. The variants can be used to analyze bees, saplings, and butterflies (if they were pre-analyzed).

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|analyze()|_table_ speciesInfo|Retrieves information about the species inside the analyzer|
|isMember()|_boolean_ isMember|Returns whether the item placed in the analyzer is an appropriate species to analyze|

### Species Info Table Keys
|Bee Analyzers:|Butterfly Analyzers:|Tree Analyzers:|
|:------------:|:------------------:|:-------------:|
|type|speciesPrimary|speciesPrimary|
|speciesPrimary|speciesSecondary|speciesSecondary|
|speciesSecondary|speed|height|
|speed|lifespan|fertility|
|lifespan|metabolism|yield|
|fertility|fertility|sappiness|
|nocturnal|nocturnal|matures|
|tolerantFlyer|tolerantFlyer|fruit|
|caveDwelling|fireResistant|growth|
|flower|flower|girth|
|territory|effect|plant|
|effect|temperature|effect|
|temperature|toleranceTemperature||
|toleranceTemperature|humidity||
|humidity|toleranceHumidity||
|toleranceHumidity|||