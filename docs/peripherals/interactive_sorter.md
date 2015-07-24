# Interactive Sorter
--------------------

The interactive sorter makes it easy for a computer to process items. It acts like an item buffer, holding up to one stack at a time.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|analyze()|_table_ result|Gathers information about the item in the sorter, the table has the keys: amount, stringId, numericalId, oreDictionaryEntries, meta, name, and nbt|
|push(_string or number_ direction[, _number_ amount])|_boolean_ successful|Pushes the item in the sorter to the inventory in the provided direction|
|pull(_string or number_ direction[, _number_ amount[, _number_ slot]])|_boolean_ successful|Pulls an item from the inventory in the provided directino into the sorter|
|isInventoryPresent(_string or number_ directio)|_boolean_ inventoryPresent|Checks if an inventory is present in the provided inventory|
