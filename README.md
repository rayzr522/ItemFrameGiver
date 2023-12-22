# ItemFrameGiver

> Allows you to create item frames that give you items with a configurable cooldown

## Installation

Just grab the latest JAR off of the [releases page](https://github.com/Rayzr522/ItemFrameGiver/releases).

## Usage

Giver frames give you the item contained within when right clicked. You can set cooldowns for individual giver frames via `/ifg setCooldown`. Users with `ItemFrameGiver.admin` can remove giver frames by punching them.

## Commands

> All commands require the `ItemFrameGiver.admin` permission. `/ifg` is a valid alias for `/itemframegiver`.

### `/itemframegiver`

Shows the help message for the plugin.

### `/itemframegiver place`

Places a giver frame with the item in your hand.

### `/itemframegiver setCooldown none|<seconds>`

Sets the cooldown for a giver frame. Use `0` or `none` to remove the cooldown.

### `/itemframegiver clean`

Cleans up old giver frames whose counterpart entities have somehow been removed without notifying the event listeners.
