-- Nameless Nightmare Engine lib
-- Engine params and functions

-- v0.5.0 @xxiangcoding

local ControlSpec = require "controlspec"

local NamelessNightmare = {}

local specs = {}

specs.amp = ControlSpec.new(0, 100, "lin", 0, 80)
specs.power = ControlSpec.new(0, 100, "lin", 0, 0)

NamelessNightmare.specs = specs

function NamelessNightmare.add_params()

    params:add_separator("Mercury")
    params:add{type = "control", id = "mercury_self", name = "self", controlspec = specs.amp, action = engine.mercurySelfAmp}
    params:add{type = "control", id = "vulcan_pow", name = "Vulcan", controlspec = specs.power, action = engine.mercuryVulcanPow}

    params:add_separator("Venus")
    params:add{type = "control", id = "venus_self", name = "self", controlspec = specs.amp, action = engine.venusSelfAmp}
    params:add{type = "control", id = "zoozve_pow", name = "Zoozve", controlspec = specs.power, action = engine.venusZoozvePow}

    params:add_separator("Earth")
    params:add{type = "control", id = "earth_self", name = "self", controlspec = specs.amp, action = engine.earthSelfAmp}
    params:add{type = "control", id = "earth_moon_pow", name = "Moon", controlspec = specs.power, action = engine.earthMoonPow}
    params:add{type = "control", id = "satellites_pow", name = "Satellites", controlspec = specs.power, action = engine.earthSatellitesPow}

    params:add_separator("Mars")
    params:add{type = "control", id = "mars_self", name = "self", controlspec = specs.amp, action = engine.marsSelfAmp}
    params:add{type = "control", id = "phobos_pow", name = "Phobos", controlspec = specs.power, action = engine.marsPhobosPow}
    params:add{type = "control", id = "deimos_pow", name = "Deimos", controlspec = specs.power, action = engine.marsDeimosPow}

    params:add_separator("Jupiter")
    params:add{type = "control", id = "jupiter_self", name = "self", controlspec = specs.amp, action = engine.jupiterSelfAmp}
    params:add{type = "control", id = "jupiter_moons_pow", name = "Moons", controlspec = specs.power, action = engine.jupiterMoonsPow}
    params:add{type = "control", id = "jupiter_ring_pow", name = "Ring", controlspec = specs.power, action = engine.jupiterRingPow}
    
    params:add_separator("Saturn")
    params:add{type = "control", id = "saturn_self", name = "self", controlspec = specs.amp, action = engine.saturnSelfAmp}
    params:add{type = "control", id = "mimas_pow", name = "Mimas", controlspec = specs.power, action = engine.saturnMimasPow}
    params:add{type = "control", id = "rhea_pow", name = "Rhea", controlspec = specs.power, action = engine.saturnRheaPow}
    params:add{type = "control", id = "titan_pow", name = "Titan", controlspec = specs.power, action = engine.saturnTitanPow}
    params:add{type = "control", id = "saturn_ring_pow", name = "Ring", controlspec = specs.power, action = engine.saturnRingPow}

    params:add_separator("Uranus")
    params:add{type = "control", id = "uranus_self", name = "self", controlspec = specs.amp, action = engine.uranusSelfAmp}
    params:add{type = "control", id = "titania_pow", name = "Titania", controlspec = specs.power, action = engine.uranusTitaniaPow}
    params:add{type = "control", id = "oberon_pow", name = "Oberon", controlspec = specs.power, action = engine.uranusOberonPow}
    params:add{type = "control", id = "uranus_ring_pow", name = "Ring", controlspec = specs.power, action = engine.uranusRingPow}

    params:add_separator("Neptune")
    params:add{type = "control", id = "neptune_self", name = "self", controlspec = specs.amp, action = engine.neptuneSelfAmp}
    params:add{type = "control", id = "triton_pow", name = "Triton", controlspec = specs.power, action = engine.neptuneTritonPow}
    params:add{type = "control", id = "nereid_pow", name = "Nereid", controlspec = specs.power, action = engine.neptuneNereidPow}
    params:add{type = "control", id = "pluto_pow", name = "Pluto", controlspec = specs.power, action = engine.neptunePlutoPow}
    params:add{type = "control", id = "orcus_pow", name = "Orcus", controlspec = specs.power, action = engine.neptuneOrcusPow}
    params:add{type = "control", id = "neptune_ring_pow", name = "Ring", controlspec = specs.power, action = engine.neptuneRingPow}

    params:add_separator("PlanetX")
    params:add{type = "control", id = "x_pow", name = "X", controlspec = specs.amp, action = engine.xPow}
end

local mercury = {self, Vulcan}
local venus = {self, Zoozve}
local earth = {self, Moon, Satellites}
local mars = {self, Phobos, Deimos} 
local jupiter = {self, Moons, Ring}
local saturn = {self, Mimas, Rhea, Titan, Ring}
local uranus = {self, Titania, Oberon, Ring}
local neptune = {self, Triton, Nereid, Pluto, Orcus, Ring}
local planetx = {X}


return NamelessNightmare