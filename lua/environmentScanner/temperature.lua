--[[
Created by Fxz_y
]]
p = peripheral.find("environmentScanner") --Wrapping of the peripheral
temp = p.getTemperature() --Checking temperature
temp = string.lower(temp) --Changing the string to lower case because someone thought it would be funny to make it print out upper case
print("This biome has a "..temp.." temperature.") --Printing out the temperature
