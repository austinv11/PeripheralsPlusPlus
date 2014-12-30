--[[
Created by Fxz_y
]]
tArgs = {...} --Arguements
range = tArgs[1]
p = peripheral.find("playerSensor") --Wrapping the peripheral
if p then
  data = p.getNearbyPlayers(tonumber(range)) --Getting the distance to and players nearby
  for k,v in pairs(data) do
    for l,w in pairs(v) do
      print(tostring(l)..": "..tostring(w))
    end
  end
end
