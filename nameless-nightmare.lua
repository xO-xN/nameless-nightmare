-- Nameless Nightmare v0.5.0
-- Granular-based ambient sound generator
--
-- llllllll.co/t/nameless-nightmare
--
--
--
--    ▼ instructions below ▼
-- watch out your back
-- (and watch out cpu actually)

engine.name = 'Nameless_Nightmare'
fileselect = require 'fileselect'
util = require 'util'
NamelessNightmare = include("nameless-nightmare/lib/NamelessNightmare_engine")
drawPlanets = include("nameless-nightmare/lib/drawPlanets")

local the_engine = 1
local engines = {"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "PlanetX"}
local engines_low = {"mercury", "venus", "earth", "mars", "jupiter", "saturn", "uranus", "neptune", "planetx"}
local launch_state = {["mercury"] = false, ["venus"] = false, ["earth"] = false, ["mars"] = false, ["jupiter"] = false, ["saturn"] = false, ["uranus"] = false, ["neptune"] = false, ["planetx"] = false}
local num_engines = 9
local pow_select = 1
local pow_name = "self"
local pow_100 = 80.0

local engine_name = "Mercury"
local engine_low_name = "mercury"
local pow_list = {"self", "Vulcan"}
local num_pows = 2

local file_exists = 0
local selecting = false
local length = 0.0



-- file loading

function load_file(file)
    selecting = false
    if file ~= "cancel" then
        local ch, samples, samplerate = audio.file_info(file)
        length = samples/samplerate
        engine.read(file)
        file_exists = 1
        launch_state = {["mercury"] = false, ["venus"] = false, ["earth"] = false, ["mars"] = false, ["jupiter"] = false, ["saturn"] = false, ["uranus"] = false, ["neptune"] = false, ["planetx"] = false}
        print_info(file)
        redraw()
    end
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
function launch(i)
    launch_state[engines_low[i]] = not launch_state[engines_low[i]]

    if i == 1 then
        engine.mercuryGate(params:get("mercury_self"), params:get("vulcan_pow"), params:get("planetx_self"))
    elseif i == 2 then
        engine.venusGate(params:get("venus_self"), params:get("zoozve_pow"), params:get("planetx_self"))
    elseif i == 3 then
        engine.earthGate(params:get("earth_self"), params:get("moon_pow"), params:get("satellites_pow"), params:get("planetx_self"))
    elseif i == 4 then
        engine.marsGate(params:get("mars_self"), params:get("phobos_pow"), params:get("deimos_pow"), params:get("planetx_self"))
    elseif i == 5 then
        engine.jupiterGate(params:get("jupiter_self"), params:get("moons_pow"), params:get("jupiter_ring_pow"), params:get("planetx_self"))
    elseif i == 6 then
        engine.saturnGate(params:get("saturn_self"), params:get("mimas_pow"), params:get("rhea_pow"), params:get("titan_pow"), params:get("saturn_ring_pow"), params:get("planetx_self"))
    elseif i == 7 then
        engine.uranusGate(params:get("uranus_self"), params:get("titania_pow"), params:get("oberon_pow"), params:get("uranus_ring_pow"), params:get("planetx_self"))
    elseif i == 8 then
        engine.neptuneGate(params:get("neptune_self"), params:get("triton_pow"), params:get("nereid_pow"), params:get("pluto_pow"), params:get("orcus_pow"), params:get("neptune_ring_pow"), params:get("planetx_self"))
    elseif i == 9 then
        engine.planetxGate(1)
    end
end

-- planet drawing
function draw_planet(i)
    if i == 1 then
        drawPlanets.mercury(params:get("mercury_self"), params:get("vulcan_pow"))
    elseif i == 2 then
        drawPlanets.venus(params:get("venus_self"), params:get("zoozve_pow"))
    elseif i == 3 then
        drawPlanets.earth(params:get("earth_self"), params:get("moon_pow"), params:get("satellites_pow"))
    elseif i == 4 then
        drawPlanets.mars(params:get("mars_self"), params:get("phobos_pow"), params:get("deimos_pow"))
    elseif i == 5 then
        drawPlanets.jupiter(params:get("jupiter_self"), params:get("moons_pow"), params:get("jupiter_ring_pow"))
    elseif i == 6 then
        drawPlanets.saturn(params:get("saturn_self"), params:get("mimas_pow"), params:get("rhea_pow"), params:get("titan_pow"), params:get("saturn_ring_pow"))
    elseif i == 7 then
        drawPlanets.uranus(params:get("uranus_self"), params:get("titania_pow"), params:get("oberon_pow"), params:get("uranus_ring_pow"))
    elseif i == 8 then
        drawPlanets.neptune(params:get("neptune_self"), params:get("triton_pow"), params:get("nereid_pow"), params:get("pluto_pow"), params:get("orcus_pow"), params:get("neptune_ring_pow"))
    elseif i == 9 then
        drawPlanets.planetx(params:get("planetx_self"))
    end
end

-- controls

function key(n,z)
    if n == 1 and z == 1 then
        selecting = true
        fileselect.enter(_path.dust,load_file)
    elseif n == 2 and z == 1 then
    elseif n == 3 and z == 1 then
        launch(the_engine)

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
        pow_100 = params:get(engine_low_name.."_self")

        redraw()
    end

    if n == 2 and file_exists == 1 then
        num_pows = NamelessNightmare.num_pow[the_engine]
        pow_select = util.clamp(pow_select + util.clamp(d, -1, 1), 1, num_pows)
        pow_name = pow_list[pow_select]
        if pow_select == 1 then
            pow_100 = params:get(engine_low_name.."_self")
        elseif pow_name == "Ring" then
            pow_100 = params:get(engine_low_name.."_ring_pow")
        else
            pow_100 = params:get(string.lower(pow_name).."_pow")
        end

        redraw()
    end

    if n == 3 and file_exists == 1 then
        if pow_select == 1 then
            params:delta(engine_low_name.."_self", d)
            pow_100 = params:get(engine_low_name.."_self")
        elseif pow_name == "Ring" then
            params:delta(engine_low_name.."_ring_pow", d)
            pow_100 = params:get(engine_low_name.."_ring_pow")
        else
            params:delta(string.lower(pow_name).."_pow", d)
            pow_100 = params:get(string.lower(pow_name).."_pow")
        end
        
        redraw()
    end
    
end


-- initialization

function init()
    -- add params
    NamelessNightmare.add_params()
    the_engine = math.random(1, num_engines-1)
    engine_name = engines[the_engine]
    engine_low_name = engines_low[the_engine]
    pow_list = NamelessNightmare[engine_low_name]
    
    --norns initialization
    audio.rev_on()

    params:bang()
    redraw()
end

function redraw()
    --screen size 128 * 64
    if file_exists == 0 then
        screen.clear()
        screen.level(15)
        screen.move(64, 30)
        screen.text_center("HOLD K1 TO LOAD SAMPLE")
        screen.update()

    else

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

    screen.move(110, 10)
    screen.text_center("Engine")
    screen.level(12)
    screen.move(110, 18)
    screen.text_center(engine_name)

    if launch_state[engines_low[the_engine]] then
        screen.level(12)
    else
        screen.level(4)
    end
    screen.move(33, 62)
    screen.text_center("launch")

    screen.level(12)
    screen.move(116, 56)
    screen.text_center(pow_100)
    screen.move(116, 62)
    screen.text_center("Power")

    screen.move(99, 62)
    screen.text_right(pow_name.." :")

    draw_planet(the_engine)

    screen.update()
    end
end
