#!/usr/bin/env jython

from smallboxforfansub.scripting import ScriptPlugin
from smallboxforfansub.scripting import InputDialog

# Register the function in the main program.
ScriptPlugin.pythonRegister("myname", "myfunction", "1.0", "no description", "author", "", "")

# Register the function in the main program.
ScriptPlugin.pythonRegister("myname2", "myfunction2", "1.0", "no description", "author", "", "")

# Register the function in the main program.
ScriptPlugin.pythonRegister("Lightning (array mode)", "lightning", "1.0",
"Lightning effect with the font PowerOfGod.\nFrom the original Lua script for Aegisub of April 2009.", "AssFxMaker owner (Chien-Rouge)", "", "")

# Register the function in the main program.
ScriptPlugin.addTheme("5B5B5B", "999999", "DeepGray")

def myfunction():
    print "Hello World !! A (jython)"



def myfunction2():
    print "Hello World !! B (jython)"
    # Test
    ScriptPlugin.addResLine("Dialogue: 0,0:01:23.77,0:01:29.67,Default,,0000,0000,0000,,Je suis là !")


def lightning():
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
        
        ScriptPlugin.regenerateAssStyleCollection()
        assstyle = ScriptPlugin.getAssStyle(line)
        linewidth = ScriptPlugin.phXSentenceWidth(osyl, assstyle) # integer
        pxArray = ScriptPlugin.phPosXSyllable(osyl, assstyle) # integer
        
        leftstartpos = (int(videowidth)-linewidth)/2
        
        # ======================================================================
        # Layer 1 : Disappearance of syllable when pronounced
        # ======================================================================
    
        # Create a new sentence
        newsentence = ""

        # Counter
        counter = 0

        # Loop onto each syllable to create the sentence
        while counter<len(osyl):

            syl = osyl[counter][0]
            sstart = str(osyl[counter][3])              # start become a string

            # The first syllable may contains overrides
            if counter==0:
                newsentence = "{\\an5\\pos("+posx+","+posy+")\\alpha&HFF&}"+syl
            # All others syllables
            else:
                newsentence = newsentence + "{\\r\\t("+sstart+","+sstart+",\\alpha&HFF&)}"+syl

            counter += 1

        # Reformat the line        
        line = ScriptPlugin.getAssLineOf(head, newsentence)

        # Add a new line to the table of the main program.
        ScriptPlugin.addResLine(line)
        
        # ======================================================================
        # Layer 2 : Appearance of syllable when pronounced
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
                newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")\\fscx200\\fscy200\\1c&HFFD700&"
                newsentence = newsentence + "\\t("+send+","+str(osyl[counter][4]+600)+",0.5,\\fscx100\\fscy100\\1c&HFFFFFF&)}"+syl
                line = ScriptPlugin.getAssLineOf(head, newsentence) # Reformat the line
                ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.
            # All others syllables
            else:
                newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")\\alpha&HFF&\\t("+sstart+","+sstart+",\\fscx200\\fscy200\\1c&HFFD700&\\alpha&H00&)"
                newsentence = newsentence + "\\t("+send+","+str(osyl[counter][4]+600)+",0.5,\\fscx100\\fscy100\\1c&HFFFFFF&)}"+syl
                line = ScriptPlugin.getAssLineOf(head, newsentence) # Reformat the line
                ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.

            counter += 1
            
        # ======================================================================
        # Layer 3 : Blur
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
                newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")\\fscx200\\fscy200\\blur10\\be1\\bord1\\3c&HFAE8C5&\\alpha&HFF&\\3a&H00&"
                newsentence = newsentence + "\\t("+send+","+str(osyl[counter][4]+600)+",0.5,\\fscx100\\fscy100\\3a&HFF&)}"+syl
                line = ScriptPlugin.getAssLineOf(head, newsentence) # Reformat the line
                ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.
            # All others syllables
            else:
                newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")\\alpha&HFF&\\3c&HFAE8C5&\\blur10\\be1\\bord1"
                newsentence = newsentence + "\\t("+sstart+","+sstart+",\\fscx200\\fscy200\\3a&H00&)"
                newsentence = newsentence + "\\t("+send+","+str(osyl[counter][4]+600)+",0.5,\\fscx100\\fscy100\\3a&HFF&)}"+syl
                line = ScriptPlugin.getAssLineOf(head, newsentence) # Reformat the line
                ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.

            counter += 1
            
        # ======================================================================
        # Layer 4 : Drawings (lightning)
        # ======================================================================
    
        # Create a new sentence
        newsentence = ""

        # Counter
        counter = 0
        
        sfx = ["a","b","c","d"]

        # Loop onto each syllable to create the sentence
        while counter<len(osyl):

            syl = osyl[counter][0]
            smiddle = str(osyl[counter][3]+osyl[counter][2]/2)       # middle become a string
            sstart = str(osyl[counter][3])                           # start become a string
            send = str(osyl[counter][4])                             # end become a string

            # The first syllable may contains overrides
            if counter==0:
                kounter = 20
                layer = 0
                loopfx = 0
                
                # str(kounter) represents start - str(kounter+20) represents end - period is 20ms
                # There is at each 20ms a new lightning until the end of the time of the syllable
                while kounter<osyl[counter][2]:
                    
                    layer += 1
                    newhead = ScriptPlugin.phChangeLayer(head, str(layer))                    
                    
                    if kounter==0:                        
                        newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")"
                        newsentence = newsentence + "\\fscx200\\fscy200\\fnPowerOfGod\\bord1\\3a&H7F&\\1a&H7F&\\3c&HFFFFFF&"
                        newsentence = newsentence + "\\t("+str(kounter+20)+","+str(kounter+20)+",\\alpha&HFF&)}"+sfx[loopfx]
                    else:
                        newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")"                        
                        newsentence = newsentence + "\\fscx200\\fscy200\\fnPowerOfGod\\bord1\\3a&H7F&\\1a&H7F&\\3c&HFFFFFF&"
                        newsentence = newsentence + "\\frz"+str(kounter)+"\\alpha&HFF&"
                        newsentence = newsentence + "\\t("+str(kounter)+","+str(kounter)+",\\3a&H7F&\\1a&H7F&)"
                        newsentence = newsentence + "\\t("+str(kounter+20)+","+str(kounter+20)+",\\alpha&HFF&)}"+sfx[loopfx]
                    kounter += 20
                    
                    line = ScriptPlugin.getAssLineOf(newhead, newsentence) # Reformat the line
                    ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.
                    
                    loopfx += 1
                    if loopfx==4:
                        loopfx = 0
                    
            # All others syllables
            else:
                kounter = 20
                layer = 0
                loopfx = 0
                
                # str(kounter) represents start - str(kounter+20) represents end - period is 20ms
                # There is at each 20ms a new lightning until the end of the time of the syllable
                while kounter<osyl[counter][2]:
                    
                    layer += 1
                    newhead = ScriptPlugin.phChangeLayer(head, str(layer))                    
                    
                    if kounter==0:                        
                        newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")"
                        newsentence = newsentence + "\\fscx200\\fscy200\\fnPowerOfGod\\bord1\\3a&H7F&\\1a&H7F&\\3c&HFFFFFF&"
                        newsentence = newsentence + "\\t("+str(osyl[counter][3]+kounter+20)+","+str(osyl[counter][3]+kounter+20)+",\\alpha&HFF&)}"+sfx[loopfx]
                    else:
                        newsentence = "{\\an5\\pos("+str(pxArray[counter]+leftstartpos)+","+posy+")"                        
                        newsentence = newsentence + "\\fscx200\\fscy200\\fnPowerOfGod\\bord1\\3a&H7F&\\1a&H7F&\\3c&HFFFFFF&"
                        newsentence = newsentence + "\\frz"+str(kounter)+"\\alpha&HFF&"
                        newsentence = newsentence + "\\t("+str(osyl[counter][3]+kounter)+","+str(osyl[counter][3]+kounter)+",\\3a&H7F&\\1a&H7F&)"
                        newsentence = newsentence + "\\t("+str(osyl[counter][3]+kounter+20)+","+str(osyl[counter][3]+kounter+20)+",\\alpha&HFF&)}"+sfx[loopfx]
                    kounter += 20
                    
                    line = ScriptPlugin.getAssLineOf(newhead, newsentence) # Reformat the line
                    ScriptPlugin.addResLine(line) # Add a new line to the table of the main program.
                    
                    loopfx += 1
                    if loopfx==4:
                        loopfx = 0

            counter += 1
