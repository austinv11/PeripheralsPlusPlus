# Speaker

---

The speaker is a peripheral which allows for Computers to talk (literally)! It interfaces directly with the Google Translate Text-to-Speech system, allowing you to speak with different language-based voices. It can also be crafted with a turtle to make a [Talkative Turtle](/turtle_upgrades/talkative_turtle/).

## Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Description |
|-------|-------------|-------------|-------------|-------------|
|speechComplete|"speechComplete"|_string_ text|_string_ language|This event is fired after the Text-to-Speech system is finished speaking|

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|speak(_string_ text, [_number_ range, [_string_ languageAccent[, _boolean_ pauseComputerUntilSpeechComplete]]])|_string_ message, _string_ languageAccent|Synthesizes a message to play to nearby players|