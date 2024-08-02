-- Nameless Nightmare draw planets lib
-- draw planets on norns' screen

-- v1.0.0 @xO-xN


local drawPlanets = {}

drawPlanets.sun = function(self)
    screen.aa(1)

    screen.level(14-math.floor(self/20))
    screen.circle(64, 32, 20+math.floor(self/5.5))
    screen.fill()

    screen.aa(0)
end

drawPlanets.mercury = function(self, vulcan)
    screen.aa(1)
    screen.level(math.floor((self/100)*vulcan/14))
    screen.circle(64-math.floor(vulcan/12.5), 32+math.floor(vulcan/33), 7+math.floor(vulcan/33))
    screen.fill()

    screen.level(math.floor(self/6.8))
    screen.circle(64+math.floor(vulcan/12.5), 32-math.floor(vulcan/33), 7+math.floor(self/33))
    screen.fill()
    screen.aa(0)
end

drawPlanets.venus = function(self, zoozve)
    screen.aa(1)
    screen.level(math.floor((self/100)*zoozve/12.5))
    screen.circle(64, 22-math.floor(zoozve/33), 6+math.floor(zoozve/33))
    screen.fill()

    screen.level(math.floor(self/6.8))
    screen.circle(64, 32+math.floor(zoozve/40), 12+math.floor(self/25))
    screen.fill()
    screen.aa(0)
end

drawPlanets.earth = function(self, moon, satellites)
    screen.aa(1)

    screen.level(math.floor(satellites/6.8))
    screen.move(72, 39)
    screen.text_center(". ..")
    screen.move(75, 26)
    screen.text_center(". .")
    screen.move(81, 30)
    screen.text_center(".. .")
    screen.move(42, 28)
    screen.text_center(" . .")
    screen.move(46, 31)
    screen.text_center(".  .")
    screen.move(39, 34)
    screen.text_center(". . .")

    screen.level(math.floor(self/6.8))
    screen.circle(64+((util.clamp(moon-50, 0, 50)/10)), 32, 15-(math.floor(moon/50.1)*5))
    screen.fill()

    screen.level(math.floor(moon/8))
    screen.circle(89-(math.floor(moon/2)), 32, 4+(math.floor(moon/50.1)*4))
    screen.fill()
    
    screen.aa(0)
end

drawPlanets.mars = function(self, phobos, deimos)
    screen.aa(1)

    screen.level(12*(math.floor(phobos/52)))
    screen.move(68+((phobos/44)*math.floor((phobos-58)/5))+(5*(1-(phobos/52))), 24)
    screen.text_center(". ..")
    screen.move(69+((phobos/46)*math.floor((phobos-56)/5))+(5*(1-(phobos/52))), 27)
    screen.text_center(". .")
    screen.move(74+((phobos/48)*math.floor((phobos-54)/5))+(5*(1-(phobos/52))), 28)
    screen.text_center("..  .")
    screen.move(77+((phobos/50)*math.floor((phobos-52)/5))+(5*(1-(phobos/52))), 34)
    screen.text_center(". .")
    screen.move(70+((phobos/52)*math.floor((phobos-50)/5))+(5*(1-(phobos/52))), 39)
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
    screen.level(math.floor(self/6.8)+math.floor(deimos/6.8))
    screen.circle(58-((math.floor(deimos/50.1)*math.floor((deimos-50)/5))), 36, 3)
    screen.stroke()

    screen.aa(0)
end

drawPlanets.jupiter = function(self, moons, ring)
    screen.aa(1)
    screen.move((46-(ring/3)), 33)
    screen.level(math.floor(self/10)+math.floor(ring/33))
    screen.curve((46-(ring/3)), 33, 64, 26-math.floor(ring/40), (82+(ring/3)), 33)
    screen.stroke()

    screen.level(math.floor(self/10))
    screen.circle(64, 32, 21)
    screen.fill()

    screen.level(math.floor(self/10)+math.floor(ring/20))
    screen.curve((46-(ring/3)), 33, 64, 39+math.floor(ring/25), (82+(ring/3)), 33)
    screen.stroke()
    

    screen.level(math.floor(moons/6.8))
    screen.circle(36, 24, 2)
    screen.fill()
    screen.circle(28, 40, 4)
    screen.fill()
    screen.circle(75, 38, 3)
    screen.fill()
    screen.circle(98, 40, 1)
    screen.fill()

    screen.aa(0)
end

drawPlanets.saturn = function(self, mimas, rhea, titan, ring)
    screen.aa(1)

    screen.move((40-(ring/50)), 33)
    screen.level(math.floor(self/10)+math.floor(ring/20))
    screen.curve((40-(ring/50)), 33, 64, 24-math.floor(ring/25), (88+(ring/50)), 33)
    screen.stroke()
    screen.level(math.floor(self/10)+math.floor(ring/33)-math.floor(mimas/10))
    screen.move((25-(ring/50)), 33)
    screen.curve((25-(ring/50)), 33, 64, 24-math.floor(ring/25), (103+(ring/50)), 33)
    screen.stroke()
    screen.level(math.floor(self/10)+math.floor(ring/33))
    screen.move((10-(ring/50)), 33)
    screen.curve((10-(ring/50)), 33, 64, 22-math.floor(ring/25), (118+(ring/50)), 33)
    screen.stroke() 
    screen.move(87+(ring/53), 36)
    screen.text_center(". .   .")
    screen.move(26-(ring/75), 29)
    screen.text_center(". ..")
    screen.move(78+(ring/65), 32)
    screen.text_center(".   .")
    screen.move(32-(ring/45), 31)
    screen.text_center(". .")

    screen.level(math.floor(self/10))
    screen.circle(64, 32, 17)
    screen.fill()

    screen.level(math.floor(self/10)+math.floor(ring/33)+2)
    screen.curve((40-(ring/50)), 33, 64, 39+math.floor(ring/15), (88+(ring/50)), 33)
    screen.stroke()
    screen.level(math.floor(self/10)+math.floor(ring/33)+2-math.floor(mimas/10))
    screen.curve((25-(ring/50)), 33, 64, 45+math.floor(ring/15), (103+(ring/50)), 33)
    screen.stroke()
    screen.level(math.floor(mimas/10))
    screen.circle(25, 33, 3)
    screen.fill()
    screen.level(math.floor(self/10)+math.floor(ring/33)+2)
    screen.curve((10-(ring/50)), 33, 64, 52+math.floor(ring/15), (118+(ring/50)), 33)
    screen.stroke() 
    screen.move(50-(ring/33), 33)
    screen.text_center(". .   .")
    screen.move(53-(ring/81), 38)
    screen.text_center(". ..")
    screen.move(69+(ring/68), 36)
    screen.text_center(".   .")
    screen.move(75+(ring/43), 35)
    screen.text_center(". .")

    screen.level(math.floor(titan/10))
    screen.circle(82, 18, 2)
    screen.fill()

    screen.level(math.floor(rhea/12.5))
    screen.circle(84, 50, 4)
    screen.fill()
    screen.level(math.floor(rhea/10))
    screen.curve(78, 47, 83, 51, 90, 53)
    screen.stroke()

    screen.aa(0)
end

drawPlanets.uranus = function(self, titania, oberon, ring)
    screen.aa(1)

    screen.move(64,5)
    screen.level(math.floor(self/10)+math.floor(ring/50))
    screen.curve(64, 5, 64-math.floor(ring/5), 32, 64, 59)
    screen.stroke()

    screen.level(math.floor(self/10))
    screen.circle(64, 32, 13)
    screen.fill()

    screen.level(math.floor(self/10)+math.floor(ring/33)+2)
    screen.curve(64, 5, 64+math.floor(ring/5), 32, 64, 59)
    screen.stroke()

    screen.level(math.floor(titania/10))
    screen.circle(38, 47+(titania/42), 5)
    screen.fill()

    screen.level(math.floor(oberon/10))
    screen.circle(86, 25-(oberon/33), 3)
    screen.fill()

    
    screen.aa(0)
end

drawPlanets.neptune = function(self, triton, nereid, pluto, orcus, ring)
    screen.aa(1)

    screen.move(20, 33)
    screen.level(math.floor(self/10)+math.floor(ring/20))
    screen.curve(20, 33, 64, 25, 108, 32)
    screen.stroke()

    screen.move(64,32)
    screen.level(math.floor(triton/10))
    screen.circle(64, 32, 15)
    screen.level(math.floor(self/10))
    screen.circle(64, 32, 13)
    screen.fill()

    screen.level(math.floor(triton/10))
    screen.circle(88, 38-(triton/25), 5)
    screen.fill()
    
    screen.level(math.floor(nereid/10))
    screen.circle(35-(nereid/10), 28, 2)
    screen.fill()

    screen.level(math.floor(self/10)+math.floor(ring/20))
    screen.curve(20, 33, 64, 40, 108, 32)
    screen.stroke()

    screen.level(math.floor(pluto/10))
    screen.circle(12, 32, 3)
    screen.fill()

    screen.level(math.floor(orcus/10))
    screen.circle(116, 32, 3)
    screen.fill()

    
    screen.aa(0)
end

drawPlanets.planetx = function(self)
    screen.aa(1)
    screen.level(10+math.floor(self/20))
    screen.circle(74, 41, 1)
    screen.fill()

    screen.level(8+math.floor(self/20))
    screen.circle(64, 32, 15)
    screen.stroke()
    screen.level(3+math.floor(self/20))
    screen.circle(64, 32, 14)
    screen.stroke()
    screen.level(math.floor(self/20))
    screen.circle(64, 32, 13)
    screen.stroke()

    screen.aa(0)
end


return drawPlanets