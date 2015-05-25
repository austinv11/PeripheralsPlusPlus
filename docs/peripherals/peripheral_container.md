# Peripheral Container

---

The peripheral container is a peripheral which allows for multiple peripherals to be compacted together into one block! Just craft the container with any peripheral(s) you wish to condense.

## Functions
| Function | Returns | Description |
|----------|---------|-------------|
|getContainedPeripherals()|_table_ List of contained peripherals|Gets the compacted peripherals in the block|
|wrapPeripheral(_string_ peripheralName)|_object_ Handle for the requested peripheral|Same as peripheral.wrap(), but for a peripheral in this block|