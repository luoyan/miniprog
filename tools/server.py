import socket
import threading
import sys
import os
class SocketClientThread(threading.Thread):
    def __init__(self, socket):
        super(SocketClientThread, self).__init__()
        self.socket = socket

    def run(self):
        data = ''
        while True:
            chunk = self.socket.recv(1024)
            if chunk == '':
                break
            data += chunk
        print 'recv [' + data + ']'
        self.socket.close()

    def join(self, timeout=None):
        threading.Thread.join(self, timeout)

def listen(port):
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    #serversocket.bind((socket.gethostname(), port))
    serversocket.bind(('0.0.0.0', port))
    serversocket.listen(5)
    while True:
        (clientsocket, address) = serversocket.accept()
        ct = SocketClientThread(clientsocket)
        ct.start()
        ct.join()

def usage(argv0):
    print argv0 + ' port'
if __name__ == '__main__':
    if len(sys.argv) != 2:
        usage(sys.argv[0])
        sys.exit(-1)
    port = int(sys.argv[1])
    listen(port)
