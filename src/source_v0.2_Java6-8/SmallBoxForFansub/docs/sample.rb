# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

puts "Hello World"

# Simply define numbers like that :             temps = 120
# And strings like this :                       syllabe = "ka"
# At the end, return the karaoke with
#   "{\\k" + temps.to_s + "}" + syllabe
#    [temps is a number and becomes a string
#     using the toString method 'to_s']

temps = 120
syllabe = "ka"

puts "{\\k" + temps.to_s + "}" + syllabe

# Here is the table '@names'.
@names = ["Albert", "Brenda", "Charles",
    "Dave", "Englebert"]

# Here is the manner to do a for each in ruby.
# For each 'name' of table '@names', we say to them Hello.
@names.each do |name|
  puts "Hello #{name}!"
end

# This line is very important if you want to use java code in ruby.
require 'java'

# Here is the way to make a JFrame from ruby.
JFrame = javax.swing.JFrame
JLabel = javax.swing.JLabel

frame = JFrame.new()
frame.getContentPane().add(JLabel.new("This is an example."))
frame.pack()
frame.setVisible(true)

# A small interaction with the java program.
# getTextFromScript diplays the passed text to the console.
# Asss = Java::smallboxforfansub.AssScripting
# asss = Asss.new()
# asss.getTextFromScript("This is an example.")

# We add a line in the table with :
Java::smallboxforfansub.scripting.ScriptPlugin.addOrgLine("Dialogue: 0,0:23:15.81,0:23:22.30,DTB Ending,,0000,0000,0000,,{\\alpha&h00&\\t(2090,12000,\\alpha&ff&)}{\\2a&HFF&\\fscx200\\fscy200\\t(0,640,\\fscx100\\fscy100)\\fad(100,0)}{\\k32}Ma{\\k38}yo{\\k41}u{\\k1} {\\k18}ko{\\k44}ko{\\k31}ro{\\k0} {\\k31}ga{\\k2} {\\k26}ki{\\k41}mi{\\k0} {\\k30}ni{\\k0} {\\k33}to{\\k32}do{\\k33}ku{\\k0} {\\K66}you{\\K0} {\\K150}ni{\\K0}")
# And get the line like that :
uno = Java::smallboxforfansub.scripting.ScriptPlugin.getOrgLine(0)
# Then print the same line in the console :
puts uno
# Then add the same line :
Java::smallboxforfansub.scripting.ScriptPlugin.addOrgLine(uno)

#require "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\tools.rb"
#require Java::smallboxforfansub.scripting.ScriptPlugin.getRubyScriptsPath()+"tools.rb"
#puts invert_color("H45DF72")
#puts darker_color("H45DF72",25)
#puts darker_color("H45DF72",50)
#puts lighter_color("H45DF72",25)
#puts lighter_color("H45DF72",50)
#count_syllables("{\\k42}gu{\\k77}n{\\k42}jo{\\k42}u {\\k35}o {\\k49}ma{\\k131}to{\\k72}u{\\k79} {\\k42}ya{\\k73}ma{\\k31}na{\\k51}mi {\\k51}o {\\k51}ko{\\k25}e{\\k150}te")# include Java

# import java.awt.GridLayout
# import javax.swing.JFrame
# import javax.swing.JButton
# import javax.swing.JPanel
# import javax.swing.JOptionPane


# class Example < JFrame
  
    # def initialize
        # super "Message boxes"
        
        # self.initUI
    # end
      
    # def initUI
      
        # panel = JPanel.new
        # panel.setLayout GridLayout.new 2, 2

        # errorButton = JButton.new "Error"
        # errorButton.addActionListener do |e|
            # JOptionPane.showMessageDialog panel, "Could not open file",
                # "Error", JOptionPane::ERROR_MESSAGE
        # end
        
        # warningButton = JButton.new "Warning"
        # warningButton.addActionListener do |e|
            # JOptionPane.showMessageDialog panel, "A deprecated call",
                # "Warning", JOptionPane::WARNING_MESSAGE
        # end
        
        # questionButton = JButton.new "Question"
        # questionButton.addActionListener do |e|
            # JOptionPane.showMessageDialog panel, "Are you sure to quit?",
                # "Question", JOptionPane::QUESTION_MESSAGE
        # end
        
        # informButton = JButton.new "Information"
        # informButton.addActionListener do |e|
            # JOptionPane.showMessageDialog panel, "Download completed",
                # "Information", JOptionPane::INFORMATION_MESSAGE
        # end
    
        # panel.add errorButton
        # panel.add warningButton
        # panel.add questionButton
        # panel.add informButton

        # self.add panel      
      
        
        # self.setDefaultCloseOperation JFrame::EXIT_ON_CLOSE
        # self.setSize 300, 200
        # self.setLocationRelativeTo nil
        # self.setVisible true
    # end
# end

# Example.new
