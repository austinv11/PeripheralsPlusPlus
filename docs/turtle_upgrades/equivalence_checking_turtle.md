# Equivalence Checking Turtle

---

The Equivalence Checking Turtle Upgrade is the Turtle variant of the [Ore Dictionary](/peripherals/ore_dictionary/) block. 

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getEntries()|_table_ entries|Returns a table containing all ore dictionary entries of the item in the selected turtle slot|
|combineStacks(_number_ slotNum1, _number_ slotNum2)|_boolean_ success|This function will combine two equivalent stacks in the turtles inventory into the selected slot (Note:The selected slot, then first slot have priority)|
|transmute()|_boolean_ success|Cycles the selected item through all OreDictionary entries (think of the forge lexicon)|
|doItemsMatch(_number_ slotNum1, _number_ slotNum2)|_boolean_ itemMatches|This function checks whether the two chosen items are equivalent according to the OreDictionary|