--[[
(C) Copyright 2013 William Dyce

All rights reserved. This program and the accompanying materials
are made available under the terms of the GNU Lesser General Public License
(LGPL) version 2.1 which accompanies this distribution, and is available at
http://www.gnu.org/licenses/lgpl-2.1.html

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.
--]]

--[[------------------------------------------------------------
IMPORTS
--]]------------------------------------------------------------

local Class = require("hump/class")

--[[------------------------------------------------------------
GAMEOBJECT CLASS
--]]------------------------------------------------------------

--[[------------------------------------------------------------
Initialisation
--]]

local GameObject = Class
{
  -- container
  INSTANCES = { },
      
  -- identifiers
  NEXT_ID = 1,
  
  -- type
  TYPE = { },
      
  -- constructor
  init = function(self, x, y, w, h)
    -- save attributes
    self.w        = (w or 0)
    self.h        = (h or 0)
    self.x        = x
    self.y        = y
    self.prevx    = self.x
    self.prevy    = self.y
    
    -- are there other objects of this type?
    if (not GameObject.INSTANCES[self.type]) then
      GameObject.INSTANCES[self.type] = {}
    end
    table.insert(GameObject.INSTANCES[self.type], self) 
    
    -- assign identifier
    self.id = GameObject.NEXT_ID
    GameObject.NEXT_ID = GameObject.NEXT_ID + 1
    self.name = self:typename() .. '(' .. tostring(self.id) .. ')'
    
  end,
  
  -- default attribute values
  dx = 0,
  dy = 0
}

--[[------------------------------------------------------------
TYPES
--]]------------------------------------------------------------

function GameObject.TYPE.new(typename)
  local type_index = #(GameObject.TYPE) + 1
  useful.bind(GameObject.TYPE, typename, type_index)
  return type_index
end


function GameObject:typename()
  return GameObject.TYPE[self.type]
end

function GameObject:isType(typename)
  return (self.type == GameObject.TYPE[typename])
end

--[[------------------------------------------------------------
CONTAINER
--]]------------------------------------------------------------

function GameObject.purgeAll()
  for type, _ in pairs(GameObject.INSTANCES) do
    GameObject.INSTANCES[type] = { }
  end
  GameObject.NEXT_ID = 1
end

function GameObject.get(typename, i)
  i = (i or 1)
  local type = GameObject.TYPE[typename]
  local objects = GameObject.INSTANCES[type] 
  if objects and (i <= #objects) then
    return (GameObject.INSTANCES[type][i])
  else
    return nil
  end
end

function GameObject.getAll(typename)
  if typename then
    return 
      pairs(GameObject.INSTANCES[GameObject.TYPE[typename]])
  else
    return pairs(GameObject.INSTANCES)
  end
end

function GameObject.count(typename)
  if typename then
    return #(GameObject.INSTANCES[GameObject.TYPE[typename]])
  else
    local count = 0
    for _, objects_of_type 
    in pairs(GameObject.INSTANCES) do
      count = count + #(objects_of_type)
    end
    return count
  end
end

function GameObject.getSuchThat(predicate, typename)
  local set = {}
  if typename then
    local type = GameObject.TYPE[typename]
    if GameObject.INSTANCES[type] then
      for _, object in pairs(GameObject.INSTANCES[type]) do
        if predicate(object) then
          table.insert(set, object)
        end
      end
    end
  else
    for _, objects_of_type 
    in pairs(GameObject.INSTANCES) do
      for _, object in pairs(objects_of_type) do
        if predicate(object) then
          table.insert(set, object)
        end
      end
    end
  end
  return set
end

function GameObject.countSuchThat(predicate, typename)
  local count = 0
  if typename then
    local type = GameObject.TYPE[typename]
    if GameObject.INSTANCES[type] then
      for _, object in pairs(GameObject.INSTANCES[type]) do
        if predicate(object) then
          count = count + 1
        end
      end
    end
  else
    for _, objects_of_type 
    in pairs(GameObject.INSTANCES) do
      for _, object in pairs(objects_of_type) do
        if predicate(object) then
          count = count + 1
        end
      end
    end
  end
  return count
end

function GameObject.updateAll(dt, level, view)
  
  -- update objects
  -- ...for each type of object
  for type, objects_of_type in pairs(GameObject.INSTANCES) do
    -- ...for each object
    useful.map(objects_of_type,
      function(object)
        -- ...update the object
        object:update(dt, level, view)
        -- ...check collisions with other object
        -- ...... for each other type of object
        for othertype, objects_of_othertype 
            in pairs(GameObject.INSTANCES) do
          if object:collidesType(othertype) then
            -- ...... for each object of this other type
            useful.map(objects_of_othertype,
                function(otherobject)
                  -- check collisions between objects
                  if object:isColliding(otherobject) then
                    object:eventCollision(otherobject, level)
                  end
                end)
          end
        end  
    end)
  end
end

function GameObject.drawAll(view)
  -- for each type of object
  for t, object_type in pairs(GameObject.INSTANCES) do
    -- for each object
    useful.map(object_type,
      function(object)
        -- if the object is in view...
        if (not view) or object:isColliding(view) then
          -- ...draw the object
          object:draw(view)
        end
    end)
  end
end

function GameObject.mapToAll(f)
  -- for each type of object
  for _, type_instances in pairs(GameObject.INSTANCES) do
    -- for each object
    useful.map(type_instances, f)
  end
end

function GameObject.mapToType(typename, f)
  local type_instances = 
    GameObject.INSTANCES[GameObject.TYPE[typename]]
  useful.map(type_instances, f)
end

function GameObject.trueForAny(typename, f)
  local type_instances = 
    GameObject.INSTANCES[GameObject.TYPE[typename]]
  for id, obj in pairs(type_instances) do
    if f(obj) then
      return true
    end
  end
  return false
end

function GameObject.trueForAll(typename, f)
  local type_instances = 
    GameObject.INSTANCES[GameObject.TYPE[typename]]
  local result = false
  for id, obj in pairs(type_instances) do
    result = (result and f(obj))
  end
  return result
end



--[[------------------------------------------------------------
METHODS
--]]------------------------------------------------------------

--[[----------------------------------------------------------------------------
Collisions
--]]

function GameObject:centreOn(x, y)
  self.x, self.y = x - self.w/2, y - self.h/2
end

function GameObject:centreX()
  return self.x + self.w/2
end

function GameObject:centreY()
  return self.y + self.h/2
end

function GameObject:snap_from_collision(dx, dy, collisiongrid, max, type)
  local i = 0
  while collisiongrid:collision(self, self.x, self.y, type) 
  and (not max or i < max)  do
    self.x = self.x + dx
    self.y = self.y + dy
    i = i + 1
  end
end

function GameObject:snap_to_collision(dx, dy, collisiongrid, max, type)
  local i = 0
  while not collisiongrid:collision(self, self.x + dx, self.y + dy, type) 
        and (not max or i < max)  do
    self.x = self.x + dx
    self.y = self.y + dy
    i = i + 1
  end
end

function GameObject:eventCollision(other)
  -- override me!
end

function GameObject:collidesType(type)
  -- override me!
  return false
end

function GameObject:isColliding(other)
  -- no self collisions
  if self == other then
    return false
  end
  -- horizontally seperate ? 
  local v1x = (other.x + other.w) - self.x
  local v2x = (self.x + self.w) - other.x
  if useful.sign(v1x) ~= useful.sign(v2x) then
    return false
  end
  -- vertically seperate ?
  local v1y = (self.y + self.h) - other.y
  local v2y = (other.y + other.h) - self.y
  if useful.sign(v1y) ~= useful.sign(v2y) then
    return false
  end
  
  -- in every other case there is a collision
  return true
end

function GameObject:isCollidingPoint(x, y)
  return (x >= self.x and x <= self.x + self.w
        and y >= self.y and y <= self.y + self.h)
end

--[[------------------------------------------------------------
Game loop
--]]

function GameObject:update(dt, level)

  -- object may have several fisix settings
  local fisix = (self.fisix or self)
  
  -- gravity
  if fisix.GRAVITY and self.airborne then
    self.dy = self.dy + fisix.GRAVITY*dt
  end
  
  -- friction
  if (self.dx ~= 0) and fisix.FRICTION_X and (fisix.FRICTION_X ~= 0) then
    self.dx = self.dx / (math.pow(fisix.FRICTION_X, dt))
  end
  if (self.dy ~= 0) and fisix.FRICTION_Y and (fisix.FRICTION_Y ~= 0) then
    self.dy = self.dy / (math.pow(fisix.FRICTION_Y, dt))
  end
  
  -- terminal velocity
  local abs_dx, abs_dy = math.abs(self.dx), math.abs(self.dy)
  if (self.dx ~= 0) and fisix.MAX_DX and (abs_dx > fisix.MAX_DX) then
    self.dx = fisix.MAX_DX*useful.sign(self.dx)
  end
  if (self.dy ~= 0) and fisix.MAX_DY and (abs_dy > fisix.MAX_DY) then
    self.dy = fisix.MAX_DY*useful.sign(self.dy)
  end
  
  -- clamp less than epsilon inertia to 0
  if math.abs(self.dx) < 0.01 then self.dx = 0 end
  if math.abs(self.dy) < 0.01 then self.dy = 0 end
  
  if GameObject.COLLISIONGRID then
    local collisiongrid = GameObject.COLLISIONGRID
    -- check if we're on the ground
    self.airborne = 
      ((not collisiongrid:pixelCollision(self.x, self.y + self.h + 1, collide_type)
      and (not collisiongrid:pixelCollision(self.x + self.w, self.y + self.h + 1, collide_type))))
    if not self.airborne and self.dy > 0 then
      if collisiongrid:collision(self, collide_type) then
        self:snap_from_collision(0, -1, collisiongrid, math.abs(self.dy), collide_type)
      end
      self.dy = 0
    end 
    
    -- move HORIZONTALLY FIRST
    if self.dx ~= 0 then
      local move_x = self.dx * dt
      local new_x = self.x + move_x
      self.prevx = self.x
      -- is new x in collision ?
      if collisiongrid:collision(self, new_x, self.y) then
        -- move as far as possible towards new position
        self:snap_to_collision(useful.sign(self.dx), 0, 
                          collisiongrid, math.abs(self.dx))
        self.dx = 0
      else
        -- if not move to new position
        self.x = new_x
      end
    end
    
    -- move the object VERTICALLY SECOND
    if self.dy ~= 0 then
      local move_y = self.dy*dt
      local new_y = self.y + move_y
      self.prevy = self.y
      -- is new y position free ?
      if collisiongrid:collision(self, self.x, new_y) then
        -- if not move as far as possible
        self:snap_to_collision(0, useful.sign(self.dy), collisiongrid, math.abs(self.dy))
        self.dy = 0
      else
        -- if so move to new position
        self.y = new_y
      end
    end
  else -- not self.COLLISIONGRID
    self.x = self.x + self.dx*dt
    self.y = self.y + self.dy*dt
  end
end

function GameObject:draw()
  if DEBUG then
    self.DEBUG_VIEW:draw(self)
  end
end

GameObject.DEBUG_VIEW = 
{
  draw = function(self, target)
    love.graphics.rectangle("line", 
        target.x, target.y, target.w, target.h)
    love.graphics.print(target.name, 
        target.x, target.y+32)
  end
}

--[[------------------------------------------------------------
EXPORT
--]]------------------------------------------------------------

return GameObject