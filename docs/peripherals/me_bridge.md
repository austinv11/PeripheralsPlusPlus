# ME Bridge

---

The ME Bridge is peripheral which is added when Applied Energistics 2 is installed. This allows you to interact with an ME network (given you have the security access required for the network).

<b>Note: Because of changes in the AE2 API, this peripheral requires at least AE2 rv2 beta 8.</b>

## Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Description |
|-------|-------------|-------------|-------------|-------------|
|gridNotification|"gridNotification"|_string_ notification||This event is fired when changes have been made on the connection to the ME Bridge|
|gridChanged|"gridChanged"|||This event is fired when the grid is changed|
|securityBreak|"securityBreak"||||This event is fired when the ME Bridge is violating security rules, it will break on the next tick|
|craftingComplete|"craftingComplete"|_string_ itemCrafted|_number_ amount|_number_ bytesRequiredToCraft|This event is called when an item finishes crafting after it was requested with the craft() function|

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|listAll()|_table_ items|Lists all referenced items in the network|
|listItems()|_table_ items|Lists all stored items in the network|
|listCraft()|_table_ items|Lists all craftable items in the network|
|retrieve(_string_ itemId, _number_ amount, _string or number_ directionToDeposit)|_number_ extracted|Retrieves the given items and attempts to place it in an inventory in the direction given (either the number or a string like "north" or "up", returns the amount extracted. **Note**: The item id can include metadata value by using the form "[id] [meta]"|
|craft(_string_ itemId, _number_ amount)|_nil_|Attempts to craft the given items **Note**: The item id can include metadata value by using the form "[id] [meta]"|
