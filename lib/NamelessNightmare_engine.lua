-- Nameless Nightmare Engine lib
-- Engine params and functions

-- v0.5.0 @xxiangcoding

local ControlSpec = require "controlspec"

local NamelessNightmare = {}

local specs = {}

specs.amp = ControlSpec.new(0, 1, "lin", 0, 0.8)
specs.power = ControlSpec.new(-50, 50, "lin", 0, 0)

NamelessNightmare.specs = specs

function NamelessNightmare.add_params()

    params:add_separator("Mercury")
    params:add{type = "control", id = "mercury_self", name = "self", controlspec = specs.amp, action = engine.mercurySelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.mercuryVulcanPow}

    params:add_separator("Venus")
    params:add{type = "control", id = "venus_self", name = "self", controlspec = specs.amp, action = engine.venusSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.venusZoozvePow}

    params:add_separator("Earth")
    params:add{type = "control", id = "earth_self", name = "self", controlspec = specs.amp, action = engine.earthSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.earthMoonPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.earthSatellitesPow}

    params:add_separator("Mars")
    params:add{type = "control", id = "mars_self", name = "self", controlspec = specs.amp, action = engine.marsSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.marsPhobosPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.marsDeimosPow}

    params:add_separator("Jupiter")
    params:add{type = "control", id = "jupiter_self", name = "self", controlspec = specs.amp, action = engine.jupiterSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.jupiterLoPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.jupiterEuropaPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.jupiterGanymedePow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.jupiterCallistoPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.jupiterRingPow}
    
    params:add_separator("Saturn")
    params:add{type = "control", id = "saturn_self", name = "self", controlspec = specs.amp, action = engine.saturnSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.saturnMimasPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.saturnRheaPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.saturnTitanPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.saturnRingPow}

    params:add_separator("Uranus")
    params:add{type = "control", id = "uranus_self", name = "self", controlspec = specs.amp, action = engine.uranusSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.uranusTitaniaPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.uranusOberonPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.uranusRingPow}

    params:add_separator("Neptune")
    params:add{type = "control", id = "neptune_self", name = "self", controlspec = specs.amp, action = engine.neptuneSelfAmp}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.neptuneTritonPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.neptuneNereidPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.neptuneRingPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.neptunePlutoPow}
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.neptuneOrcusPow}

    params:add_separator("PlanetX")
    params:add{type = "control", id = "self", name = "self", controlspec = specs.power, action = engine.xSelfPow}
end

local mercury = {self, vulcan}
local venus = {self, zoozve}
local earth = {self, moon, satellites}
local mars = {self, phobos, deimos} 
local jupiter = {self, lo, europa, ganymede, callisto, ring}
local saturn = {self, mimas, rhea, titan, ring}
local uranus = {self, titania, oberon, ring}
local neptune = {self, triton, nereid, ring, pluto, orcus}
local planetx = {self}


return NamelessNightmare