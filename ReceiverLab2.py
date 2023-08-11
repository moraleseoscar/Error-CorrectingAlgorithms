
import socket

#Variables para conexión y recibimiento de mensajes
HOST = "127.0.0.1" # IP, capa de Red. 127.0.0.1 es localhost
PORT = 65432 # Puerto, capa de Transporte


#-------------------------------------Decodificador de Binario------------------------------------------

def binary_to_words(binary_string):
    # Divide la cadena binaria en grupos de 8 bits (un byte)
    bytes_list = [binary_string[i:i+8] for i in range(0, len(binary_string), 8)]
    
    # Convierte cada byte en su equivalente decimal y luego en un caracter
    characters = [chr(int(byte, 2)) for byte in bytes_list]
    
    # Une los caracteres para formar las palabras
    words = ''.join(characters)
    
    return words

#-------------------------------------CRC Receptor------------------------------------------
def xor_binary_strings(str1, str2):
    if len(str1) != len(str2):
        print("Las cadenas deben tener la misma longitud para realizar la operación XOR.")
    result = ""
    for bit1, bit2 in zip(str1, str2):
        xor_result = int(bit1) ^ int(bit2)
        result += str(xor_result)
    return list(result)

def crc_receptor(trama):
    trama_lista = list(trama)
    crc32   = "100000100110000010001110110110111"

    # Inicia logica para CRC 32
    A = trama_lista[:33]
    trama_lista = trama_lista[33:]
    resultado = xor_binary_strings(''.join(A),crc32)

    while len(trama_lista) > 0:
        if resultado[0] == '0':
            resultado.pop(0)
            agregar = trama_lista.pop(0)
            resultado.append(agregar)
        if resultado[0] == '1':
            resultado = xor_binary_strings(resultado,crc32)
    
    respuesta = True

    for x in resultado:
        if x != '0':
            respuesta = False
    
    mensaje_original = list(trama)
    
    if respuesta:
        for x in range(0,32):
            mensaje_original.pop()
    else:
        print('Se encontraron problemas')
        mensaje_original = '010100110110010100100000011001010110111001100011011011110110111001110100011100100110000101110010011011110110111000100000011100000111001001101111011000100110110001100101011011010110000101110011'
    
    return mensaje_original

#-------------------------------------Hamming Receptor------------------------------------------

def hamming_receptor(d):
    data=list(d)
    data.reverse()
    c,ch,j,r,error,h,parity_list,h_copy=0,0,0,0,0,[],[],[]

    for k in range(0,len(data)):
        p=(2**c)
        h.append(int(data[k]))
        h_copy.append(data[k])
        if(p==(k+1)):
            c=c+1
            
    for parity in range(0,(len(h))):
        ph=(2**ch)
        if(ph==(parity+1)):

            startIndex=ph-1
            i=startIndex
            toXor=[]

            while(i<len(h)):
                block=h[i:i+ph]
                toXor.extend(block)
                i+=2*ph

            for z in range(1,len(toXor)):
                h[startIndex]=h[startIndex]^toXor[z]
            parity_list.append(h[parity])
            ch+=1
    parity_list.reverse()
    error=sum(int(parity_list) * (2 ** i) for i, parity_list in enumerate(parity_list[::-1]))

    if((error)==0):
        print('No hay error en la trama recibida')
        data.reverse()
        decoded_message = "".join(str(bit) for i, bit in enumerate(data) if i not in [2**k - 1 for k in range(len(data))])
    elif((error)>=len(h_copy)):
        print('No hay error para detectar')
        data.reverse()
        decoded_message = "".join(str(bit) for i, bit in enumerate(data) if i not in [2**k - 1 for k in range(len(data))])
    else:
        print('Hay error en el bit ',error)
        h_copy.reverse()
        if(h_copy[error-1]=='0'):
            h_copy[error-1]='1'

        elif(h_copy[error-1]=='1'):
            h_copy[error-1]='0'
        print('Despues de correccion por Hamming:- ',int(''.join(map(str, h_copy))))
        decoded_message = "".join(str(bit) for i, bit in enumerate(h_copy) if i not in [2**k - 1 for k in range(len(h_copy))])
    return decoded_message

#-------------------------------------Conexión Socket------------------------------------------
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()

    conn, addr = s.accept()
    with conn:
        while True:
            try:
                print( "\n--------------------------------------------")
                print(f"\n Conexion Entrante del proceso {addr}")
                full_data = conn.recv(1024).decode("utf-8")
                selections_and_messages = full_data.split("\n")
                # print(selections_and_messages)
                selAlgorithm, message,_ = selections_and_messages
                message = message.replace('\r','')
                
                print("\nTrama recibida: ", message) 
                if(selAlgorithm == "1"):
                    print("Algoritmo seleccionado: Hamming-Code 7,4") 
                    # Mandamos trama a Hamming
                    print("\nReceptor por Hamming:\n----------------------")
                    receptor = hamming_receptor(message)
                    print("Trama recibida:", receptor)
                else:
                    print("Algoritmo seleccionado: CRC-32") 
                    # Mandamos trama a CRC
                    print("\nReceptor por CRC32:\n----------------------")
                    receptor = ''.join(crc_receptor(message))
                    print(receptor, "\n")
                print("Letra recibida:", binary_to_words(receptor), "\n")
            except:
                break
            