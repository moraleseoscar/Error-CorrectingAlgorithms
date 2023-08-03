d=input('Enter the hamming code received: ')
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
    print("Trama recibida: ", decoded_message)
elif((error)>=len(h_copy)):
    print('No hay error para detectar')
    data.reverse()
    decoded_message = "".join(str(bit) for i, bit in enumerate(data) if i not in [2**k - 1 for k in range(len(data))])
    print("Trama recibida: ", decoded_message)
else:
    print('Hay error en el bit ',error)
    h_copy.reverse()
    if(h_copy[error-1]=='0'):
        h_copy[error-1]='1'

    elif(h_copy[error-1]=='1'):
        h_copy[error-1]='0'
    print('Despues de correccion por Hamming:- ',int(''.join(map(str, h_copy))))
    decoded_message = "".join(str(bit) for i, bit in enumerate(h_copy) if i not in [2**k - 1 for k in range(len(h_copy))])
    print("Trama recibida: ", decoded_message)

