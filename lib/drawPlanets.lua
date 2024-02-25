-- Nameless Nightmare draw planets lib
-- draw planets on norns' screen

-- v0.5.0 @xxiangcoding


local drawPlanets = {}

drawPlanets.mercury = function(self, vulcan)
    screen.aa(1)
    screen.level(math.floor((self/100)*vulcan/12.5))
    screen.circle(64-math.floor(vulcan/10), 32, 11+math.floor(vulcan/16.6))
    screen.fill()

    screen.level(math.floor(self/6.8))
    screen.circle(64+math.floor(vulcan/10), 32, 14+math.floor(self/33))
    screen.fill()
    screen.aa(0)
end

drawPlanets.venus = function(self, zoozve)
    screen.aa(1)
    screen.level(math.floor((self/100)*zoozve/12.5))
    screen.circle(64, 22-math.floor(zoozve/25), 8+math.floor(zoozve/33))
    screen.fill()

    screen.level(math.floor(self/6.8))
    screen.circle(64, 35-math.floor(self/66), 14+math.floor(self/25))
    screen.fill()
    screen.aa(0)
end

drawPlanets.earth = function(self, moon, satellites)
    screen.aa(1)
    screen.level(math.floor(self/6.8))
    screen.circle(64+((util.clamp(moon-50, 0, 50)/10)), 32, 15-(math.floor(moon/50.1)*5))
    screen.fill()

    screen.level(math.floor((self/100)*self/12.5))
    screen.circle(104-(math.floor(moon/1.25)), 32, 4+(math.floor(moon/50.1)*4))
    screen.fill()

    screen.level(math.floor(satellites/6.8))
    screen.move(67, 42)
    screen.text_center(". .   .")
    screen.move(62, 22)
    screen.text_center(" .   .")
    screen.move(60, 30)
    screen.text_center(".. .     .  ")
    screen.move(56, 28)
    screen.text_center(" .  .")
    screen.move(66, 31)
    screen.text_center("  .  .  ")
    screen.move(58, 34)
    screen.text_center("  . . .  ")

    screen.aa(0)
end

drawPlanets.mars = function(self, phobos, deimos)
    screen.aa(1)

    screen.level(12*(math.floor(phobos/52)))
    screen.move(68+(math.floor(phobos/44)*math.floor((phobos-58)/5))+(5*(1-math.floor(phobos/52))), 24)
    screen.text_center(". ..")
    screen.move(69+(math.floor(phobos/46)*math.floor((phobos-56)/5))+(5*(1-math.floor(phobos/52))), 27)
    screen.text_center(". .")
    screen.move(74+(math.floor(phobos/48)*math.floor((phobos-54)/5))+(5*(1-math.floor(phobos/52))), 28)
    screen.text_center("..  .")
    screen.move(77+(math.floor(phobos/50)*math.floor((phobos-52)/5))+(5*(1-math.floor(phobos/52))), 34)
    screen.text_center(". .")
    screen.move(70+(math.floor(phobos/52)*math.floor((phobos-50)/5))+(5*(1-math.floor(phobos/52))), 39)
    screen.text_center(". ..")

    screen.level(math.floor((phobos/10)*(1-math.floor(phobos/52))))
    screen.circle(85-math.floor(phobos/5), 32, 5)
    screen.fill()

    screen.level(math.floor(self/6.8))
    screen.circle(64, 32, 13)
    screen.fill()

    screen.level(math.floor(self/6.8)-math.floor(deimos/6.8))
    screen.circle(58-((math.floor(deimos/50.1)*math.floor((deimos-50)/5))), 36, 3)
    screen.fill()


    screen.aa(0)
end

drawPlanets.jupiter = function(self, moons, ring)
    screen.level(8)
    screen.circle(64, 12, 4)

    screen.level(15)
    screen.circle(64, 32, 10)
end

drawPlanets.saturn = function(self, mimas, rhea, titan, ring)
    screen.level(8)
    screen.circle(64, 12, 4)

    screen.level(15)
    screen.circle(64, 32, 10)
end

drawPlanets.uranus = function(self, titania, oberon, ring)
    screen.level(8)
    screen.circle(64, 12, 4)

    screen.level(15)
    screen.circle(64, 32, 10)
end

drawPlanets.neptune = function(self, triton, nereid, pluto, orcus, ring)
    screen.level(8)
    screen.circle(64, 12, 4)

    screen.level(15)
    screen.circle(64, 32, 10)
end

drawPlanets.planetx = function(self)
    screen.level(8)
    screen.circle(64, 12, 4)

    screen.level(15)
    screen.circle(64, 32, 10)
end


return drawPlanets