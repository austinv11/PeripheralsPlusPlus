# Player Sensor

---

The Player Sensor is a peripheral which allows a computer to detect players. It can be crafted with a Turtle to get an [Player Noticing Turtle](/turtle_upgrades/player_noticing_turtle/).

## Events
| Event | Parameter 1 | Parameter 2 | Description |
|-------|-------------|-------------|-------------|
|player|"player"|_string_ playerName|This event is called when a player right clicks upon the peripheral|

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getNearbyPlayers(_number_ range)|_table_ players|Returns nearby players in the form of a table, this table contains other tables with the keys "player" and "distance"|
|getAllPlayers(_boolean_ limitToCurrentWorld)|_table_ players|Returns a list of all players currently on the server (or within the current world if set to)|