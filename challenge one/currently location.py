#currently location
import bpy

obj = bpy.context.active_object
ol = obj.location

# set current frame to 1
bpy.context.scene.frame_set(1)

pos_1 = (ol.x, ol.y, ol.z)

# set current frame to 5
bpy.context.scene.frame_set(5)
pos_5 = (ol.x, ol.y, ol.z)

# set current frame to 10
bpy.context.scene.frame_set(10)
pos_10 = (ol.x, ol.y, ol.z)

# set current frame back to 1
bpy.context.scene.frame_set(1)

print('Frame 1 {0}, id: {1}'.format(pos_1, id(pos_1)))
print('Frame 5 {0}, id: {1}'.format(pos_5, id(pos_5)))
print('Frame 10 {0}, id: {1}'.format(pos_10, id(pos_10)))