--[[
Created by austinv11
]]
tArgs = {...}
local LANGUAGE = "English"--Change this to change the voice
local SPEED = 1--Change this to allow more or less time between sentences
local p = peripheral.find("speaker")
for key,value in pairs(tArgs) do
  p.speak(value, 64, LANGUAGE)
  sleep(SPEED)
  end
