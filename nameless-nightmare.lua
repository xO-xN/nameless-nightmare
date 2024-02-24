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
UI = require "ui"
fileselect = require 'fileselect'
NamelessNightmare = include("nameless-nightmare/lib/NamelessNightmare_engine")

local pages
local tabs
local tab_titles = {"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "PlanetX"}

local file_exists = 0
local selecting = false
local length = 0.0

local gate_on = false



-- file loading

function load_file(file)
    selecting = false
    if file ~= "cancel" then
        local ch, samples, samplerate = audio.file_info(file)
        length = samples/samplerate
        engine.read(file)
        file_exists = 1
        print_info(file)
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


-- controls

function key(n,z)
    if n == 1 and z == 1 then
        selecting = true
        fileselect.enter(_path.dust,load_file)
    elseif n == 2 and z == 1 then
    elseif n == 3 and z == 1 then
        if gate_on then
            engine.mercuryGate(0)
            gate_on = false
        else
            engine.mercuryGate(1)
            gate_on = true
        end
    end
end

function enc(n,d)
    if n == 2 then

    end

    if n == 3 then

    end
end    


-- initialization

function init()
    -- add params

    NamelessNightmare.add_params()
    
    --norns initialization
    audio.rev_on()


    -- UI initialization
    --pages = UI.Pages.new(1, 9)
    --tabs = UI.Tabs.new(1, tab_titles[pages.index])

    params:bang()
    redraw()
end

function redraw()
    --screen size 128 * 64
    if file_exists == 0 then
        screen.clear()
        screen.level(15)
        screen.move(64, 30)
        screen.text_center("HOLD K1 TO LOAD SOUND")
        screen.update()

    else
    screen.clear()

    --pages:redraw()
    --tabs:redraw()

    screen.level(15)
    screen.move(64, 30)
    screen.text_center("loaded")
    screen.update()
    end
end


function pageprint()
    if pages.index == 1 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Mercury")
    elseif pages.index == 2 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Venus")
    elseif pages.index == 3 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Earth")
    elseif pages.index == 4 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Mars")
    elseif pages.index == 5 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Jupiter")
    elseif pages.index == 6 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Saturn")
    elseif pages.index == 7 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Uranus")
    elseif pages.index == 8 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("Neptune")
    elseif pages.index == 9 then
        screen.level(15)
        screen.move(64, 40)
        screen.text_center("PlanetX")
    end
end
