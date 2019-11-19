# To change this template, choose Tools | Templates
# and open the template in the editor.

# This line is very important if you want to use java code in ruby.
# You must write it at every time.
require 'java'

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Up and down","upanddown","1.0","A simple wave","AssFxMaker owner (Chien-Rouge)","","")

def upanddown
  # Get a selected line (line = head + sentence)
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLine()
  
  # Get the sentence for this line
  sentence = Java::smallboxforfansub.scripting.ScriptPlugin.getSentence(line);

  # Get a table of syllable parameters for the basic karaoke
  # [i][0] : syllable                   string
  # [i][1] : hundredth                  integer
  # [i][2] : thousandth                 integer
  # [i][3] : thousandth (start)         integer
  # [i][4] : thousandth (end)           integer
  osyl = Java::smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence);

  # Create a new sentence
  newsentence = ""

  # Loop onto each syllable to create the sentence
  for i in 0..osyl.length-1 do

    syl = osyl[i][0]
    smiddle = (osyl[i][3]+osyl[i][2]/2).to_s()    # middle become a string
    sstart = osyl[i][3].to_s()                    # start become a string
    send = osyl[i][4].to_s()                      # end become a string

    # The first syllable may contains overrides
    if i==0
      newsentence = "{\\an8\\t("+sstart+","+smiddle+",\\fscx200\\fscy200)"+"\\t("+smiddle+","+send+",\\fscx100\\fscy100)}"+syl
    # All others syllables
    else
      newsentence = newsentence + "{\\r\\t("+sstart+","+smiddle+",\\fscx200\\fscy200)"+"\\t("+smiddle+","+send+",\\fscx100\\fscy100)}"+syl
    end
  end

  # Reformat the line
  head = Java::smallboxforfansub.scripting.ScriptPlugin.getHead(line);
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newsentence);
  
  # Add a new line to the table of the main program.
  Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
end

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Blue to green","bluetogreen","1.0","","AssFxMaker owner (Chien-Rouge)","","")

def bluetogreen
  
  # It's better to load the 'tools.rb' script by using the absolute path.
  # Here is a method to obtain the path without you have to change something else.
  # But if you prefer then type your absolute path :
  # Example on Windows : require "C:\\Programs\\AssFxMaker\\Effects\\tools.rb"
  require Java::smallboxforfansub.scripting.ScriptPlugin.getScriptsPath()+"tools.rb"

  # Get a selected line (line = head + sentence)
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLine()

  # Get the sentence for this line
  sentence = Java::smallboxforfansub.scripting.ScriptPlugin.getSentence(line);

  # Get a table of syllable parameters for the basic karaoke
  # [i][0] : syllable                   string
  # [i][1] : hundredth                  integer
  # [i][2] : thousandth                 integer
  # [i][3] : thousandth (start)         integer
  # [i][4] : thousandth (end)           integer
  osyl = Java::smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence);

  # Get a gradation of colors (these functions are in the 'tools.rb' script)
  @colors = gradation_of_colors("HFF3333","H33CC00",count_syllables(sentence))
  @invert = gradation_of_colors(invert_color("HFF3333"),invert_color("H33CC00"),count_syllables(sentence))

  # Create a new sentence
  newsentence = ""

  # Loop onto each syllable to create the sentence
  for i in 0..osyl.length-1 do

    syl = osyl[i][0]
    smiddle = (osyl[i][3]+osyl[i][2]/2).to_s()    # middle become a string
    sstart = osyl[i][3].to_s()                    # start become a string
    sextra = (osyl[i][4]+500).to_s()
    scolor = @colors[i].to_s()
    sinvert = @invert[i].to_s()

    # The first syllable may contains overrides
    if i==0
      newsentence = "{\\an8\\1c&"+sinvert+"&\\3c&HFFFFFF&\\t("+sstart+","+smiddle+",\\bord7\\blur7)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\1c&"+scolor+"&)}"+syl
    # All others syllables
    else
      newsentence = newsentence + "{\\r\\1c&"+scolor+"&\\3c&HFFFFFF&\\t("+sstart+","+sstart+",\\1c&"+sinvert+"&)\\t("+sstart+","+smiddle+",\\bord7\\blur7)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\1c&"+scolor+"&)}"+syl
    end
  end

  # Reformat the line
  head = Java::smallboxforfansub.scripting.ScriptPlugin.getHead(line);
  line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newsentence);

  # Add a new line to the table of the main program.
  Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
end

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Small grad (array mode)","smallgradation","1.0","","AssFxMaker owner (Chien-Rouge)","","")

def smallgradation
  # Load of the 'tools.rb' library.
  require Java::smallboxforfansub.scripting.ScriptPlugin.getScriptsPath()+"tools.rb"

  # Get all selected lines (line = head + sentence)
  @lines = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLines()
  
  # Use a dialog of AssFxMaker.
  dialog = Java::smallboxforfansub.scripting.AssColorDialog
  acd = dialog.new(nil,true)
  acd.setLocationRelativeTo(nil)
  
  # Set up the color 1 of the gradation
  color_one = acd.showDialog("FFFFFF")
  
  # Set up the color 2 of the gradation
  color_two = acd.showDialog("FFFFFF")
  
  # For each 'line' in @lines
  @lines.each do |line|
    # Get the sentence for this line
	sentence = Java::smallboxforfansub.scripting.ScriptPlugin.getSentence(line)
	
	# Get a table of syllable parameters for the basic karaoke
	# [i][0] : syllable                   string
	# [i][1] : hundredth                  integer
    # [i][2] : thousandth                 integer
    # [i][3] : thousandth (start)         integer
    # [i][4] : thousandth (end)           integer
    osyl = Java::smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence)
	
	# Get a gradation of colors (these functions are in the 'tools.rb' script)
	@border_colors = gradation_of_colors("H"+color_one,"H"+color_two,count_syllables(sentence))
	@text_colors = gradation_of_colors("H"+color_two,"H"+color_one,count_syllables(sentence))
	
	# Create a new sentence
    newsentence = ""
	
	# Loop onto each syllable to create the sentence
    for i in 0..osyl.length-1 do

      syl = osyl[i][0]
      smiddle = (osyl[i][3]+osyl[i][2]/2).to_s()    # middle become a string
      sstart = osyl[i][3].to_s()                    # start become a string
      sextra = (osyl[i][4]+500).to_s()
      sbcolor = @border_colors[i].to_s()
      stcolor = @text_colors[i].to_s()

      # The first syllable may contains overrides
      if i==0
        newsentence = "{\\an8\\org(-10000,640)\\1c&"+stcolor+"&\\3c&"+sbcolor+"&\\t("+sstart+","+smiddle+",\\bord7\\blur7\\frz0.02\\1c&HFFFFFF&)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\frz0\\1c&"+stcolor+"&)}"+syl
      # All others syllables
      else
        newsentence = newsentence + "{\\r\\1c&"+stcolor+"&\\3c&"+sbcolor+"&\\t("+sstart+","+smiddle+",\\bord7\\blur7\\frz0.02\\1c&HFFFFFF&)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\frz0\\1c&"+stcolor+"&)}"+syl
      end
    end

    # Reformat the line
    head = Java::smallboxforfansub.scripting.ScriptPlugin.getHead(line);
    line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newsentence);

    # Add a new line to the table of the main program.
    Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)	
  end
end

# Register the function into the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Wide grad (array mode)","widegradation","1.0","","AssFxMaker owner (Chien-Rouge)","","")

def widegradation
  # Load of the 'tools.rb' library.
  require Java::smallboxforfansub.scripting.ScriptPlugin.getScriptsPath()+"tools.rb"

  # Get all selected lines (line = head + sentence).
  @lines = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLines()
  
  # Ask for the width of video (you can define it in the option (default -> 1280)).
  video_width = Java::smallboxforfansub.scripting.ScriptPlugin.getVideoWidth()
  
  # Use a dialog of AssFxMaker.
  dialog = Java::smallboxforfansub.scripting.InputDialog
  id = dialog.new(nil,true)
  id.setLocationRelativeTo(nil)
  
  # Set up the ordinate 1 of the gradation
  id.setDialogMessage("Choose the ordinate to start the gradation.")
  id.setDialogTitle("Choose a number.")
  y_one = id.showDialog("20")
  
  # Set up the ordinate 2 of the gradation
  id.setDialogMessage("Choose the ordinate to end the gradation.")
  id.setDialogTitle("Choose a number.")
  y_two = id.showDialog("70")
  
  # Use a dialog of AssFxMaker.
  dialog = Java::smallboxforfansub.scripting.AssColorDialog
  acd = dialog.new(nil,true)
  acd.setLocationRelativeTo(nil)
  
  # Set up the color 1 of the gradation
  color_one = acd.showDialog("FFFFFF")
  
  # Set up the color 2 of the gradation
  color_two = acd.showDialog("0000FF")
  
  # For each 'line' in @lines
  @lines.each do |line|
    # Get the sentence for this line
	sentence = Java::smallboxforfansub.scripting.ScriptPlugin.getSentence(line)
	
	# Get a table of syllable parameters for the basic karaoke
	# [i][0] : syllable                   string
	# [i][1] : hundredth                  integer
    # [i][2] : thousandth                 integer
    # [i][3] : thousandth (start)         integer
    # [i][4] : thousandth (end)           integer
    osyl = Java::smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence)
	
	# Get a gradation of colors (these functions are in the 'tools.rb' script)
	@border_colors = gradation_of_colors("H"+color_one,"H"+color_two,count_syllables(sentence))
	@pixel_by_pixel = gradation_of_colors("H"+color_one,"H"+color_two,y_two.to_i()-y_one.to_i())
	
	# Create a new sentence
    newsentence = ""
	
	# Loop from y_one to y_two
	for j in y_one.to_i()..y_two.to_i() do
	  # Loop onto each syllable to create the sentence
      for i in 0..osyl.length-1 do

        syl = osyl[i][0]
        smiddle = (osyl[i][3]+osyl[i][2]/2).to_s()    # middle become a string
        sstart = osyl[i][3].to_s()                    # start become a string
        sextra = (osyl[i][4]+500).to_s()
        sbcolor = @border_colors[i].to_s()
        stcolor = @pixel_by_pixel[j-y_one.to_i()].to_s()

        # The first syllable may contains overrides
        if i==0
          newsentence = "{\\an8\\clip(0,"+j.to_s()+","+video_width.to_s()+","+(j+1).to_s()+")\\org(-10000,640)\\1c&"+stcolor+"&\\3c&"+sbcolor+"&\\t("+sstart+","+smiddle+",\\bord7\\blur7\\frz0.02\\1c&HFFFFFF&)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\frz0\\1c&"+stcolor+"&)}"+syl
        # All others syllables
        else
          newsentence = newsentence + "{\\r\\1c&"+stcolor+"&\\3c&"+sbcolor+"&\\t("+sstart+","+smiddle+",\\bord7\\blur7\\frz0.02\\1c&HFFFFFF&)"+"\\t("+smiddle+","+sextra+",\\bord2\\blur0\\frz0\\1c&"+stcolor+"&)}"+syl
        end
      end

      # Get the head
      head = Java::smallboxforfansub.scripting.ScriptPlugin.getHead(line)
	  
	  # Change the layer
	  head = Java::smallboxforfansub.scripting.ScriptPlugin.phChangeLayer(head, j.to_s())
	  
	  # Reformat the line
      line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newsentence)

      # Add a new line to the table of the main program.
      Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
	end
  end
end
