# Smart Helmets

---

Smart helmets allow for computers to directly interact with a player. In order to be able to use a smart helmet, right click a smart helmet onto an [antenna](/peripherals/antenna/) to link it to that antenna. Currently, all the smart helmet does is let you interact with the wearer's HUD

The smart helmet item's description (in the tooltip) will be the connected antenna's label. If no label has been set for the antenna, the description will instead be the UUID of the connected antenna. 

## Smart Helmet HUD functions
A smart helmet HUD works with a stack (or technically 'Deque') based system. Meaning that you create a list of instructions, which are then synced to the player and are run in a FIFO ("First In First Out") order. With the smart helmet, you could modify a player's hud as well as create custom guis!

### Hud Functions:

| Function | Returns | Description |
|----------|---------|-------------|
|getResolution()|_number_ width, _number_ height|Gets the screen resolution of the smart helmet wearer in question|
|sendMessage(_string_ message)|_nil_|Simplified function to easily allow for sending messages to a player's HUD|
|drawString(_string_ message, _number_ x, _number_ y[, _number_ color[, _boolean_ shadow]])|_nil_|Draws a string to a player's HUD|
|drawTexture(_string_ texture, _number_ x, _number_ y[, _number_ width[, _number_ height[, _number_ u[, _number_ v]]]])|_nil_|Draws a rectangle with the passed texture (can be an item/block id or direct resource location)|
|drawRectangle(_number_ x1, _number_ y1, _number_ x2, _number_ y2, _number_ color[, _number_ toColor])|_nil_|Draws a solid colored rectangle, if passed two colors, it will draw the rectangle with a gradient|
|drawHorizontalLine(_number_ x1, _number_ y, _number_ x2, _number_ color)|_nil_|Draws a horizontal line|
|drawVerticalLine(_number_ x1, _number_ y1, _number_ y2, _number_ color)|_nil_|Draws a vertical line|
|sync()|_nil_|Overwrites the player's render stack with the current one **Note:** Clears the current stack|
|clear()|_nil_|Clears the player's render stack **Note:** Clears the current stack|
|add()|_nil_|Adds to the current render stack to the player's one **Note:** Clears the current stack|
|getColorFromRGB(_number_ red, _number_ green, _number_ blue[, _number_ alpha])|_number_ colorValue|Computes a color value from the given rgb (and a) values|
|getGUI()|_object_ guiObject|Returns a gui object for this hud, similar to the getHUD() function of the antenna|

### Gui Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getResolution()|_number_ width, _number_ height|Gets the screen resolution of the smart helmet wearer in question|
|sendMessage(_string_ message)|_nil_|Simplified function to easily allow for sending messages to a player's HUD|
|drawString(_string_ message, _number_ x, _number_ y[, _number_ color[, _boolean_ shadow]])|_nil_|Draws a string to a player's HUD|
|drawTexture(_string_ texture, _number_ x, _number_ y[, _number_ width[, _number_ height[, _number_ u[, _number_ v]]]])|_nil_|Draws a rectangle with the passed texture (can be an item/block id or direct resource location)|
|drawRectangle(_number_ x1, _number_ y1, _number_ x2, _number_ y2, _number_ color[, _number_ toColor])|_nil_|Draws a solid colored rectangle, if passed two colors, it will draw the rectangle with a gradient|
|drawHorizontalLine(_number_ x1, _number_ y, _number_ x2, _number_ color)|_nil_|Draws a horizontal line|
|drawVerticalLine(_number_ x1, _number_ y1, _number_ y2, _number_ color)|_nil_|Draws a vertical line|
|sync()|_nil_|Overwrites the player's render stack with the current one **Note:** Clears the current stack|
|clear()|_nil_|Clears the player's render stack **Note:** Clears the current stack|
|add()|_nil_|Adds to the current render stack to the player's one **Note:** Clears the current stack|
|getColorFromRGB(_number_ red, _number_ green, _number_ blue[, _number_ alpha])|_number_ colorValue|Computes a color value from the given rgb (and a) values|
|open()|_nil_|Opens the custom gui|
|close()|_nil_|Closes the custom gui|
|drawBackground([_boolean_ defaultBackground])|_nil_|Draws a background for the gui, dirt background if true and a simple black, transparent background if false|
|addButton(_number_ x1, _number_ y1, _number_ x2, _number_ y2, _number_ id, _string_ label)|_nil_|Creates a button for the gui|
|addTextField(_number_ x1, _number_ y1, _number_ x2, _number_ y2, _number_ id[, _string_ defaultEntry])|_nil_|Adds a user-modifiable text field for the gui|

### General Smart Helmet Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Parameter 4 | Description |
|-------|-------------|-------------|-------------|-------------|-------------|
|mouseInput|"mouseInput"|_string_ player|_number_ mouseButton|_boolean_ buttonState|This is called when a player wearing a smart helmet clicks their mouse|
|keyInput|"keyInput"|_string_ player|_number_ keyNumber|_boolean_ keyState|This is called when a player wearing a smart helmet types on their keyboard|

### Custom Gui-Specific Smart Helmet Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Parameter 4 | Description |
|-------|-------------|-------------|-------------|-------------|-------------|
|buttonClicked|"buttonClicked"|_string_ player|_number_ buttonId||This is called when a player in a custom gui clicks on a button|
|textboxEntry|"textboxEntry"|_string_ player|_number_ textFieldId|_string_ textFieldContents|This is called when a player in a custom gui enters text to a textField|