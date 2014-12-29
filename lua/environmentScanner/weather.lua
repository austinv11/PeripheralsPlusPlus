--[[
Created by Fxz_y
]]
p = peripheral.find("environmentScanner") --Wrapping of the peripheral
snow = p.isSnow() --Checking if it snows in the biome or not
rain = p.isRaining() --Checking if it's raining
if snow then --if/else to print out whether it is snowing or raining or neither
  if rain then
    print("It is snowing.")
  else
    print("It is not snowing.")
  end
else
    if rain then
      print("It is raining.")
    else
      print("It is not raining.")
    end
end
