# Antenna

---

The Antenna is a peripheral which expands the wireless capabilities of computers. It currently has three uses: communicating with [Satellites](/miscellaneous_additions/satellites/) and communicating with [Smart Helmets](/miscellaneous_additions/smart_helmets/) and
communicating with [Nano Swarms](/miscellaneous_additions/nano_swarm/).

## Satellite Functions
| Function | Returns | Description |
|----------|---------|-------------|
|listSatellites()|_table_ satellites|Gets all the currently orbiting satellites in the world, table uses the keys: id, x, y, z, upgrade, addons|
|connectToSatelliteById(_number_ id)|_object_ satelliteHandle|Returns an object representing the given satellite, see the [Satellites](/miscellaneous_additions/satellites/) for info on how to use this|

## Satellite Events
| Event | Parameter 1 | Parameter 2 | Parameter 3 | Parameter 4 | Parameter 5 | Parameter 6 |Description |
|-------|-------------|-------------|-------------|-------------|-------------|-------------|------------|
|satelliteCrash|"satelliteCrash"|_number_ id|_number_ x|_number_ y|_number_ z|_number_ dimension|This event is fired when a satellite crashes|
|satelliteLaunch|"satelliteLaunch"|_number_ x|_number_ y|_number_ z|_number_ dimension||This event is fired when a satellite is launched|

## Smart Helmet Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getPlayers()|_table_ players|Lists all the players currently wearing a smart helmet linked to the antenna|
|getHUD(_string_ player)|_object_ helmetHandle|Returns an object representing the given player's smart helmet HUD, see the [Smart Helmets](/miscellaneous_additions/smart_helmets/) for info on how to use this|

## Nano Swarm Functions
<table>
  <thead>
    <tr>
      <th>Function</th>
      <th>Returns</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>getInfectedEntites()</td>
      <td><em>table</em> entities</td>
      <td>Returns a list containing the entity IDs for all entities currently infected by a nanobot swarm.</td>
    </tr>
    <tr>
      <td>getInfectedEntity(<em>number</em> entityID)</td>
      <td><em>object</em> entity</td>
      <td>Returns an object representing the infected entity.</td>
    </tr>
  </tbody>
</table>

## Labeling Functions
| Function | Returns | Description |
|----------|---------|-------------|
|setLabel(_string_ label)|_nil_|Sets a new label for the antenna|
|getLabel()|_string_ label|Returns the current label of the antenna|