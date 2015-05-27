# XP Turtle

---

The XP Turtle Upgrade is a simple Turtle Upgrade allows Turtles to suck up and use xp.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|add(_number_ amount)|_number_ xp|This function adds experience to the turtle from experience bottles in the selected slot|
|getXP()|_number_ totalXP|Returns the currently stored xp|
|getLevels()|_number_ totalLevels|Returns the number of experience levels currently stored|
|collect()|_number_ xp|Collects nearby xp orbs|
|setAutoCollect(_boolean_ autoCollect)|_boolean_ autoCollect|Sets whether the turtle should periodically (every second) automatically collect xp|
|enchant(_number_ levels)|_boolean_ success|Enchants the selected item with the set amount of experience levels|