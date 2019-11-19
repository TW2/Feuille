#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin

# Register the function in the main program.
ScriptPlugin.aspRegister("Change couches", "chglay", "1.0", "no description", "Chien-Rouge")

def chglay():
	ScriptPlugin.add_M_To(0, 0)
	ScriptPlugin.add_L_To(100, 0)
	ScriptPlugin.add_B_To(0, -25, 75, -50, 25, -50)
	ScriptPlugin.setLayerName("Couche 1")
	ScriptPlugin.setLayerColor(255, 0, 0)
	layertwo = ScriptPlugin.createLayer("", 0, 0, 255)
	ScriptPlugin.changeLayer(layertwo)
	ScriptPlugin.add_M_To(0, 0)
	ScriptPlugin.add_L_To(0, 100)
	ScriptPlugin.add_B_To(-25, 0, -50, 75, -50, 25)
	ScriptPlugin.changeLayer(0)
	ScriptPlugin.add_L_To(-100, 0)