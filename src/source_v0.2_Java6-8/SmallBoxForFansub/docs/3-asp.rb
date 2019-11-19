# This line is very important if you want to use java code in ruby.
# You must write it at every time.
require 'java'

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.aspRegister("On a le choix","withmcq","1.0","Mise en forme par QCM (exemple)","AssFxMaker owner (Chien-Rouge)")

def withmcq
	
	# Use a dialog of AssSketchpad.
	dialog = Java::smallboxforfansub.scripting.MCQDialog
	mcq = dialog.new(nil,true)
	mcq.setLocationRelativeTo(nil)
	
	# Settings
	mcq.setQuestion("Voulez-vous...")	# Do you want...
	mcq.setAnswer1("Une ligne")			# A line
	mcq.setAnswer2("Un triangle")		# A triangle
	mcq.setAnswer3("Un carre")			# A square
	
	# An answer corresponding to one number
	# If we set the answer4 and if the user clicks on this 
	# (with the radiobutton) then showDialog returns the number 4.
	# setAnswer1 -> returns 1		setAnswer2 -> returns 2
	# setAnswer3 -> returns 3		setAnswer4 -> returns 4
	# setAnswer5 -> returns 5		setAnswer6 -> returns 6
	# There is 6 answers, not 7, it's enough.
	# If the user clicks on the button Cancel or if there is
	# an error then showDialog returns the number -1.
	
	# Wait for a numeric answer
	answer = mcq.showDialog()
	
	if (answer==1)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_M_To(0, 0)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(0, 100)
	elsif (answer==2)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_M_To(0, 0)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(0, 100)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(100, 100)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(0, 0)
	elsif (answer==3)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_M_To(0, 0)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(0, 100)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(100, 100)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(100, 0)
		Java::smallboxforfansub.scripting.ScriptPlugin.add_L_To(0, 0)
	end
	
end