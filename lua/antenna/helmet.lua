--[[
Created by Fxz_y
]]
--Play around with the settings as much as you want
--Set any unused variables to nil

p = peripheral.find("antenna")
--Please make sure that an antenna is connected

--Global variables that you need to change
player = "nil" --The player that is wearing the helmet
printResolution = false --true/false for printing out
--the resolution of your screen
easyMessage = false --true/false for just sending a
--string, this will show up in the top left cornor
otherMessage = false --true/false for a more complicated
--message
texture = false --true/false for displaying a texture
rectangle = false --true/false for displaying a rectangle
horizontalLine = false --true/false for displaying a
--horizontal line
verticalLine = false --true/false for displaying a
--vertical line

--Variables depending on previous choices:
--Easy message
message = "nil"
--More complicated message
messageString = "nil"
stringX = nil -- x-coordinate
stringY = nil -- y-coordinate
stringRed = nil --Red RGB value     |
stringGreen = nil --Green RGB value |Color string
stringBlue = nil --Blue RGB value   |
--Texture
textureName = "nil" --Unlocalized name of item/block
textureX = nil -- x-coordinate
textureY = nil -- y-coordinate
textureWidth = nil
textureHeight = nil
textureU = nil --Start texture on actual texture(X)
textureV = nil --Start texture on actual texture(Y)
--Rectangle
firstRecX = nil --Start of rectangle
firstRecY = nil --Start of rectangle
secondRecX = nil --End of rectangle
secondRecY = nil --End of rectangle
recRed = nil --Red RGB value      |
recGreen = nil --Green RGB value  |Color rectangle
recBlue = nil --Blue RGB value    |
toRecRed = nil --To red RGB value     |optional
toRecGreen = nil --To green RGB value |for rectangle
toRecBlue = nil --To blue RGB value   |with gradient
--Horizontal line
firstHorLineX = nil --Start of line
secondHorLineX = nil --End of line
horLineY = nil -- y-coordinate
horLineRed = nil --Red RGB value
horLineGreen = nil --Green RGB value
horLineBlue = nil --Blue RGB value
--Vertical line
verLineX = nil -- x-ccordinate
firstVerLineY = nil --Start of line
secondVerLineY = nil --End of line
verLineRed = nil --Red RGB value
verLineGreen = nil --Green RGB value
verLineBlue = nil --Blue RGB value
--End of the variables you may change

--Getting the object
obj = p.getHUD(player)

--Functions
function printRes()
  if printResolution then
    width,height = obj.getResolution()
    print(player.."'s resolution is:")
    print(width.."x"..height)
  end
end

function sendEasyMessage()
  if easyMessage then
    obj.sendMessage(message)
  end
end

function drawText()
  if texture then
    obj.drawTexture(textureName,textureX,textureY,textureWidth,textureHeight,textureU,textureV)
  end
end

function sendString()
  if otherMessage then
    stringColor = obj.getColorFromRGB(stringRed,stringGreen,stringBlue)
    obj.drawString(messageString,stringX,stringY,stringColor)
  end
end

function drawRec()
  if rectangle then
    recColor = obj.getColorFromRGB(recRed,recGreen,recBlue)
    if toRecRed and toRecGreen and toRecBlue then
      toRecColor = obj.getColorFromRGB(toRecRed,toRecGreen,toRecBlue)
      obj.drawRectangle(firstRecX,firstRecY,secondRecX,secondRecY,recColor,toRecColor)
    else
      obj.drawRectangle(firstRecX,firstRecY,secondRecX,secondRecY,recColor)
    end
  end
end

function drawHorLine()
  if horizontalLine then
    horLineColor = obj.getColorFromRGB(horLineRed,horLineGreen,horLineBlue)
    obj.drawHorizontalLine(firstHorLineX,horLineY,secondHorLineX,horLineColor)
  end
end

function drawVerLine()
  if verticalLine then
    verLineColor = obj.getColorFromRGB(verLineRed,verLineGreen,verLineBlue)
    obj.drawVerticalLine(verLineX,firstVerLineY,secondVerLineY,verLineColor)
  end
end

--Actual program

if p then
  printRes()
  sendEasyMessage()
  sendString()
  drawText()
  drawRec()
  drawHorLine()
  drawVerLine()
  obj.sync()
end
