require 'java'

# FORMAT : pluginname, displayname, function, version, description, author, help, type
# TYPE : function, macro, shortcut, command
Java::smallboxforfansub.scripting.ScriptPlugin.addButton("UGPlugin","User guide","userguide", "1.0", "Open the user guide.", "Chien-Rouge", "No help.","command")

def userguide
  # system("E:\\Dev\\Projets\\Java\\[AssFxMaker - autres documents]\\AssFxMaker Guide draft3.pdf")
  Java::smallboxforfansub.scripting.ScriptPlugin.openFile("E:\\Dev\\Projets\\Java\\[AssFxMaker - autres documents]\\AssFxMaker Guide draft3.pdf")
end

Java::smallboxforfansub.scripting.ScriptPlugin.addButton("MacroPlugin","Macro","macro", "1.0", "Ceci est une macro.", "Chien-Rouge", "Pas d'aide.","macro")

def macro
  puts "Hello World !! (Ruby)"
end

Java::smallboxforfansub.scripting.ScriptPlugin.addButton("ShortcutPlugin","Shortcut","shortcut", "1.0", "Ceci est un raccourci.", "Chien-Rouge", "Pas d'aide.","shortcut")

def shortcut
  puts "Hello World !! (Ruby)"
end

Java::smallboxforfansub.scripting.ScriptPlugin.addButton("CommandPlugin","Command","command", "1.0", "Ceci est une commande.", "Chien-Rouge", "Pas d'aide.","command")

def command
  puts "Hello World !! (Ruby)"
end