#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin
from smallboxforfansub.scripting import InputDialog

# Register the function in the main program.
ScriptPlugin.pythonRegister("Violet (array mode)", "violetandblood", "1.0", "no description", "Chien-Rouge", "", "")

def violetandblood():
    # Get selected line (line = head + sentence)
    lines = ScriptPlugin.getSelectedOrgLines()
    
    # Ask for size of the video
    dialog = InputDialog(None,True)
    dialog.setLocationRelativeTo(None)
    dialog.setDialogMessage("Choose the video width to set up the karaoke.")
    dialog.setDialogTitle("Choose a size.")
    videowidth = dialog.showDialog("1280") # string
    dialog.setDialogMessage("Choose the video height to set up the karaoke.")
    dialog.setDialogTitle("Choose a size.")
    videoheight = dialog.showDialog("720") # string
    
    posx = str(int(videowidth)/2)
    posy = str(int(videoheight)/18)
    
    for line in lines:
        # Get the sentence for this line
        sentence = ScriptPlugin.getSentence(line)
        
        # Get the head for this line
        head = ScriptPlugin.getHead(line)

        # Get a table of syllable parameters for the basic karaoke
        # [i][0] : syllable                   string
        # [i][1] : hundredth                  integer
        # [i][2] : thousandth                 integer
        # [i][3] : thousandth (start)         integer
        # [i][4] : thousandth (end)           integer
        osyl = ScriptPlugin.phKaraoke(sentence)
        
        # ======================================================================
        # Layer 1 : Change color
        # ======================================================================
    
        # Create a new sentence
        newsentence = ""

        # Counter
        counter = 0

        # Loop onto each syllable to create the sentence
        while counter<len(osyl):

            syl = osyl[counter][0]
            smiddle = str(osyl[counter][3]+osyl[counter][2]/2)       # middle become a string
            sstart = str(osyl[counter][3])                           # start become a string
            send = str(osyl[counter][4])                             # end become a string

            # The first syllable may contains overrides
            if counter==0:
                newsentence = "{\\an5\\pos("+posx+","+posy+")\\1c&H0000CC&\\3c&HFFFFFF&\\t("+sstart+","+smiddle+",\\1c&H66CCFF&\\3c&H660066&)\\t("+smiddle+","+send+",\\1c&H0000CC&\\3c&HFFFFFF&)}"+syl
            # All others syllables
            else:
                newsentence = newsentence + "{\\r\\1c&H0000CC&\\3c&HFFFFFF&\\t("+sstart+","+smiddle+",\\1c&H66CCFF&\\3c&H660066&)\\t("+smiddle+","+send+",\\1c&H0000CC&\\3c&HFFFFFF&)}"+syl

            counter += 1

        # Reformat the line        
        line = ScriptPlugin.getAssLineOf(head, newsentence)

        # Add a new line to the table of the main program.
        ScriptPlugin.addResLine(line)
        
        
        # ======================================================================
        # Layer 2 : Larger and larger (size 1)
        # ======================================================================
    
        # Create a new sentence
        newsentence = ""

        # Counter
        counter = 0

        # Loop onto each syllable to create the sentence
        while counter<len(osyl):

            syl = osyl[counter][0]
            smiddle = str(osyl[counter][3]+osyl[counter][2]/2)       # middle become a string
            sstart = str(osyl[counter][3])                           # start become a string
            send = str(osyl[counter][4])                             # end become a string

            # The first syllable may contains overrides
            if counter==0:
                newsentence = "{\\an5\\pos("+posx+","+posy+")\\3c&H660066&\\alpha&HFF&\\3a&H00&\\t("+sstart+","+smiddle+",\\fscx225\\fscy225)\\t("+smiddle+","+send+",\\fscx100\\fscy100)\\t("+send+","+send+",\\3a&HFF&)}"+syl
            # All others syllables
            else:
                newsentence = newsentence + "{\\r\\3c&H660066&\\alpha&HFF&\\t("+sstart+","+sstart+",\\3a&H00&)\\t("+sstart+","+smiddle+",\\fscx225\\fscy225)\\t("+smiddle+","+send+",\\fscx100\\fscy100)\\t("+send+","+send+",\\3a&HFF&)}"+syl

            counter += 1

        # Reformat the line        
        line = ScriptPlugin.getAssLineOf(head, newsentence)

        # Add a new line to the table of the main program.
        ScriptPlugin.addResLine(line)
        
        # ======================================================================
        # Layer 3 : Larger and larger (size 2)
        # ======================================================================
    
        # Create a new sentence
        newsentence = ""

        # Counter
        counter = 0

        # Loop onto each syllable to create the sentence
        while counter<len(osyl):

            syl = osyl[counter][0]
            smiddle = str(osyl[counter][3]+osyl[counter][2]/2)       # middle become a string
            sstart = str(osyl[counter][3])                           # start become a string
            send = str(osyl[counter][4])                             # end become a string

            # The first syllable may contains overrides
            if counter==0:
                newsentence = "{\\an5\\pos("+posx+","+posy+")\\3c&H660066&\\alpha&HFF&\\3a&H00&\\t("+sstart+","+smiddle+",\\fscx300\\fscy300)\\t("+smiddle+","+send+",\\fscx100\\fscy100)\\t("+send+","+send+",\\3a&HFF&)}"+syl
            # All others syllables
            else:
                newsentence = newsentence + "{\\r\\3c&H660066&\\alpha&HFF&\\t("+sstart+","+sstart+",\\3a&H00&)\\t("+sstart+","+smiddle+",\\fscx300\\fscy300)\\t("+smiddle+","+send+",\\fscx100\\fscy100)\\t("+send+","+send+",\\3a&HFF&)}"+syl

            counter += 1

        # Reformat the line        
        line = ScriptPlugin.getAssLineOf(head, newsentence)

        # Add a new line to the table of the main program.
        ScriptPlugin.addResLine(line)
        


