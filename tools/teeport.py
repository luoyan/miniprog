import socket
import threading
import sys
import os
import time
import os
import datetime
class SocketClientThread(threading.Thread):
    def __init__(self, socket, dest_host, dest_port, file_name):
        super(SocketClientThread, self).__init__()
        self.socket = socket
	self.dest_host = dest_host
	self.dest_port = dest_port
	self.file_name = file_name

    def run(self):
        data = ''
	now_timestamp = datetime.datetime.now().strftime('%Y-%m-%d_%H:%M:%S.%f')
	real_file_name = self.file_name + "." + now_timestamp
	#if os.path.exists(self.file_name):
	#os.rename(self.file_name, self.file_name + "." + now_timestamp)
	f = open(real_file_name, 'a')
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((self.dest_host, self.dest_port))
        while True:
            chunk = self.socket.recv(1024)
	    s.sendall(chunk)
	    f.write(chunk)
	    f.flush()
            if chunk == '':
                break
            data += chunk
        print 'recv [' + data + ']'
        self.socket.close()
	#print "closing file " + self.file_name
	#time.sleep(100)
	f.close()

    def join(self, timeout=None):
        threading.Thread.join(self, timeout)

def listen(port, dest_host, dest_port, file_name):
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    #serversocket.bind((socket.gethostname(), port))
    serversocket.bind(('0.0.0.0', port))
    serversocket.listen(5)
    while True:
        (clientsocket, address) = serversocket.accept()
        ct = SocketClientThread(clientsocket, dest_host, dest_port, file_name)
        ct.start()
        ct.join()

def usage(argv0):
    print argv0 + ' port dest_host dest_port file_name'
if __name__ == '__main__':
    if len(sys.argv) != 5:
        usage(sys.argv[0])
        sys.exit(-1)
    port = int(sys.argv[1])
    dest_host = sys.argv[2]
    dest_port = int(sys.argv[3])
    file_name = sys.argv[4]
    file_name = file_name + "." + dest_host + "." + str(dest_port)
    listen(port, dest_host, dest_port, file_name)
