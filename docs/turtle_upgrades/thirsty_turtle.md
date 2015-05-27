# Thirsty Turtle

---

The Thirsty Turtle Upgrade is a Turtle Upgrade allows Turtles to suck up and drop fluids.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|drop()|_number_ amount|Places a fluid in the world or tries to put a fluid inside a tank|
|dropUp()|_number_ amount|Places a fluid in the world or tries to put a fluid inside a tank|
|dropDown()|_number_ amount|Places a fluid in the world or tries to put a fluid inside a tank|
|suck([_number_ amount])|_number_ amount|Sucks a fluid from the world or from a tank|
|suckUp([_number_ amount])|_number_ amount|Sucks a fluid from the world or from a tank|
|suckDown([_number_ amount])|_number_ amount|Sucks a fluid from the world or from a tank|
|getLiquid()|_table_ fluidContained|Gets info for a fluid contained, table has the key values: amount, id and name|
|pack([_number_ slot])|_number_ amount|Attempts to pack the fluid contained in the turtle into an item in the turtle's inventory|
|unpack([_number_ slot])|_number_ amount|Attempts to unpack a fluid from an item in the turtle's inventory|