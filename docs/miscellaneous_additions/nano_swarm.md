# Nano Swarm

---

A Nano Swarm is a throwable entity that will infect the first mob it touches. After infecting the mob, a player can control what the infected mob does by communicating with the nano swarm through an [antenna](/peripherals/antenna/). Before a nano swarm can be thrown (or shot by a dispenser), it first must be linked to an antenna. To do this, simply right click on an antenna with the swarm in your hand.

Many attributes and actions for the infected entity can be accessed through the nano swarm. However, the nanobots can only perform a certain amount of actions and gather so much data before they become unusable. By default, the nanobots can only receive 8 instructions before being destroyed. This is configurable in the config file.
##Functions
Functions accessible for both mobs and players.
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
				<td>isPlayer()</td>
				<td><em>boolean</em> isPlayer</td>
				<td>Returns whether or not the entity is a player.</td>
			</tr>
			<tr>
				<td>hurt()</td>
				<td><em>boolean</em> success</td>
				<td>Hurts the entity.</td>
			</tr>
			<tr>
				<td>heal()</td>
				<td><em>boolean</em> success</td>
				<td>Heals the entity.</td>
			</tr>
			<tr>
				<td>getHealth()</td>
				<td><em>number</em> health</td>
				<td>Returns the entity's current health.</td>
			</tr>
			<tr>
				<td>getMaxHealth()</td>
				<td><em>number</em> maxHealth</td>
				<td>Returns the entity's maximum health.</td>
			</tr>
			<tr>
				<td>isDead()</td>
				<td><em>boolean</em> isDead</td>
				<td>Returns whether or not the entity is dead.</td>
			</tr>
			<tr>
				<td>getRemainingBots()</td>
				<td><em>number</em> remainingBots</td>
				<td>Returns the number of nanobots left in the entity.</td>
			</tr>
			<tr>
				<td>getDisplayName()</td>
				<td><em>string</em> name</td>
				<td>Returns the entity's display name.</td>
			</tr>
		</tbody>
	</table>
	


##Player-Only Functions
Functions exclusive to players.
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
				<td>getPlayerName()</td>
				<td><em>string</em> name</td>
				<td>Returns the player's name.</td>
			</tr>
			<tr>
				<td>getUUID()</td>
				<td><em>string</em> UUID</td>
				<td>Returns the player's UUID.</td>
			</tr>
			<tr>
				<td>getHunger()</td>
				<td><em>number</em> foodLevel</td>
				<td>Returns the player's current food level (hunger).</td>
			</tr>
			<tr>
				<td>click(<em>number</em> button)</td>
				<td><em>nil</em></td>
				<td>Simulates a click for the player.</td>
			</tr>
			<tr>
				<td>clickRelease(<em>number</em> button)</td>
				<td><em>nil</em></td>
				<td>Simulates the releasing of the left mouse button for the player.</td>
			</tr>
			<tr>
				<td>keyPress(<em>string</em> key)</td>
				<td><em>nil</em></td>
				<td>Simulates a key press for the player.</td>
			</tr>
			<tr>
				<td>keyRelease(<em>string</em> key)</td>
				<td><em>nil</em></td>
				<td>Simulates the releasing of the passed key for the player.</td>
			</tr>
			<tr>
				<td>mouseMove(<em>number</em> x, <em>number</em> y)</td>
				<td><em>nil</em></td>
				<td>Moves the player's mouse to the passed coordinates.</td>
			</tr>
			<tr>
				<td>whisper(<em>string</em> message, <em>string</em> senderName)</td>
				<td><em>boolean</em> success</td>
				<td>Whispers the passed message to the player. It will appear to come from the passed sender name.</td>
			</tr>
		</tbody>
	</table>

##Mob-Only Functions
Functions exclusive to mobs.
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
				<td>getEntityName()</td>
				<td><em>string</em> name</td>
				<td>Returns the class name of the entity.</td>
			</tr>
			<tr>
				<td>setTarget(<em>string</em>/<em>number</em> name/x, <em>number</em> y, <em>number</em> z)</td>
				<td><em>nil</em></td>
				<td>Sets the target of the entity. Either pass a player's name or the coordinates the target.</td>
			</tr>
			<tr>
				<td>setAttackTarget(<em>string</em>/<em>number</em> name/x, <em>number</em> y, <em>number</em> z)</td>
				<td><em>nil</em></td>
				<td>Sets the attack target of the entity. Either pass a player's name or the coordinates the target.</td>
			</tr>
			<tr>
				<td>setMovementTarget(<em>string</em>/<em>number</em> name/x, <em>number</em> y, <em>number</em> z)</td>
				<td><em>nil</em></td>
				<td>Sets the movement target of the entity. Either pass a player's name or the coordinates the target.</td>
			</tr>
			<tr>
				<td>setTurnAngle(<em>number</em> rotation)</td>
				<td><em>nil</em></td>
				<td>Sets the entity's rotation yaw.</td>
			</tr>
			<tr>
				<td>toggleJumping()</td>
				<td><em>boolean</em> success</td>
				<td>Toggles jumping for the entity.</td>
			</tr>
		</tbody>
	</table>