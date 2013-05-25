--[[
(C) Copyright 2013 William Dyce

All rights reserved. This program and the accompanying materials
are made available under the terms of the GNU Lesser General Public License
(LGPL) version 2.1 which accompanies this distribution, and is available at
http://www.gnu.org/licenses/lgpl-2.1.html

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License fDEFAULT_W, DEFAULT_H, zor more details.
--]]

--[[------------------------------------------------------------
IMPORTS
--]]------------------------------------------------------------

Class = require("hump/class")
Camera = require("hump/camera")
Vector = require("hump/vector-light")

useful = require("unrequited/useful")
audio = require("unrequited/audio")
scaling = require("unrequited/scaling")
input = require("unrequited/input")
GameObject = require("unrequited/GameObject")
Tile = require("unrequited/Tile")
CollisionGrid = require("unrequited/CollisionGrid")


--[[------------------------------------------------------------
GLOBAL SETTINGS
--]]------------------------------------------------------------

DEBUG = true

--[[------------------------------------------------------------
GLOBAL VARIABLES
--]]------------------------------------------------------------

local camera
local player









--[[------------------------------------------------------------
PLANET CLASS
--]]------------------------------------------------------------
local Planet = Class
{
  type = GameObject.TYPE.new("Planet"),
      
  init = function(self, x, y)
    GameObject.init(self, x, y, 12, 12)
  end
}
Planet:include(GameObject)

function Planet:draw()
  
  love.graphics.setColor(255, 0, 0)
  love.graphics.circle("fill", self:centreX(), self:centreY(), 8)
  
  love.graphics.setColor(255, 255, 255)
  
  GameObject.draw(self)
end











--[[------------------------------------------------------------
SHIP CLASS
--]]------------------------------------------------------------

local Ship = Class
{
  type = GameObject.TYPE.new("Ship"),
      
  init = function(self, orbitX, orbitY, orbitR)
    GameObject.init(self, orbitX + orbitR - 8, orbitY - 8, 4, 4)
    self.orbitX = orbitX
    self.orbitY = orbitY
    self.orbitR = orbitR
    self.angle = 0
  end,
  
  speed = 40
}
Ship:include(GameObject)

function Ship:draw()
  
  love.graphics.setColor(255, 255, 255, 50)
  love.graphics.circle("line", self.orbitX, self.orbitY, self.orbitR)
  
  love.graphics.setColor(255, 255, 255)
  
  GameObject.draw(self)
end

function Ship:update(dt)
  self.angle = self.angle + dt*self.speed/self.orbitR
  self:centreOn(self.orbitX + math.cos(self.angle)*self.orbitR, 
                self.orbitY + math.sin(self.angle)*self.orbitR)
end







--[[------------------------------------------------------------
THUMB CLASS
--]]------------------------------------------------------------

local Thumb = Class
{
  type = GameObject.TYPE.new("Thumb"),
      
  init = function(self, x, y, dx, dy)
    GameObject.init(self, x, y, 2, 2)
    self.dx, self.dy = dx*self.BASE_SPEED, dy*self.BASE_SPEED
  end,
  
  FRICTION_X = 5,
  FRICTION_Y = 5,
  BASE_SPEED = 90

}
Thumb:include(GameObject)

function Thumb:update(dt)
  if (math.abs(self.dx) < 1) or (math.abs(self.dy) < 1) then
    self.purge = true
  end
  
  GameObject.update(self, dt)
end







--[[------------------------------------------------------------
PLAYER CLASS
--]]------------------------------------------------------------

local Player = Class
{
  type = GameObject.TYPE.new("Player"),
      
  init = function(self, x, y)
    GameObject.init(self, x, y, 2, 2)
    self.thumbCharge = 0
  end,

  MAX_CHARGE = 4

}
Player:include(GameObject)


function Player:update(dt)
  if love.mouse.isDown("l") then
    self.thumbCharge = math.min(self.MAX_CHARGE, self.thumbCharge + dt)
  elseif self.thumbCharge > 0 then
    local x, y = camera:worldCoords(love.mouse.getPosition())
    x, y = Vector.normalize(x - player:centreX(), y - player:centreY())
    Thumb(self:centreX(), self:centreY(), x*self.thumbCharge, y*self.thumbCharge)
    self.thumbCharge = 0
  end
end











--[[------------------------------------------------------------
LOVE CALLBACKS
--]]------------------------------------------------------------

function love.load(arg)
    
  -- set up the screen resolution
  if (not scaling:setup(1280, 720, (not DEBUG))) then
    print("Failed to set mode")
    love.event.push("quit")
  end

  -- initialise random
  math.randomseed(os.time())
  
  -- camera
  camera = Camera(0, 0)
  camera:zoomTo(3, 3)
  
  -- initialise
  player = Player(300, 300)
  
  Planet(100, 400)
  Planet(300, 100)
  
  Ship(300, 200, 200)
  Ship(400, 500, 100)
  Ship(100, 300, 250)
  Ship(220, 300, 60)
end

function love.focus(f)
end

function love.quit()
end

function love.mousepressed( x, y, button )
end

function love.mousereleased( x, y, button )
end

function love.keypressed(key, uni)
  if key == "escape" then
    love.event.push("quit")
  end
end

function keyreleased(key, uni)
end

MIN_DT = 1/60
MAX_DT = 1/10
function love.update(dt)
  dt = useful.clamp(dt, MIN_DT, MAX_DT)
  
  input:update(dt)
  
  GameObject.updateAll(dt)
  
  camera:lookAt(player:centreX(), player:centreY())
end

function love.draw()
  --camera:attach()
    GameObject.drawAll()
  --camera:detach()
end
