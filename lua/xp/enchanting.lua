--[[
Created by Fxz_y
]]
p = peripheral.find("xp")
state = "on"
function printOut()
  term.clear()
  term.setCursorPos(1,1)
  print("Levels: "..levels..".")
  print("XP: "..xp..".")
  if state == "on" then
    print("Collecting XP: On.")
    print("Press X to turn off XP collecting.")
  elseif state == "off" then
    print("Collecting XP: Off.")
    print("Press X to turn on XP collecting.")
  end
  print("Press B to use bottles o' enchanting that are in the first slot.")
  print("Press E to enchant an item in the first slot.")
  print(" ")
  print(" ")
  print(" ")
  print("Press R to refresh the page.")
end
function enchant()
  term.clear()
  term.setCursorPos(1,1)
  print("What level do you want your enchant to be?")
  level = read()
  if tonumber(levels) >= tonumber(level) then
    p.enchant(tonumber(level))
  else
    term.clear()
    term.setCursorPos(1,1)
    print("This turtle doesn't have enough levels to perform the enchant!")
    print("You will be redirected in 5 seconds.")
    sleep(5)
  end
end
if p then
  while true do
    levels = p.getLevels()
    xp = p.getXP()
    printOut()
    if state == "on"then
      p.setAutoCollect(true)
      continue = false
      repeat
        event, char = os.pullEvent()
        printOut()
        if event == "char" then
          if char == "x" then
            state = "off"
            continue = true
          elseif char == "b" then
            p.add()
            continue = true
          elseif char == "e" then
            enchant()
            continue = true
          elseif char == "r" then
            continue = true
          end
        end
      until continue
    elseif state == "off" then
      p.setAutoCollect(false)
      continue = false
      repeat
        event, char = os.pullEvent()
        printOut()
        if event == "char" then
          if char == "x" then
            state = "on"
            continue = true
          elseif char == "b" then
            p.add()
            continue = true
          elseif char == "e" then
            enchant()
            continue = true
          elseif char == "r" then
            continue = true
          end
        end
      until continue
    end
  end
end
