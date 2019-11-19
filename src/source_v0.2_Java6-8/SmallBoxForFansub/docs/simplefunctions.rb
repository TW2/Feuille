# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
require 'java'
JFrame = javax.swing.JFrame
JLabel = javax.swing.JLabel

def left
  frame = JFrame.new()
  frame.getContentPane().add(JLabel.new("Your choice is the left."))
  frame.pack()
  frame.setVisible(true)
end

def right
  frame = JFrame.new()
  frame.getContentPane().add(JLabel.new("Your choice is the right."))
  frame.pack()
  frame.setVisible(true)
end