#get location in specific frame
import bpy
cube=bpy.data.objects['Cube']
for f in cube.animation_data.action.fcurves:
    for k in f.keyframe_points:
        pos=cube.location
        print(pos)