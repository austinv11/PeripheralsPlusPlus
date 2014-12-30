--[[
Created by Fxz_y
]]
p = peripheral.find("oreDictionary") --Wrapping the peripheral
if p then
  while true do
    turtle.select(1)
    if turtle.getItemCount(1) == 0 then
      term.clear()
      term.setCursorPos(1,1)
      print("Place an item in the first slot to transmute it.")
    elseif turtle.getItemCount(1) ~= 0 then
      entries = p.getEntries() --Gets the ore dictionary entries from the item
      term.clear()
      term.setCursorPos(1,1)
      print("Press T to transmute!")
      for k,v in pairs(entries) do --Printing all the entries
        print(tostring(k)..": "..tostring(v))
      end
      continue = false
      repeat
        event,char = os.pullEvent()
        if event == "char" and char == "t" then
          p.transmute() --Transmuting to an equivalent item
          continue = true
        elseif event == "turtle_inventory" then
          continue = true
        end
      until continue
    else
      term.clear()
      term.setCursorPos(1,1)
      sleep(0.5)
    end
  end
end
