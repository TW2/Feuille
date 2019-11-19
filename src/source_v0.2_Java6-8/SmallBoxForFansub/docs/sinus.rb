require 'java'

# Register the function in the main program.
Java::smallboxforfansub.scripting.ScriptPlugin.rubyRegister("Sinus (array mode)","sinusoidale","1.0",
  "Karaoke on emulated curve.\nFrom the original Visual Basic script of March-April 2008.","AssFxMaker owner (Chien-Rouge)","","")

# Nous allons représenter un mouvement sinusoïdale à l'aide de mouvement \move(x1,y1,x2,y2,t1,t2).
# Nous utiliserons des points clés afin d'émuler le mouvement.
# Ces points seront situés à:
# -  0    -  1/8  -  3/16 -  1/4  -  5/16 -  3/8  -  1/2
# -  1/2  -  5/8  - 11/16 -  3/4  - 13/16 -  7/8  -  1
# Nous allons donc remplir un tableau de valeurs pour une période.
# Nous avons besoin de savoir:
# - quelle est la résolution de l'écran {resolution}.
# - à quel point commencer la période {debut} (point de référence).
# - à quel point finir la période {fin}.
# - le temps de début
# - le temps de fin
# - l'amplitude maximum (calcul de y si pas maximum)
# - la ligne de base
#-------------------------------------------------------------------------------------------------------  
# Rappel de maths - relations:
# Pour tout point A de l'espace 2D n'étant pas l'origine O, ses projections orthogonales sur l'axe x et y
# dessinent deux triangles rectangles: OAxA et OAyA.
# Nous pourrons donc trouver les coordonnées grâce à la trigonométrie:
# cos a = OAx / OA
# sin a = AxA / OA

# Pour convertir degrés en radians >> multiplier par pi/180
# Pour convertir radians en degrés >> multiplier par 180/pi

# On recherchera la longueur AxA qui est notre ordonnée y (sachant qu'on connait x, on aura A[x,y])
# AxA = sin a / OA = sin (alpha x pi/180) / OA

# Dim xDebut, xFinPeriode, periode, amp, ligneBase As Integer

# Gakuen Utopia Manabi Straight : on prend le point du milieu de la résolution (960x720) c'est à dire 480
# comme point clé 3/4 de T.
# On veut essayer une période de 480
# 3/4 x 480 = 360 ; 480-360=120 ; 480+120=600
# xDebut = 120
# xFin = 600

# xDebut = 120
# xFinPeriode = 600
# periode = 480
# amp = 50
# ligneBase = 80

# Tableau de valeurs pour une période
# Dim tablePeriode(,) As Integer = FaireTablePeriode(xDebut, periode, amp, ligneBase)

# For i As Integer = 0 To 960 Step 10

# Next

# NOUVEAUX PARAMETRAGES

# ligne de base = 100 et amplitude = 80 (20-100-180)
# -  0    -  1/8  -  3/16 -  1/4  -  5/16 -  3/8  -  1/2
# -  1/2  -  5/8  - 11/16 -  3/4  - 13/16 -  7/8  -  1

# calcul des positions y avec la fonction sinus :

# 0 = pi        :       0       >> 100
# 1/12 (pi/6)   :       0.5     >> 100 + 0.5 x 80 = 100 + 40 = 140 ou 100 - 40 = 60
# 1/8 (pi/4)    :       0.707   >> 100 + 0.707 x 80 = 100 + 56.56 = 156.56 = 157 ou 100 - 57 = 43
# 3/16 (3pi/8)  :       0.924   >> 100 + 0.924 x 80 = 100 + 73.92 = 173.92 = 174 ou 100 - 74 = 26
# 1/4 (pi/2)    :       1       >> 100 + 1 x 80 = 100 + 80 = 180 ou 100 - 80 = 20

# Valeurs :
# 100 > 140 > 157 > 174 > 180 > 174 > 157 > 140
# > 100 > 60 > 43 > 26 > 20 > 26 > 43 > 60

# mouvement [amplitude type | temps]

# /move(960,100,900,157) [0 à -1/8 | 500]
# /move(900,157,870,174) [-1/8 à -3/16 | 250]
# /move(870,174,840,180) [-3/16 à -1/4 | 250] << fin fondu début
# /move(840,180,810,174) [-1/4 à -3/16 | 250] << début totalement visible
# /move(810,174,780,157) [-3/16 à -1/8 | 250]
# /move(780,157,720,100) [-1/8 à 0 | 500]
# /move(720,100,660,43) [0 à 1/8 | 500]
# /move(660,43,630,26) [1/8 à 3/16 | 250]
# /move(630,26,600,20) [3/16 à 1/4 | 250]
# /move(600,20,570,26) [1/4 à 3/16 | 250] << point de départ syllabe
# /move(570,26,540,43) [3/16 à 1/8 | 250]
# /move(540,43,480,100) [1/8 à 0 | 500]
# /move(480,100,420,157) [0 à -1/8 | 500]
# /move(420,157,390,174) [-1/8 à -3/16 | 250]
# /move(390,174,360,180) [-3/16 à -1/4 | 250]
# /move(360,180,330,174) [-1/4 à -3/16 | 250]
# /move(330,174,300,157) [-3/16 à -1/8 | 250]
# /move(300,157,240,100) [-1/8 à 0 | 500]
# /move(240,100,180,43) [0 à 1/8 | 500]
# /move(180,43,150,26) [1/8 à 3/16 | 250]
# /move(150,26,120,20) [3/16 à 1/4 | 250]
# /move(120,20,90,26) [1/4 à 3/16 | 250] << début fondu fin
# /move(90,26,60,43) [3/16 à 1/8 | 250]
# /move(60,43,0,100) [1/8 à 0 | 500]

# Jusqu'à activation kara >> 3000
# Fondu entrant pendant >> 1000
# Fondu sortant pendant >> 1000
# Total d'une période >> 4000
# Total de la phase >> 8000

def sinusoidale
  # Get all selected lines (line = head + sentence)
  @lines = Java::smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLines()
  
  count = 1
  resx = 1280
  ymax = 20
  ymin = 100
  
  # Prepare an array of movements
  # Videowidth sample = width/32
  # T Period = width/2
  # example : (baseline = 100 (ymin); amplitude = 80 (ymin-ymax))
  # 0 = pi        :       0       >> 100
  # 1/12 (pi/6)   :       0.5     >> 100 + 0.5 x 80 = 100 + 40 = 140 ou 100 - 40 = 60
  # 1/8 (pi/4)    :       0.707   >> 100 + 0.707 x 80 = 100 + 56.56 = 156.56 = 157 ou 100 - 57 = 43
  # 3/16 (3pi/8)  :       0.924   >> 100 + 0.924 x 80 = 100 + 73.92 = 173.92 = 174 ou 100 - 74 = 26
  # 1/4 (pi/2)    :       1       >> 100 + 1 x 80 = 100 + 80 = 180 ou 100 - 80 = 20    

  @period = [0,0.5,0.707,0.924,1,0.924,0.707,0.5,0,-0.5,-0.707,-0.924,-1,-0.924,-0.707,-0.5,0,0.5,0.707,0.924,1,0.924,0.707,0.5,0,-0.5,-0.707,-0.924,-1,-0.924,-0.707,-0.5,0]

  x = resx
  @movements = []    
  for k in 0..32 do
    if k<32
      xmax = x
      xmin = x - (resx/32)

      # sample : pos = baseline + @period[k] * amplitude
      oldypos = (ymin+@period[k]*(ymin-ymax)).ceil
      newypos = (ymin+@period[k+1]*(ymin-ymax)).ceil

      @movements[k] = "\\move("+xmax.to_s()+","+oldypos.to_s()+","+xmin.to_s()+","+newypos.to_s()+")"
      x = xmin
    end
  end
  
  # For each 'line' in @lines
  @lines.each do |line|
    # Get the sentence for this line
    sentence = Java::smallboxforfansub.scripting.ScriptPlugin.getSentence(line)
    
    # Get the head for this line
    head = Java::smallboxforfansub.scripting.ScriptPlugin.getHead(line);
    
    # Replace the layer
    head = Java::smallboxforfansub.scripting.ScriptPlugin.phChangeLayer(head, count.to_s())

    # Get a table of syllable parameters for the basic karaoke
    # [i][0] : syllable                   string
    # [i][1] : hundredth                  integer
    # [i][2] : thousandth                 integer
    # [i][3] : thousandth (start)         integer
    # [i][4] : thousandth (end)           integer
    osyl = Java::smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence)

    # Create a new sentence
    newsentence = ""
    
    # Loop onto each syllable to create the sentence
    for i in 0..osyl.length-1 do
      
      # Shift by -3000ms
      newhead = Java::smallboxforfansub.scripting.ScriptPlugin.phShift(head, (-3000+osyl[i][3]).to_s())
      
      # Get the start time of the head and translate it in milliseconds
      millistart = Java::smallboxforfansub.scripting.ScriptPlugin.phGetMillisecondsStart(newhead).to_i()
            
      # Syllable counter
      countms = 0
      
      # Trainee counter
      trainee = 250
      
      syl = osyl[i][0]
      smiddle = (osyl[i][3]+osyl[i][2]/2).to_s()    # middle become a string
      sstart = osyl[i][3].to_s()                    # start become a string
      send = osyl[i][4].to_s()                      # end become a string
      
      # For each movement
      @movements.each do |movement|
        
        # Change the head by trainee.time
        newhead = Java::smallboxforfansub.scripting.ScriptPlugin.phChangeEnd(newhead, (trainee+millistart).to_s())
        
        if countms==0          
          newsentence = "{"+movement+"\\alpha&HFF\\3a&HFF\\t(\\3a&HE0)\\3c&hCCFFFF\\be1\\bord3}"+syl  # Start with fade in (1/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)
        elsif countms==250
          newsentence = "{"+movement+"\\alpha&HFF\\3a&HE0\\t(\\3a&HC0)\\3c&hCCFFFF\\be1\\bord3}"+syl  # Start with fade in (2/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==500
          newsentence = "{"+movement+"\\alpha&HFF\\3a&HC0\\t(\\3a&HA0)\\3c&hCCFFFF\\be1\\bord3}"+syl  # Start with fade in (3/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==750
          newsentence = "{"+movement+"\\alpha&HFF\\3a&HA0\\t(\\3a&H7F)\\3c&hCCFFFF\\be1\\bord3}"+syl  # Start with fade in (4/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==3000
          newsentence = "{"+movement+"}"+syl
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
          newsentence = "{"+movement+"\\t(\\fscx150\\alpha&h77\\frz360)}"+syl
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==7000
          newsentence = "{"+movement+"\\t(\\alpha&HA0)}"+syl                                          # End with fade out (4/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==7250
          newsentence = "{"+movement+"\\alpha&HA0\\t(\\alpha&HC0)}"+syl                               # End with fade out (3/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==7500
          newsentence = "{"+movement+"\\alpha&HC0\\t(\\alpha&HE0)}"+syl                               # End with fade out (2/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms==7750
          newsentence = "{"+movement+"\\alpha&HE0\\t(\\alpha&HFF)}"+syl                               # End with fade out (1/4)
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms<3000
          newsentence = "{"+movement+"\\alpha&HFF\\3a&H7F\\3c&hCCFFFF\\be1\\bord3}"+syl
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
        elsif countms>3000
          newsentence = "{"+movement+"}"+syl
          line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
          Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
          if countms==3250
            newsentence = "{"+movement+"\\fscx150\\alpha&h77\\t(\\fscx300\\alpha&hFF\\frz360)}"+syl
            line = Java::smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(newhead, newsentence)       # Reformat the line 
            Java::smallboxforfansub.scripting.ScriptPlugin.addResLine(line)                                # Add a new line to the table of the main program
          end
        end
        
        newhead = Java::smallboxforfansub.scripting.ScriptPlugin.phChangeStart(newhead, (trainee+millistart).to_s())
        
        countms = countms + 250
        trainee = trainee + 250
      end
    end
    
    # Add 1 to the count
    count = count + 1
  end
end