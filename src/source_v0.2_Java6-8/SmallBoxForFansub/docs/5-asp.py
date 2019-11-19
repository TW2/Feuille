#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin

# Register the function in the main program.
ScriptPlugin.aspRegister("Boite sur pattes", "littlebox", "1.0", "no description", "AssFxMaker owner (Chien-Rouge)")

def littlebox():
	# m 0 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -125 0 l -115 0 l -90 -50 l -35 -50 l -10 0
	ScriptPlugin.setLayerName("Sprite 01")
	ScriptPlugin.setLayerColor(0, 255, 0)
	ScriptPlugin.add_M_To(0, 0)
	ScriptPlugin.add_L_To(-25, -50)
	ScriptPlugin.add_L_To(-25, -100)
	ScriptPlugin.add_L_To(-100, -100)
	ScriptPlugin.add_L_To(-100, -50)
	ScriptPlugin.add_L_To(-125, 0)
	ScriptPlugin.add_L_To(-115, 0)
	ScriptPlugin.add_L_To(-90, -50)
	ScriptPlugin.add_L_To(-35, -50)
	ScriptPlugin.add_L_To(-10, 0)
	
	# m -25 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -100 0 l -90 0 l -90 -50 l -35 -50 l -35 0 
	layertwo = ScriptPlugin.createLayer("Sprite 02", 255, 0, 0)
	ScriptPlugin.changeLayer(layertwo)
	ScriptPlugin.add_M_To(-25, 0)
	ScriptPlugin.add_L_To(-25, -50)
	ScriptPlugin.add_L_To(-25, -100)
	ScriptPlugin.add_L_To(-100, -100)
	ScriptPlugin.add_L_To(-100, -50)
	ScriptPlugin.add_L_To(-100, 0)
	ScriptPlugin.add_L_To(-90, 0)
	ScriptPlugin.add_L_To(-90, -50)
	ScriptPlugin.add_L_To(-35, -50)
	ScriptPlugin.add_L_To(-35, 0)
	
	# m -50 0 l -25 -50 l -25 -100 l -100 -100 l -100 -50 l -75 0 l -65 0 l -90 -50 l -35 -50 l -60 0
	layerthr = ScriptPlugin.createLayer("Sprite 03", 0, 0, 255)
	ScriptPlugin.changeLayer(layerthr)
	ScriptPlugin.add_M_To(-50, 0)
	ScriptPlugin.add_L_To(-25, -50)
	ScriptPlugin.add_L_To(-25, -100)
	ScriptPlugin.add_L_To(-100, -100)
	ScriptPlugin.add_L_To(-100, -50)
	ScriptPlugin.add_L_To(-75, 0)
	ScriptPlugin.add_L_To(-65, 0)
	ScriptPlugin.add_L_To(-90, -50)
	ScriptPlugin.add_L_To(-35, -50)
	ScriptPlugin.add_L_To(-60, 0)