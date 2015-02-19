--[[
Created by Fxz_y
]]
p = peripheral.find("noteBlock")
if p then
  tArgs = {...}
  if not tArgs[1] then
    print('Usage: <"Sound"> <volume> <pitch>')
    print('For all vanilla sounds I refer you to: http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571574-all-minecraft-playsound-file-name')
  else
    sound = tArgs[1]
    volume = tonumber(tArgs[2])
    pitch = tonumber(tArgs[3])
    p.playSound(sound, volume, pitch)
  end
end
