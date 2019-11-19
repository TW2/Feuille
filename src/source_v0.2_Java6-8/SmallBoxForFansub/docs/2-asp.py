#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin

# Register the function in the main program.
ScriptPlugin.aspRegister("Deux lignes", "twolines", "1.0", "no description", "author")

def twolines():
	ScriptPlugin.add_M_To(0, 50)
	ScriptPlugin.add_L_To(50, 50)
	ScriptPlugin.add_L_To(100, 50)
	ScriptPlugin.add_L_To(100, 100)
	ScriptPlugin.add_B_To(200, -250, 0, 0, -150, -200)