# AI Chatbox

---

The chatbox is a peripheral which allows for Computers to interface with Cleverbot via __Pierre-David Bélanger__'s [chatter-bot-api](https://github.com/pierredavidbelanger/chatter-bot-api/).

Since the devs at _Cleverbot_ makes updates from day to day, it is not garunteed that this peripheral will work 24/7. If it doesn't work as expected then please add it to the [issue tracker](https://github.com/austinv11/PeripheralsPlusPlus/issues)!

## Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Parameter 4 | Description |
|-------|-------------|-------------|-------------|-------------|-------------|
|"ai\_response"|_string_ side|_boolean_ success|_string_ response|_string_ uuid|This event is fired when the peripheral receives a reply from _Cleverbot_. Similar to the [HTTP events](http://computercraft.info/wiki/Http_success_(event)).|

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|newSession()|_sessionObject_ session|Creates a new session to _Cleverbot_.|
|getSession(_string_ uuid)|_sessionObject_ session|Gets the session object from the peripheral by the UUID. Returns _nil_ if there is no match.|
|getAllSessions()|table{[_string_ uuid] = _sessionObject_ session}|Returns a table containing all sessions belonging to that specific peripheral block.|

## Session Object

Session objects are created via the peripheral and contains methods to communicate with _Cleverbot_. A session is like a conversation, when you say something new it will base the answer off what previously has been said in the session/_conversation_.

Each session is linked to its _AI Chatbox_ peripheral block.
**Note**: While each session gets assigned an UUID, they are only garunteed to be unique for each peripheral block. This means that two sessions from two different _AI Chatbox_es may get the same UUID, but that wont matter since they are not linked in any way.

### Functions
| Function | Returns | Description |
|----------|---------|-------------|
|_session_.think(_string_ message)|_boolean_ success, _string_ response|Sends your _message_ to _Cleverbot_ and waits for the reply before returning, and therefore yielding the computer until that happens.|
|_session_.thinkAsync(_string_ message)|_nil_|Same as _session.think()_ but will not hault/yield the computer. You will have to use _[os.pullEvent()](http://computercraft.info/wiki/Os.pullEvent)_ to catch the response.|
|_session_.getUUID()|_string_ uuid|Gets the UUID assigned to this session. Useful for idendifying the _ai\_response_ event.|
|_session_.remove()|_nil_|Destroys the _session_ and removes it from the peripheral. Not a required step. Once the _session_ is removed it cannot be used again, in fact, it will even raise an error if you do.|

**Note**: All attached computers will receive the _ai\_response_ event.

**Note**: Both _session.think()_ and _session.thinkAsync()_ queues the _ai\_response_ event, while _session.think()_ is just a wrapper that waits for the event automatically.

## Credits

Credits to __Pierre-David Bélanger__'s [chatter-bot-api](https://github.com/pierredavidbelanger/chatter-bot-api/) for making the creation of this peripheral a lot simpler, and __Rollo Carpenter__'s [Cleverbot](http://www.cleverbot.com/) for a sweet AI.