--[[
Created by Fxz_y
]]
tArgs = {...}
amount = tArgs[1]
p = peripheral.find("feeder")
if p then
  for i = 1, amount do
    p.feed()
  end
end
