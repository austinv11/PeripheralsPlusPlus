--[[
Created by Fxz_y
]]
analyzer = {"beeAnalyzer","butterflyAnalyzer","treeAnalyzer"} --The different analyzer variations
local p
for k,v in pairs(analyzer) do
  p = peripheral.find(v) --Attempting to wrap the peripheral
  if p then
    break
  end
end
member = p.isMember() --Checking if there is a valid item in the analyzer
data = p.analyze() --Data from the species
if member then
  for k,v in pairs(data) do --Looping through table
    if type(v) == "table" then
      for l,w in pairs(v) do
        v = string.lower(tostring(v))
        print(tostring(k)..": "..tostring(w))
      end
    else
      v = string.lower(tostring(v))
      print(tostring(k)..": "..v)
    end
  end
else
  print("The item in the analyzer can't be recognised!")
end
