# To change this template, choose Tools | Templates
# and open the template in the editor.
# author : Chien-Rouge in 2011/01 for AssFxMaker/Funsub Project

# ---------------------------- CONVERSION ----------------------------
# Hexadecimal to Decimal conversion.
def hex_to_dec(hexValue)
  return hexValue.to_i(16)
end

# Decimal to Hexadecimal conversion.
def dec_to_hex(decValue)
  return decValue.to_s(16)
end

# ------------------------------- COLOR ------------------------------
# Get a color from Decimal and return a Hexadecimal in Hbbggrr format.
def create_color(red,green,blue)
  # Decimal to Hexadecimal conversion
  b = blue.to_s(16)
  g = green.to_s(16)
  r = red.to_s(16)

  # Reformat to the good value
  if b.length == 1
    b = "0".concat(b)
  end
  if g.length == 1
    g = "0".concat(g)
  end
  if r.length == 1
    r = "0".concat(r)
  end

  b = b.upcase
  g = g.upcase
  r = r.upcase

  return "H"+b+g+r
end

# Get an ass color and return invert ass color.
# Require value in Hexadecimal in Hbbggrr format.
# Return value in Hexadecimal in Hbbggrr format.
def invert_color(assColor)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")

  # Get and invert colors (with Hexadecimal to Decimal conversion)
  blue = 255-reg.match(assColor)[1].to_i(16)
  green = 255-reg.match(assColor)[2].to_i(16)
  red = 255-reg.match(assColor)[3].to_i(16)

  return create_color(red,green,blue)
end

# Get the red value from a color.
# Require value in Hexadecimal in Hbbggrr format.
# Return value in Decimal.
def extract_red(assColor)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")
  return reg.match(assColor)[3].to_i(16)
end

# Get the green value from a color.
# Require value in Hexadecimal in Hbbggrr format.
# Return value in Decimal.
def extract_green(assColor)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")
  return reg.match(assColor)[2].to_i(16)
end

# Get the blue value from a color.
# Require value in Hexadecimal in Hbbggrr format.
# Return value in Decimal.
def extract_blue(assColor)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")
  return reg.match(assColor)[1].to_i(16)
end

# Create a gradation and return it in a table of colors in Hbbggrr format.
def gradation_of_colors(assColor1,assColor2,subdivision)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")

  # Get colors (with Hexadecimal to Decimal conversion)
  blue1 = reg.match(assColor1)[1].to_i(16)
  green1 = reg.match(assColor1)[2].to_i(16)
  red1 = reg.match(assColor1)[3].to_i(16)

  blue2 = reg.match(assColor2)[1].to_i(16)
  green2 = reg.match(assColor2)[2].to_i(16)
  red2 = reg.match(assColor2)[3].to_i(16)

  # Calculation of the difference
	rdiff = red2-red1
	gdiff = green2-green1
	bdiff = blue2-blue1

  # Calculation of the delta from subdivision
	rdelta = rdiff/subdivision
	gdelta = gdiff/subdivision
	bdelta = bdiff/subdivision

  # Create the variables
  @gradation = Array.new(subdivision)
  i = 0

  # Find values for the gradation
  while i < subdivision do
    r = red1+(rdelta*i)
		g = green1+(gdelta*i)
		b = blue1+(bdelta*i)
    
    if (r<0)
      r=0
    elsif (r>255)
      r=255
    end

    if (b<0)
      b=0
    elsif (b>255)
      b=255
    end

    if (g<0)
      g=0
    elsif (g>255)
      g=255
    end

    @gradation[i] = create_color(r,g,b)

    i = i+1
  end

  return @gradation
end

# Get a color in Hbbggrr format and return a darker color in Hbbggrr format.
def darker_color(assColor,percent)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")

  # Get colors (with Hexadecimal to Decimal conversion)
  blue = reg.match(assColor)[1].to_i(16)
  green = reg.match(assColor)[2].to_i(16)
  red = reg.match(assColor)[3].to_i(16)

  # Darker by applying a ratio
  r = red-percent*255/100
  g = green-percent*255/100
  b = blue-percent*255/100

#  puts "Value of r : ".concat(r.to_s)+" ; Before : ".concat(red.to_s)
#  puts "Value of g : ".concat(g.to_s)+" ; Before : ".concat(green.to_s)
#  puts "Value of b : ".concat(b.to_s)+" ; Before : ".concat(blue.to_s)

  if (r<0)
    r=0
  elsif (r>255)
    r=255
  end

  if (b<0)
    b=0
  elsif (b>255)
    b=255
  end

  if (g<0)
    g=0
  elsif (g>255)
    g=255
  end

  return create_color(r,g,b)
end

# Get a color in Hbbggrr format and return a lighter color in Hbbggrr format.
def lighter_color(assColor,percent)
  # Regular expression for colors Blue, Green and Red
  reg = Regexp.compile("H([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")

  # Get colors (with Hexadecimal to Decimal conversion)
  blue = reg.match(assColor)[1].to_i(16)
  green = reg.match(assColor)[2].to_i(16)
  red = reg.match(assColor)[3].to_i(16)

  # Lighter by applying a ratio
  r = red+percent*255/100
  g = green+percent*255/100
  b = blue+percent*255/100

#  puts "Value of r : ".concat(r.to_s)+" ; Before : ".concat(red.to_s)
#  puts "Value of g : ".concat(g.to_s)+" ; Before : ".concat(green.to_s)
#  puts "Value of b : ".concat(b.to_s)+" ; Before : ".concat(blue.to_s)

  if (r<0)
    r=0
  elsif (r>255)
    r=255
  end

  if (b<0)
    b=0
  elsif (b>255)
    b=255
  end

  if (g<0)
    g=0
  elsif (g>255)
    g=255
  end

  return create_color(r,g,b)
end

# ------------------------------- ALPHA ------------------------------
# Get an alpha from Decimal and return a Hexadecimal in Hxx format.
def create_alpha(alpha)
  # Decimal to Hexadecimal conversion
  a = alpha.to_s(16)

  # Reformat to the good value
  if a.length == 1
    a = "0".concat(a)
  end

  a = a.upcase

  return "H"+a
end

# Get an alpha in Hxx format and return the inverted in Hxx format.
def invert_alpha(assAlpha)
  reg = Regexp.compile("H([0-9a-fA-F]{2})")
  a = 255-reg.match(assAlpha)[1].to_i(16)
  return create_alpha(a)
end

# Get the alpha value.
# Require value in Hexadecimal in Hxx format.
# Return value in Decimal.
def extract_alpha(assAlpha)
  reg = Regexp.compile("H([0-9a-fA-F]{2})")
  return reg.match(assAlpha)[1].to_i(16)
end

# Create a gradation and return it in a table of alphas in Hxx format.
def gradation_of_alphas(assAlpha1,assAlpha2,subdivision)
  # Regular expression for alphas
  reg = Regexp.compile("H([0-9a-fA-F]{2})")

  alpha1 = reg.match(assAlpha1)[1].to_i(16)
  alpha2 = reg.match(assAlpha2)[1].to_i(16)

  # Calculation of the difference
	alphadiff = alpha2-alpha1

  # Calculation of the delta from subdivision
	alphadelta = alphadiff/subdivision

  # Create the variables
  @gradation = Array.new(subdivision)
  i = 0

  # Find values for the gradation
  while i < subdivision do
    a = alpha1+(alphadelta*i)

    if (a<0)
      a=0
    elsif (a>255)
      a=255
    end

    @gradation[i] = create_alpha(a)

    i = i+1
  end

  return @gradation
end
# ------------------------------- COUNT ------------------------------

# How many syllables we have in sentence.
def count_syllables(sentence)
  nb_syl = sentence.split("\\k")
  return nb_syl.length-1
end
