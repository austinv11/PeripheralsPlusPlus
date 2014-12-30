--[[
Created by Fxz_y
]]
p = peripheral.find("playerSensor") --Wrapping the peripheral
if p then
  data = p.getAllPlayers() --Getting all the players currently online
  for k,v in pairs(data) do
    print(tostring(k)..": "..tostring(v))
  end
end
