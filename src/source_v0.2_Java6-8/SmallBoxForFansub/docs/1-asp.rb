# This line is very important if you want to use java code in ruby.
# You must write it at every time.
require 'java'

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.aspRegister("Une ligne","oneline","1.0","Ajoute une ligne","AssFxMaker owner (Chien-Rouge)")

def oneline
	
	Java::smallboxforfansub.scripting.ScriptPlugin.add_M_To(0, 50)
	Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(50, 50)
	Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(25, 30)
	Java::smallboxforfansub.scripting.ScriptPlugin.add_B_To(-50, -75)
	Java::smallboxforfansub.scripting.ScriptPlugin.add_B_To(-75, -100, 200, 150, 150, 50)
	
end