script_name = "Miniature Feuille Launcher"
script_description = "A script connection onto Feuille UI to make karaoke."
script_author = "TW2"
script_version = "1"

include("karaskel-auto4.lua")

function feuille_in_aegi(subtitles, selected_lines, active_line)

	-- Path for processing files
	local path = "<please type an absolute file folder>"
	
	-- Path of Feuille
	local feuille = "<please type your Feuille.jar absolute path>"
	
	-- Register line
	local lastline = ""

	-- Register all lines in a file that we can open with ease in Feuille
	local memo = io.open(path .. "\\in.txt", "w+")
	-- Register also keywords
	local keyz = io.open(path .. "\\line_keywords.txt", "w+")
	local sylkeyz = io.open(path .. "\\syllable_keywords.txt", "w+")
	local charkeyz = io.open(path .. "\\char_keywords.txt", "w+")
	for z, i in ipairs(selected_lines) do
		local line = subtitles[i]
		
		meta, styles = karaskel.collect_head(subtitles, generate_furigana)
		karaskel.preproc_line(subtitles, meta, styles, line)
		keyz:write(line.text_stripped)
		keyz:write("|" .. line.duration)
		--keyz:write("|" .. line.kara)
		--keyz:write("|" .. line.furi)
		--keyz:write("|" .. line.styleref)
		--keyz:write("|" .. line.furistyle)
		keyz:write("|" .. line.width)
		keyz:write("|" .. line.height)
		keyz:write("|" .. line.descent)
		keyz:write("|" .. line.extlead)
		keyz:write("|" .. line.margin_v)
		keyz:write("|" .. line.eff_margin_l)
		keyz:write("|" .. line.eff_margin_r)
		keyz:write("|" .. line.eff_margin_t)
		keyz:write("|" .. line.eff_margin_b)
		keyz:write("|" .. line.eff_margin_v)
		keyz:write("|" .. line.halign)
		keyz:write("|" .. line.valign)
		keyz:write("|" .. line.left)
		keyz:write("|" .. line.center)
		keyz:write("|" .. line.right)
		keyz:write("|" .. line.top)
		keyz:write("|" .. line.middle)
		keyz:write("|" .. line.vcenter)
		keyz:write("|" .. line.bottom)
		keyz:write("|" .. line.x)
		keyz:write("|" .. line.y)
		keyz:write("|" .. line.class)
		keyz:write("|" .. line.raw)
		keyz:write("|" .. line.section)
		--keyz:write("|" .. line.comment)
		keyz:write("|" .. line.layer)
		keyz:write("|" .. line.start_time)
		keyz:write("|" .. line.end_time)
		keyz:write("|" .. line.style)
		keyz:write("|" .. line.actor)
		keyz:write("|" .. line.margin_l)
		keyz:write("|" .. line.margin_r)
		keyz:write("|" .. line.margin_t)
		keyz:write("|" .. line.margin_b)
		keyz:write("|" .. line.effect)
		--keyz:write("|" .. line.userdata)
		keyz:write("|" .. line.text)
		keyz:write("\n")
		
		for i = 1, line.kara.n do
			local syl = line.kara[i]
			sylkeyz:write(syl.duration)
			sylkeyz:write("|" .. syl.start_time)
			sylkeyz:write("|" .. syl.end_time)
			sylkeyz:write("|" .. syl.tag)
			sylkeyz:write("|" .. syl.text)
			sylkeyz:write("|" .. syl.text_stripped)
			sylkeyz:write("|" .. syl.kdur)
			--sylkeyz:write("|" .. syl.line)
			sylkeyz:write("|" .. syl.inline_fx)
			sylkeyz:write("|" .. syl.i)
			sylkeyz:write("|" .. syl.prespace)
			sylkeyz:write("|" .. syl.postspace)
			sylkeyz:write("|" .. syl.text_spacestripped)
			--sylkeyz:write("|" .. syl.style)
			sylkeyz:write("|" .. syl.width)
			sylkeyz:write("|" .. syl.height)
			sylkeyz:write("|" .. syl.prespacewidth)
			sylkeyz:write("|" .. syl.postspacewidth)
			sylkeyz:write("|" .. syl.left)
			sylkeyz:write("|" .. syl.center)
			sylkeyz:write("|" .. syl.right)
			sylkeyz:write("|" .. syl.style.align)
			if i ~= line.kara.n then
				sylkeyz:write("_")
			end
			
			local countchar = 0
			local left, cn = line.left+syl.left, unicode.len(syl.text_stripped)
			for c,ci in unicode.chars(syl.text_stripped) do
				local widthc = aegisub.text_extents(line.styleref, c)
				local center = left + widthc/2
				local right = left + widthc
				if string.find(c, '%s') then
					charkeyz:write("SPACE_CHAR")
				else
					charkeyz:write(left)
					charkeyz:write("|" .. center)
					charkeyz:write("|" .. right)					
				end
				left = left + widthc
				countchar = countchar + 1
				if countchar ~= cn then
					charkeyz:write("¤")
				end	
			end
			if i ~= line.kara.n then
				charkeyz:write("¤")
			end
		end
		charkeyz:write("\n")
		sylkeyz:write("\n")
		
		memo:write(line.raw .. "\n")
		lastline = line
	end	
	charkeyz:close()
	sylkeyz:close()
	keyz:close()
	memo:close()
	
	-- Execute the program
	os.execute(feuille .. " karaoke " .. path)
	
	-- Read Feuille generated lines
	for generatedline in io.lines(path .. "\\out.txt") do
	
		local stable = split(generatedline, ",")
			
		lastline.comment = false
		lastline.layer = getlayer(stable[1])
		lastline.start_time = getmillliseconds(stable[2])
		lastline.end_time = getmillliseconds(stable[3])
		lastline.style = stable[4]
		lastline.actor = stable[5]
		lastline.margin_l = stable[6]
		lastline.margin_r = stable[7]
		lastline.margin_t = stable[8]
		lastline.margin_b = stable[8]
		lastline.effect = stable[9]
		
		local i = 10
		local sentence = ""
		while i<=table.maxn(stable) do
			if i == 10 then
				sentence = stable[i]
			else
				sentence = sentence .. "," .. stable[i]
			end			
			i = i + 1
		end
		
		lastline.text = sentence
		
		subtitles.append(lastline)
		
	end
	
	aegisub.set_undo_point("Make karaoke with Feuille UI through Aegisub")
end

aegisub.register_macro("Make karaoke with Feuille UI through Aegisub", "Feuille UI", feuille_in_aegi)

function split(s, delimiter)
    result = {};
    for match in (s..delimiter):gmatch("(.-)"..delimiter) do
		table.insert(result, match);
    end
    return result;
end

function getmillliseconds(s)
	s = s:gsub("[^%d]+", "-")
	number = split(s, "-")
	h = tonumber(number[1]) * 3600000
	m = tonumber(number[2]) * 60000
	sc = tonumber(number[3]) * 1000
	ml = tonumber(number[4]) * 10
	result = h + m + sc + ml
	return result
end

function getlayer(s)
	return string.sub(s, 10)
end