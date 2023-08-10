import socket

HOST = "127.0.0.1"
PORT = 65432

seleccion = "1"
#message = "1110101101" #Debe devolver 110101
#message = "1000101101000010010001110010101110001111" #Debe devolver 10001011
message = "1001100010000111011110110110001010110001100001"

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    full_message = f"{seleccion}\n{message}"
    s.sendall(full_message.encode("utf-8"))

print("Mensajes enviados exitosamente.")