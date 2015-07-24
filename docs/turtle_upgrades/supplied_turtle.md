# Supplied Turtle
-----------------

The supplied turtle allows for the turtle to interact with a [resupply station](peripherals/resupply_station) like an ender chest. Craft this with a turtle and a resupply upgrade.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|link(_string or number_ direction) or link(_number_ x, _number_ y, _number_ z)|_boolean_ successful|Links the turtle to the resupply station|
|resupply([_number_ turtleSlot[_string or number_ itemId[, _number_ meta]]])|_boolean_ successful|Attempts to resupply the turtle|
