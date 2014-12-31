--[[
Created by Fxz_y
]]
p = peripheral.find("barrel")
if p then
  while true do
    name = p.getLocalizedName()
    amount = p.getAmount()
    unlocalizedName = p.getUnlocalizedName()
    if name then
      term.clear()
      term.setCursorPos(1,1)
      print("Item in barrel: "..name..".")
      print("Amount: "..amount.." of 4096.")
      print("Press P to put items in the barrel.")
      print("Press G to get 1 stack.")
      continue = false
      repeat
        event, char = os.pullEvent()
        if event == "char" and char == "p" then
          for i = 1, 16 do
            turtle.select(i)
            item = turtle.getItemDetail()
            count = turtle.getItemCount()
            if item then
              print(unlocalizedName.." and "..item.name)
              if count and item["name"] == unlocalizedName then
                p.put()
              end
            end
          end
          turtle.select(1)
          continue = true
        elseif event == "char" and char == "g" then
          slot = 1
          repeat
            item = turtle.getItemDetail()
            if item then
              slot = slot+1
            end
            turtle.select(slot)
          until p.get()
          continue = true
        end
      until continue
    else
      term.clear()
      term.setCursorPos(1,1)
      print("Nothing in the barrel.")
      print("Press P to put an item in the barrel.")
      event, char = os.pullEvent("char")
      if char == "p" then
        for i = 1, 16 do
          turtle.select(i)
          item = turtle.getItemDetail()
          count = turtle.getItemCount()
          if item and count then
            if item["name"] == unlocalizedName or not unlocalizedName then
              p.put()
            end
          end
          unlocalizedName = p.getUnlocalizedName()
        end
        turtle.select(1)
      end
    end
  end
end
