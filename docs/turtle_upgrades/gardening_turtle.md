# Gardening Turtle

---

The Gardening Turtle Upgrade is a turtle upgrade which allows turtles to get how grown a crop is as a percentage and use bonemeal on plants. It is crafted with wheat seeds and a turtle. 

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getGrowth()|_number_ growth|This function returns how grown the plant in front of the turtle is. This number is a percentage (Out of 100)|
|getGrowthUp()|_number_ growth|This function returns how grown the plant above the turtle is. This number is a percentage (Out of 100)|
|getGrowthDown()|_number_ growth|This function returns how grown the plant below the turtle is. This number is a percentage (Out of 100)|
|fertilize()|_boolean_ success|This function attempts to use bonemeal in the turtle's currently selected slot on the plant in front of it.|
|fertilizeUp()|_boolean_ success|This function attempts to use bonemeal in the turtle's currently selected slot on the plant above it.|
|fertilizeDown()|_boolean_ success|This function attempts to use bonemeal in the turtle's currently selected slot on the plant below it.|