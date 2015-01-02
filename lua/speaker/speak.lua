--[[
Created by austinv11
]]
tArgs = {...}
local LANGUAGE = "English"--Change this to change the voice
local p = peripheral.find("speaker")
for key,value in pairs(tArgs) do
  p.speak(value, 64, LANGUAGE)--Speaking
  local event, text, lang = os.pullEvent("speechComplete")--Waiting for the speech to complete
  end
