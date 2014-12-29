--[[
Created by Fxz_y
]]
p = peripheral.find("environmentScanner") --Wrapping of the peripheral
biome = p.getBiome() --Checking the biome
print("You are in a "..biome.." biome.") --Printing out the biome
