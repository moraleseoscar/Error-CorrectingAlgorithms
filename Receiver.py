
def xor_binary_strings(str1, str2):
    if len(str1) != len(str2):
        print("Las cadenas deben tener la misma longitud para realizar la operación XOR.")
    result = ""
    for bit1, bit2 in zip(str1, str2):
        xor_result = int(bit1) ^ int(bit2)
        result += str(xor_result)
    return list(result)

def crc_receptor(trama):
    # print(len(trama))
    trama_lista = list(trama)
    crc32   = "100000100110000010001110110110111"
    # crc32 = "1001"
    
    # Inicia logica para CRC 32
    
    A = trama_lista[:33]
    trama_lista = trama_lista[33:]
    resultado = xor_binary_strings(''.join(A),crc32)

    # print("Trama ", trama_lista)
    # print("resultado ", resultado)

    while len(trama_lista) > 0:
        if resultado[0] == '0':
            resultado.pop(0)
            agregar = trama_lista.pop(0)
            resultado.append(agregar)
        if resultado[0] == '1':
            resultado = xor_binary_strings(resultado,crc32)
    
    # print(''.join(resultado))

    respuesta = True

    for x in resultado:
        if x != '0':
            respuesta = False
    
    mensaje_original = list(trama)
    
    if respuesta:
        for x in range(0,32):
            mensaje_original.pop()
    else:
        mensaje_original = 'Se encontraron problemas'
    
    return mensaje_original



print('Enter the hamming code received')
d=input()
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
    print('There is no error in the hamming code received')

elif((error)>=len(h_copy)):
    print('Error cannot be detected')

else:
    print('Error is in',error,'bit')

    if(h_copy[error-1]=='0'):
        h_copy[error-1]='1'

    elif(h_copy[error-1]=='1'):
        h_copy[error-1]='0'
        print('After correction hamming code is:- ')
    h_copy.reverse()
    print(int(''.join(map(str, h_copy))))

mensaje = input("Ingresa la trama: ")
receptorr = crc_receptor(mensaje)
print("Receptor CRC 32", ''.join(receptorr))
