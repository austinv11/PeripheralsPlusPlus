# Chatbox

---

The chatbox is a peripheral which allows for Computers to interface with Minecraft's chat system. It can allow you to send, receive and broadcast chat messages. It can also be crafted with a turtle to make a [Chatty Turtle](/turtle_upgrades/chatty_turtle/).

## Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Parameter 4 | Description |
|-------|-------------|-------------|-------------|-------------|-------------|
|chat|"chat"|_string_ playerName|_string_ message||This event is fired when someone speaks in chat|
|death|"death"|_string_ player|_string_ killer|_string_ damageType|This event is fired when someone dies|
|command|"command"|_string_ player|_table_ arguments||This event is fired when someone types a command prefixed with '\' (by default). When a command is done like this, it will not appear in chat|

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|say(_string_ text, [_number_ range, [_boolean_ infVertical,[_string_ label]]])|_boolean_ result|Sends a message to chat **Note**: The label can only be applied if enabled in the config|
|tell(_string_ playerName, _string_ text, [_number_ range, [_boolean_ infVertical,[_string_ label]]])|_boolean_ result|Sends a message to the specified player **Note**: The label can only be applied if enabled in the config|
