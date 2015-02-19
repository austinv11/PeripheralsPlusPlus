--[[
Created by Fxz_y
]]
p = peripheral.find("noteBlock")
if p then
  tArgs = {...}
  if not tArgs[1] then
    print("Usage: Arguments look like this:")
    print("Example: 1,2 2,12 1,5 (First number represents the instrument(0-4) and the second number represents the note(0-24)")
  else
    for i,k in pairs(tArgs) do
      instr = tonumber(string.sub(k,1,1))
      instrNote = tonumber(string.sub(k,3,string.len(k)))
      if instr>=0 and instr<=4 and instrNote>=0 and instrNote<=24 then
        p.playNote(instr,instrNote)
        sleep(0.5)
      else
        print("Min: 0,0 , Max: 4,24")
      end
    end
  end
end
