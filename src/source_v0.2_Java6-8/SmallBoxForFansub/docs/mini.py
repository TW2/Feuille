from smallboxforfansub.scripting import ScriptPlugin

ScriptPlugin.pythonRegister("myname", "myfunction", "1.0", "no description", "author", "", "")

def myfunction():
    print "Hello World !! A (jython)"
	
