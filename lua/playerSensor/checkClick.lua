--[[
Created by Fxz_y
]]
p = peripheral.find("playerSensor") --Wrapping the peripheral
if p then
  click,name = os.pullEvent("player") --Listening for when a player right clicks a player sensor
  if click then
    print(tostring(name).." just right-clicked the sensor.")
  else
    sleep(0.5)
  end
end
