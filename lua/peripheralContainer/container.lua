--[[
Created by Fxz_y
]]
p = peripheral.find("peripheralContainer")
if p then
  list = p.getContainedPeripherals()
  if list then
    print("The container holds "..#list.." peripheral(s):")
    for i,k in pairs(list) do
      print("-"..k)
    end
  end
end
