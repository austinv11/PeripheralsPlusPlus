# Player Interface

---

The player interface is a peripheral that can be used to interact with a linked player's inventory. Players are linked by placing a permissions card inside of the interface.
The interface cannot access a player's inventory if they do not have a permissions card in the interface.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getPlayerInv(_string_ playerName)|_PlayerInventory_ inventory|Returns a PlayerInventory object of the specified player.|
|setOutputSide(_string_ side)|_nil_|Sets the output side of the interface. Takes cardinal directions.|
|setInputSide(_string_ side)|_nil_|Sets the input side of the interface. Takes cardinal directions.|
|getOutputSide()|_string_ outputSide|Returns the output side of the interface.|
|getInputSide()|_string_ inputSide|Returns the input side of the interface.|

# PlayerInventory

An object used to interact with a player's inventory. Can be accessed with the getPlayerInv() method of the player interface. 

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getStackInSlot(_num_ slotIndex)|_table_ stack|Returns a table with information pertaining to the itemstack in slot slotIndex.|
|retrieveFromSlot(_num_ retrieveSlot, _num_ amount)|_boolean_ success|Retrieves a stack of size amount from slot retrieveSlot and attemtps to place it in the output inventory.|
|pushToSlot(_num_ sourceSlot, _num_ amount, _num_ destSlot)|_boolean_ success|Pushes amount of items from stack from slot sourceSlot in the input inventory to slot destSlot in the player's inventory.|
|push()|_num_ sourceSlot, _num_ destSlot|Pushes entire stack from slot sourceSlot in the input inventory to slot destSlot of the player's inventory.|
|getSize()|_number_ size|Returns the size of the player's inventory.|
