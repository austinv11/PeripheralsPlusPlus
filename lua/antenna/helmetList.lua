--[[
Created by Fxz_y
]]
p = peripheral.find("antenna")
if p then
  list = p.getPlayers()
  for i,k in pairs(list) do
    print("-"..k)
  end
end
