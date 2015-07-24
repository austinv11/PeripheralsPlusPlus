# Ridable Turtle

---

The ridable turtle is a turtle with the ability to be ridden by the player. It is crafted with a turtle and a saddle. To mount the turtle, hold a carrot-on-a-stick in your hand (or hold control) and right click the turtle. The player can use WASD to move the turtle while riding it, but the player will also be moved with the turtle if it moves on its own through a script. It is not necessary to hold the carrot on a stick for the turtle to move, only to mount it. 

## Functions

| Function | Returns | Description |
|----------|---------|-------------|
|getEntity()|_table_ entityInfo|Retrieves information about the entity riding the turtle, keys include name, type and uuid|
|mountNearbyEntity()|_boolean_ successful|Attempts to mount the nearest entity onto the turtle|
|unmount()|_boolean_ successful|Attempts to unmount the mounted entity from the turtle}
