-- Nameless Nightmare v1.0.0
--
-- Granular Synthesizer
-- Inspired by Solar System
-- 
--            @xO-xN
--
--    ▼ instructions below ▼
-- 
-- K1 (hold): Switch Load/Record
-- K2: Load File / Record Input
-- K3: Toggle Engine On/Off
-- 
-- E1: Select Engine
-- E2: Select Energy Source
-- E3: Adjust Energy Level
--
-- Rec mode: Hold K2 to Record

engine.name = 'Nameless_Nightmare'
fileselect = require 'fileselect'
util = require 'util'
NamelessNightmare = include("nameless-nightmare/lib/NamelessNightmare_engine")
drawPlanets = include("nameless-nightmare/lib/drawPlanets")

local the_engine = 2
local engines = {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "PlanetX"}
local engines_low = {"sun", "mercury", "venus", "earth", "mars", "jupiter", "saturn", "uranus", "neptune", "planetx"}
local launch_state = {["sun"] = false, ["mercury"] = false, ["venus"] = false, ["earth"] = false, ["mars"] = false, ["jupiter"] = false, ["saturn"] = false, ["uranus"] = false, ["neptune"] = false, ["planetx"] = false}
local num_engines = 10
local pow_select = 1
local pow_name = "Primary"
local pow_100 = 80.0

local engine_name = "Mercury"
local engine_low_name = "mercury"
local pow_list = {"Primary", "Vulcan"}
local num_pows = 2

local file_exists = 0
local selecting = false
local sampling = false
local length = 0.0
local midiFocus = true



-- file loading

function load_file(file)
    selecting = false
    if file ~= "cancel" then
        local ch, samples, samplerate = audio.file_info(file)
        length = samples/samplerate
        engine.read(file)
        file_exists = 1
        reset_all_engines()

        wait(1)
        print_info(file)
        redraw()
    end
end

function record_buffer()
    sampling = true
    engine.recStart(1)
    reset_all_engines()
    redraw()
end

function play_buffer()
    file_exists = 1
    sampling = false
    engine.recEnd(1)
    redraw()
end

function print_info(file)
    if util.file_exists(file) == true then
      local ch, samples, samplerate = audio.file_info(file)
      local duration = samples/samplerate
      print("loading file: "..file)
      print("  channels:\t"..ch)
      print("  samples:\t"..samples)
      print("  sample rate:\t"..samplerate.."hz")
      print("  duration:\t"..duration.." sec")
    else print "read_wav(): file not found" end
end

-- engine launch
function launch(the_engine)
    launch_state[engines_low[the_engine]] = not launch_state[engines_low[the_engine]]

    if the_engine == 1 then
        --sun
    elseif the_engine == 2 then
        engine.mercuryGate(params:get("mercury"), params:get("vulcan"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 3 then
        engine.venusGate(params:get("venus"), params:get("zoozve"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 4 then
        engine.earthGate(params:get("earth"), params:get("moon"), params:get("satellites"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 5 then
        engine.marsGate(params:get("mars"), params:get("phobos"), params:get("deimos"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 6 then
        engine.jupiterGate(params:get("jupiter"), params:get("moons"), params:get("jupiter_ring"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 7 then
        engine.saturnGate(params:get("saturn"), params:get("mimas"), params:get("rhea"), params:get("titan"), params:get("saturn_ring"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 8 then
        engine.uranusGate(params:get("uranus"), params:get("titania"), params:get("oberon"), params:get("uranus_ring"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 9 then
        engine.neptuneGate(params:get("neptune"), params:get("triton"), params:get("nereid"), params:get("pluto"), params:get("orcus"), params:get("neptune_ring"), params:get("planetx"), params:get("sun"))
    elseif the_engine == 10 then
        --planetx
    end
end

function reset_all_engines()
    for i = 1, 10 do
        if launch_state[engines_low[i]] then
            launch(i)
            launch_state[engines_low[i]] = false
        end
    end
end

-- planet drawing
function draw_planet(i)
    if i == 1 then
        drawPlanets.sun(params:get("sun"))
    elseif i == 2 then
        drawPlanets.mercury(params:get("mercury"), params:get("vulcan"))
    elseif i == 3 then
        drawPlanets.venus(params:get("venus"), params:get("zoozve"))
    elseif i == 4 then
        drawPlanets.earth(params:get("earth"), params:get("moon"), params:get("satellites"))
    elseif i == 5 then
        drawPlanets.mars(params:get("mars"), params:get("phobos"), params:get("deimos"))
    elseif i == 6 then
        drawPlanets.jupiter(params:get("jupiter"), params:get("moons"), params:get("jupiter_ring"))
    elseif i == 7 then
        drawPlanets.saturn(params:get("saturn"), params:get("mimas"), params:get("rhea"), params:get("titan"), params:get("saturn_ring"))
    elseif i == 8 then
        drawPlanets.uranus(params:get("uranus"), params:get("titania"), params:get("oberon"), params:get("uranus_ring"))
    elseif i == 9 then
        drawPlanets.neptune(params:get("neptune"), params:get("triton"), params:get("nereid"), params:get("pluto"), params:get("orcus"), params:get("neptune_ring"))
    elseif i == 10 then
        drawPlanets.planetx(params:get("planetx"))
    end
end

function pow_light(pow_select, num_pows)
    if pow_select == 1 then
        return num_pows <= 2 and num_pows or 3
    elseif pow_select == 2 then
        return num_pows == 2 and 1 or 2
    elseif pow_select == 3 then
        return 1
    elseif pow_select == 4 then
        return num_pows >= 4 and num_pows <= 5 and num_pows or 6
    elseif pow_select == 5 then
        return num_pows == 5 and 4 or 5
    elseif pow_select == 6 then
        return 4
    end
end

function numb_launching(launch_state)
    local count = 0
    for engine, value in pairs(launch_state) do
        if value == true then
            count = count + 1
        end
    end
    return count
end

function pow100_update()
    if pow_select == 1 then
        pow_100 = params:get(engine_low_name)
    elseif pow_name == "Ring" then
        pow_100 = params:get(engine_low_name.."_ring")
    else
        pow_100 = params:get(string.lower(pow_name))
    end
end

function focus(engine, pow)
    if midiFocus then
        the_engine = engine
        engine_low_name = engines_low[the_engine]
        engine_name = engines[the_engine]
        pow_select = pow
        pow_list = NamelessNightmare[engines_low[the_engine]]
        pow_name = pow_list[pow_select]
    end
end

function wait(seconds)
    local start = os.clock()
    repeat until os.clock() > start + seconds
end

-- controls

function key(n,z)
    if n == 1 and z == 1 then
        if params:get('sample_mode') == 1 then
            params:set('sample_mode', 2)
        else
            params:set('sample_mode', 1)
        end
    elseif n == 2 then
        -- load file OR recording sample
        file_exists = 0
        if params:get('sample_mode') == 1 and z == 1 then
            selecting = true
            fileselect.enter(_path.dust,load_file)
        elseif params:get('sample_mode') == 2 then
            if z == 1 then
                record_buffer()
            elseif z == 0 then
                play_buffer()
            end
        end
    elseif n == 3 and z == 1 then
        if file_exists == 0 then
            return
        else
            if the_engine > 1 and the_engine < 10 then
                launch(the_engine)
            end
        end

        redraw()
    end
end

function enc(n,d)
    if n == 1 then
        the_engine = util.clamp(the_engine + d, 1, num_engines)
        engine_name = engines[the_engine]
        engine_low_name = engines_low[the_engine]
        pow_list = NamelessNightmare[engine_low_name]
        pow_select = 1
        pow_name = pow_list[pow_select]
        pow_100 = params:get(engine_low_name)

        redraw()
    end

    if n == 2 then
        num_pows = NamelessNightmare.num_pow[the_engine]
        pow_select = util.clamp(pow_select + util.clamp(d, -1, 1), 1, num_pows)
        pow_name = pow_list[pow_select]
        pow100_update()

        redraw()
    end

    if n == 3 then
        if pow_select == 1 then
            params:delta(engine_low_name, d)
            pow_100 = params:get(engine_low_name)
        elseif pow_name == "Ring" then
            params:delta(engine_low_name.."_ring", d)
            pow_100 = params:get(engine_low_name.."_ring")
        else
            params:delta(string.lower(pow_name), d)
            pow_100 = params:get(string.lower(pow_name))
        end
    end
    
end

-- open screen

function intro()
    screen.clear()
    local t = 60
    for i = 1, t do
        screen.clear()
 
        screen.aa(1)
        screen.level(0+math.floor(i/(t/10)))
        screen.circle(64, 32, 2+math.floor(i/(t/10)))
        screen.fill()
        screen.aa(0)

        screen.aa(0)
        screen.font_face(1)
        screen.font_size(8)
        screen.level(0+math.floor(i/(t/5)))
        screen.move(22, 10)
        screen.text_center("Nameless")

        screen.level(0+math.floor(i/(t/5)))
        screen.move(22, 18)
        screen.text_center("Nightmare")

        screen.level(0+math.floor(i/(t/4)))
        screen.move(104, 10)
        screen.text_center("< Engine >")

        screen.move(8, 54)
        screen.level(0+math.floor(i/(t/3)))
        screen.text_center("File")

        screen.move(34, 62)
        screen.level(0+math.floor(i/(t/2)))
        screen.text_center("Sensor")

        screen.level(0+math.floor(i/(t/2)))
        screen.move(118, 24)
        screen.line_width(1)
        screen.line(118-(i/(t/10)), 24)
        screen.stroke()

        screen.level(0+math.floor(i/(t/10)))
        screen.move(118, 62)
        screen.text_center(i*math.floor(80/t))

        if i == t/3 or i == 2*t/3 then
            screen.level(15)
            screen.rect(12, 28, 8, 8)
            screen.fill()
            screen.level(2)
            screen.move(16, 34)
            screen.text_center("X")
        end

        screen.update()

        wait(1/60)
    end
end

-- initialization

function init()
    --norns reverb initialization
    --params:set("rev_monitor_input", -3)

    -- add params
    params:add_separator("settings")
    params:add_option('sample_mode', 'Sample Mode', {'Load', 'Record'}, 1)
    params:set_action('sample_mode', function(x) redraw() end)
    params:add_option('midi_focus', 'MIDI Focus', {'On', 'Off'}, 1)
    params:set_action('midi_focus', function(x) if x == 1 then midiFocus = true else midiFocus = false end end)
    NamelessNightmare.add_params()
    
    params:bang()

    the_engine = math.random(2, num_engines-1)
    engine_name = engines[the_engine]
    engine_low_name = engines_low[the_engine]
    pow_list = NamelessNightmare[engine_low_name]
    
    intro()
    redraw()
end

function redraw()
    screen.clear()

    --names and numbers
    screen.level(2)
    screen.aa(0)
    screen.font_face(1)
    screen.font_size(8)
    screen.move(22, 10)
    screen.text_center("Nameless")
    screen.move(22, 18)
    screen.text_center("Nightmare")

    screen.move(104, 10)
    screen.text_center("< Engine >")
    screen.level(12)
    screen.move(104, 18)
    screen.text_center(engine_name)

    -- line: engine selection
    screen.level(2)
    screen.move(108, 24)
    screen.line_width(1)
    screen.line_rel(11, 0)
    screen.stroke()

    screen.level(12)
    screen.move(108 + ((the_engine-1)), 24)
    screen.line_width(1)
    screen.line_rel(2, 0)
    screen.stroke()

    -- box: power selection
    num_pows = NamelessNightmare.num_pow[the_engine]
    for i = 1, num_pows do
        if i <= 3 then
            if i == pow_light(pow_select, num_pows) then
                screen.level(12)
            else
                screen.level(2)
            end
            screen.rect(121 - (i*4) ,51, 2, 2)
            screen.fill()
        else
            if i == pow_light(pow_select, num_pows) then
                screen.level(12)
            else
                screen.level(2)
            end
            screen.rect(121 - ((i-3)*4) ,47, 2, 2)
            screen.fill()
        end

    end

    -- box: numb of launching engine
    launching_engine = numb_launching(launch_state)

    for i = 1, 3 do
        if i > launching_engine then
            screen.level(2)
        else
            screen.level(12)
        end
        screen.rect(20 + (i*4), 51, 2, 2)
        screen.fill()
    end
    
    -- load/record mode
    screen.move(8, 54)
    if file_exists == 1 then
        screen.level(12)
    else
        screen.level(2)
    end
    screen.text_center("File")

    screen.move(8, 62)
    if params:get('sample_mode') == 1 then
        screen.level(12)
        screen.text_center("load")
    elseif params:get('sample_mode') == 2 then
        if sampling then
            screen.level(12)
        else
            screen.level(4)
        end
        screen.text_center("rec")
    end

    -- engine toggle on/off
    
    screen.move(34, 62)
    if the_engine > 1 and the_engine < 10 then
        if launch_state[engines_low[the_engine]] then
            screen.level(12)
        else
            screen.level(4)
        end
        screen.text_center("Sense")
    else
        --screen.level(15)
        --screen.text_center("Sense")
    end

    -- power name and number
    pow100_update()
    screen.level(12)
    screen.move(118, 62)
    screen.text_center(math.floor(pow_100))

    screen.move(109, 62)
    screen.text_right(pow_name.." :")

    draw_planet(the_engine)

    screen.update()
end
