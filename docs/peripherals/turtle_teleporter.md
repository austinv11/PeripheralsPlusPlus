# Turtle Teleporter

---

The Turtle Teleporter is a peripheral which allows a turtle to teleport to variously linked locations. To use one, you must take a vanilla redstone repeater and right click a teleporter with it to start a link. Then right click it onto another teleporter to finish the link. Teleporters normally only have one link, but it can upgraded to an Advanced Teleporter which can have up to 8 links.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|teleport([_number_ destinationID])|_boolean_ result|Teleports to the given destination|
|getLinks()|_table_ links|Returns a table consisting of other tables, each representing a link; it has the keys "dim", "x", "y", "z" and "name"|
|setName(_string_ name)|_string_ name|Sets the name of the teleporter, this can be useful for easy identification of appropriate links|