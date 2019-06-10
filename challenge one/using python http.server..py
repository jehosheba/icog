# this code return location of an object from blender using python http.server.

from http.server import HTTPServer, BaseHTTPRequestHandler
import bpy #import the library


class Serv(BaseHTTPRequestHandler):

    def do_GET(self):
        if self.path == '/location':
            self.send_response(200, 'message')
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            l = bpy.data.objects["Cube"].location
            x = "x : " + str(l[0]) + ", y : " + str(l[1]) + ", Z : " + str(l[2])
            self.wfile.write(bytes(x, 'utf-8'))

httpd = HTTPServer(('localhost', 8099), Serv)
httpd.serve_forever()