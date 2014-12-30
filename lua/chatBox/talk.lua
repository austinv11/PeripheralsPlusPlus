--[[
Created by Fxz_y
]]
tArgs = {...} -- Arguments(please always use "..." for strings
sender = tArgs[1] -- Name of Sender
message = tArgs[2] -- Message
receiver = tArgs[3] -- Name of receiver(only for private messages)
p = peripheral.find("chatBox") -- Wrapping of the chatbox
message = sender..": "..message -- Adding the sender's name to the message
if receiver then -- Checking for a receiver
  p.tell(receiver, message) -- Sending private message
else -- No Receiver
  p.say(message) -- Sending public message
end
