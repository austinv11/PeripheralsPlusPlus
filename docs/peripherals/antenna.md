# Antenna

---

The Antenna is a peripheral which expands the wireless capabilities of computers. It currently has two uses: communicating with [Smart Helmets](/miscellaneous_additions/smart_helmets/) and
communicating with [Nano Swarms](/miscellaneous_additions/nano_swarm/).

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
