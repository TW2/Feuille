#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin

# Register the function in the main program.
ScriptPlugin.aspRegister("Boite sur pattes 2", "littlebox2", "1.0", "no description", "AssFxMaker owner (Chien-Rouge)")

def littlebox2():
	# m 0 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -125 0 l -115 0 l -90 -50 l -35 -50 l -10 0
	ScriptPlugin.setLayerName("Sprite 01")
	ScriptPlugin.setLayerColor(0, 255, 0)
	ScriptPlugin.addASSCommands("m 0 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -125 0 l -115 0 l -90 -50 l -35 -50 l -10 0")
	
	# m -25 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -100 0 l -90 0 l -90 -50 l -35 -50 l -35 0 
	layertwo = ScriptPlugin.createLayer("Sprite 02", 255, 0, 0)
	ScriptPlugin.changeLayer(layertwo)
	ScriptPlugin.addASSCommands("m -25 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -100 0 l -90 0 l -90 -50 l -35 -50 l -35 0")
	
	# m -50 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -75 0 l -65 0 l -90 -50 l -35 -50 l -60 0
	layerthr = ScriptPlugin.createLayer("Sprite 03", 0, 0, 255)
	ScriptPlugin.changeLayer(layerthr)
	ScriptPlugin.addASSCommands("m -50 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -75 0 l -65 0 l -90 -50 l -35 -50 l -60 0")