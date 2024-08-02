-- Nameless Nightmare Engine lib
-- Engine params and functions

-- v1.0.0 @xO-xN

local ControlSpec = require "controlspec"

local NamelessNightmare = {}

local specs = {}

specs.amp = ControlSpec.new(0, 100, "lin", 0, 80)
specs.power = ControlSpec.new(0, 100, "lin", 0, 0)
specs.background = ControlSpec.new(-100, 100, "lin", 0, 0)

NamelessNightmare.specs = specs

function NamelessNightmare.add_params()
    params:add_separator("Sun")
    params:add{type = "control", id = "sun", name = "TimeFlow", controlspec = specs.background}
    params:set_action("sun", function(value) engine.sun(value) focus(1,1) redraw() end)

    params:add_separator("Mercury")
    params:add{type = "control", id = "mercury", name = "Mercury", controlspec = specs.amp}
    params:set_action("mercury", function(value) engine.mercuryPrimary(value) focus(2,1) redraw() end)
    params:add{type = "control", id = "vulcan", name = "Vulcan", controlspec = specs.power}
    params:set_action("vulcan", function(value) engine.mercuryVulcan(value) focus(2,2) redraw() end)

    params:add_separator("Venus")
    params:add{type = "control", id = "venus", name = "Venus", controlspec = specs.amp}
    params:set_action("venus", function(value) engine.venusPrimary(value) focus(3,1) redraw() end)
    params:add{type = "control", id = "zoozve", name = "Zoozve", controlspec = specs.power}
    params:set_action("zoozve", function(value) engine.venusZoozve(value) focus(3,2) redraw() end)
    
    params:add_separator("Earth")
    params:add{type = "control", id = "earth", name = "Earth", controlspec = specs.amp}
    params:set_action("earth", function(value) engine.earthPrimary(value) focus(4,1) redraw() end)
    params:add{type = "control", id = "moon", name = "Moon", controlspec = specs.power}
    params:set_action("moon", function(value) engine.earthMoon(value) focus(4,2) redraw() end)
    params:add{type = "control", id = "satellites", name = "Satellites", controlspec = specs.power}
    params:set_action("satellites", function(value) engine.earthSatellites(value) focus(4,3) redraw() end)

    params:add_separator("Mars")
    params:add{type = "control", id = "mars", name = "Mars", controlspec = specs.amp}
    params:set_action("mars", function(value) engine.marsPrimary(value) focus(5,1) redraw() end)
    params:add{type = "control", id = "phobos", name = "Phobos", controlspec = specs.power}
    params:set_action("phobos", function(value) engine.marsPhobos(value) focus(5,2) redraw() end)
    params:add{type = "control", id = "deimos", name = "Deimos", controlspec = specs.power}
    params:set_action("deimos", function(value) engine.marsDeimos(value) focus(5,3) redraw() end)
    
    params:add_separator("Jupiter")
    params:add{type = "control", id = "jupiter", name = "Jupiter", controlspec = specs.amp}
    params:set_action("jupiter", function(value) engine.jupiterPrimary(value) focus(6,1) redraw() end)
    params:add{type = "control", id = "moons", name = "Moons", controlspec = specs.power}
    params:set_action("moons", function(value) engine.jupiterMoons(value) focus(6,2) redraw() end)
    params:add{type = "control", id = "jupiter_ring", name = "Ring", controlspec = specs.power}
    params:set_action("jupiter_ring", function(value) engine.jupiterRing(value) focus(6,3) redraw() end)

    params:add_separator("Saturn")
    params:add{type = "control", id = "saturn", name = "Saturn", controlspec = specs.amp}
    params:set_action("saturn", function(value) engine.saturnPrimary(value) focus(7,1) redraw() end)
    params:add{type = "control", id = "mimas", name = "Mimas", controlspec = specs.power}
    params:set_action("mimas", function(value) engine.saturnMimas(value) focus(7,2) redraw() end)
    params:add{type = "control", id = "rhea", name = "Rhea", controlspec = specs.power}
    params:set_action("rhea", function(value) engine.saturnRhea(value) focus(7,3) redraw() end)
    params:add{type = "control", id = "titan", name = "Titan", controlspec = specs.power}
    params:set_action("titan", function(value) engine.saturnTitan(value) focus(7,4) redraw() end)
    params:add{type = "control", id = "saturn_ring", name = "Ring", controlspec = specs.power}
    params:set_action("saturn_ring", function(value) engine.saturnRing(value) focus(7,5) redraw() end)

    params:add_separator("Uranus")
    params:add{type = "control", id = "uranus", name = "Uranus", controlspec = specs.amp}
    params:set_action("uranus", function(value) engine.uranusPrimary(value) focus(8,1) redraw() end)
    params:add{type = "control", id = "titania", name = "Titania", controlspec = specs.power}
    params:set_action("titania", function(value) engine.uranusTitania(value) focus(8,2) redraw() end)
    params:add{type = "control", id = "oberon", name = "Oberon", controlspec = specs.power}
    params:set_action("oberon", function(value) engine.uranusOberon(value) focus(8,3) redraw() end)
    params:add{type = "control", id = "uranus_ring", name = "Ring", controlspec = specs.power}
    params:set_action("uranus_ring", function(value) engine.uranusRing(value) focus(8,4) redraw() end)

    params:add_separator("Neptune")
    params:add{type = "control", id = "neptune", name = "Neptune", controlspec = specs.amp}
    params:set_action("neptune", function(value) engine.neptunePrimary(value) focus(9,1) redraw() end)
    params:add{type = "control", id = "triton", name = "Triton", controlspec = specs.power}
    params:set_action("triton", function(value) engine.neptuneTriton(value) focus(9,2) redraw() end)
    params:add{type = "control", id = "nereid", name = "Nereid", controlspec = specs.power}
    params:set_action("nereid", function(value) engine.neptuneNereid(value) focus(9,3) redraw() end)
    params:add{type = "control", id = "pluto", name = "Pluto", controlspec = specs.power}
    params:set_action("pluto", function(value) engine.neptunePluto(value) focus(9,4) redraw() end)
    params:add{type = "control", id = "orcus", name = "Orcus", controlspec = specs.power}
    params:set_action("orcus", function(value) engine.neptuneOrcus(value) focus(9,5) redraw() end)
    params:add{type = "control", id = "neptune_ring", name = "Ring", controlspec = specs.power}
    params:set_action("neptune_ring", function(value) engine.neptuneRing(value) focus(9,6) redraw() end)

    params:add_separator("PlanetX")
    params:add{type = "control", id = "planetx", name = "X", controlspec = specs.background}
    params:set_action("planetx", function(value) engine.planetX(value) focus(10,1) redraw() end)
end

NamelessNightmare.sun = {"TimeFlow"}
NamelessNightmare.mercury = {"Primary", "Vulcan"}
NamelessNightmare.venus = {"Primary", "Zoozve"}
NamelessNightmare.earth = {"Primary", "Moon", "Satellites"}
NamelessNightmare.mars = {"Primary", "Phobos", "Deimos"} 
NamelessNightmare.jupiter = {"Primary", "Moons", "Ring"}
NamelessNightmare.saturn = {"Primary", "Mimas", "Rhea", "Titan", "Ring"}
NamelessNightmare.uranus = {"Primary", "Titania", "Oberon", "Ring"}
NamelessNightmare.neptune = {"Primary", "Triton", "Nereid", "Pluto", "Orcus", "Ring"}
NamelessNightmare.planetx = {"X"}

NamelessNightmare.num_pow = {1, 2, 2, 3, 3, 3, 5, 4, 6, 1}

return NamelessNightmare