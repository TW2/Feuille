# 
# To change this template, choose Tools | Templates
# and open the template in the editor.

# This line is very important if you want to use java code in ruby.
# You must write it at every time.
require 'java'

# This is the line which register all your functions.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Fill","fill","1.0","","AssFxMaker owner (Chien-Rouge)","","")


# Note that the form of register is :
# rubyRegister(String name, String function, String version,
#    String description, String author, String firstlayer,
#    String nblayers)

# You can register a new effect (one effect = one function).
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Outline","outline","1.0","","AssFxMaker owner (Chien-Rouge)","","")


# Here is our first function
def fill
  # As we get a "k" karaoke ({\k30}su{\k30}ki {\k30}de{\k30}shou)
  # We can transform it directly in kf karaoke ({\kf30}su{\kf30}ki...)
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLine()
  line = line.gsub(/{\\k/, "{\\kf")
  Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
end

# Here is our second function
def outline
  # As we get a "k" karaoke ({\k30}su{\k30}ki {\k30}de{\k30}shou)
  # We can transform it directly in ko karaoke ({\ko30}su{\ko30}ki...)
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLine()
  line = line.gsub(/{\\k/, "{\\ko")
  Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
end