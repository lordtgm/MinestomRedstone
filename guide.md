here is a list of how stuff here work:
there is a "redstone handler" interface which is the base interface for all those redstone stuff.
(and let's just say it here, they aren't "block handlers", just handlers)
* component redstone handler which is for redstone components, meaning anything related to redstone
* solid redstone handler which is for solid blocks
* solid component redstone handler 
lets also explain this here, handlers aren't bound to blocks in any way, there is a method for knowing what handler does a block use.

also there are three tags
* strong power level: stores the strong power level of a solid block. any component which isn't redstone dust provides solid blocks with strong power. any component near a strongly powered block will draw power from it (given the block is in an input direction of the component)
* weak power level: stores the weak power level of a solid block. redstone dust provides solid blocks with weak power. any component which isn't redstone dust will draw power from an adjacent weakly powered block.
* component power level: stores the component power level of a component block. this is the amount of power which a component delivers through its outputs. solid blocks or components with matching input direction will also be powered by this.

so a bit of explanation:
transmit of power from a solid block to a component is controlled by the component. it can decide how to use the power amount, while the solid block cannot decide how much power it will be delivering.
transmit of power from a component to a solid block is controlled by the component. same goes here
transmit of power from a component to a component is controlled by both sides. the amount is decided by the source, while what happens to this amount is decided by the destination.
(and no, there is no solid block to solid block)
for solid components, I'm not sure, it's incomplete, and it's because I've not run into such block yet. not even sure if there is such a thing.

and here we go to explain each method does
the void update, calls updatePower, and if the block's power was changed, it updates the block's neighbors through the updateNearby void.
the neighbors of a block is determined by the method getNeighbors. the methods updatePower and getNeighbors may be overridden by implementations or sub interfaces.
static methods getDirectionOf and getRelativeLocation are for utility.

next interface we talk about is ComponentRedstoneHandler
getPowerDelivery determines how much power a component delivers to the target direction. it's not recommended to override
doesStronglyPower is a boolean which determines if the component transmits strong power or weak power. it also means if the component can draw power from a weakly powered block or not.
updatePower here is overridden to return the result of updateComponentPowerLevel, it's not recommended to override.
updateComponentPowerLevel updates the power level of this component. this may be overridden. also I might change this method as it's not really useful. maybe for getting how much power is received from an input direction.
powerInputs determines which sides of this block can get power as input.
doesDeliverPower determines if this block delivers power to other blocks. it should be provided by implementations

SolidRedstoneHandler interface is pretty straightforward and has no real thing so I just skip it.

