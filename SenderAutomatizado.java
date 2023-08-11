// package Redes.Laboratorio 2.Parte 2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class SenderAutomatizado {

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 65432;
        Scanner scanner = new Scanner(System.in);
        Set<String> palabrasGeneradas = generarPalabras();
        try {
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // ! For x in range 10k
            
            System.out.println("\nOpciones:");
            System.out.println("1. Hamming");
            System.out.println("2. CRC-32\n");
            System.out.print("Selecciona una opción: ");
            int choice = scanner.nextInt();
            System.out.println("\n¿ Desea la layer de ruido ?");
            System.out.println("1. Si");
            System.out.println("2. No\n");
            System.out.print("Selecciona una opción: ");
            int ruido_activo = scanner.nextInt();

            // while(true){
            for (String palabra : palabrasGeneradas) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    // Aca va toda la lógica con "palabra"
                // System.out.print("\nIngresa un texto: ");
                // String inputText = scanner.next();
                String inputText = palabra;
                // scanner.next();
                // System.out.println("\nOpciones:");
                // Conversión de texto a binario.
                String binaryResult = textToBinary(inputText);

                System.out.print("\n");
                switch (choice) {
                    case 1:
                        binaryResult = hamming(binaryResult);
                        // Aquí podrías agregar la lógica para la codificación Hamming
                        System.out.println("\n -- Opción Hamming seleccionada -- ");
                        break;
                        case 2:
                        binaryResult = crc_emisor(binaryResult);
                        // Calcula el valor CRC-32 y muestra el resultado
                        System.out.println("\n -- Opción CRC32 seleccionada -- ");
                        break;
                        case 3:
                        // Calcula el valor CRC-32 y muestra el resultado
                        System.out.println("\n -- Gracias por usar signal 2! -- ");
                        break;
                        default:
                        System.out.println("\n -- Opción no válida. -- ");
                    }
                    if(ruido_activo == 1){
                        binaryResult = ApplyNoise(binaryResult);
                    }
                    System.out.println("\nTexto:      " + inputText);
                    System.out.println("En binario: " + binaryResult);
                    
                    // String message = consoleReader.readLine();
                    String resultado = String.valueOf(choice)+"\n"+binaryResult;
                    System.out.println("\n ---------------------------------------------\n");
                    if (String.valueOf(choice).equalsIgnoreCase("3")) {
                        break;
                    }
                    out.println(resultado);
                }
                
            
            socket.close();
            consoleReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
    public static String invertString(String input) {
        if (input.equals("1")) {
            return "0";
        } else if (input.equals("0")) {
            return "1";
        } else {
            return "";
        }
    }
    public static String ApplyNoise(String binaryResult) {

        System.out.println("\n -------------- Aplicando Ruido --------------");
        System.out.println("\n Binario Original    --> " + binaryResult );
        // System.out.println("\n --\n");

        Random random = new Random();
        List<String> noise = new ArrayList<>();
        for (char c : binaryResult.toCharArray()) {
            int randomNumber = random.nextInt(100);
            // System.out.println("Numero Random " + randomNumber);
            if( String.valueOf(randomNumber).equals("1")){
                // System.out.println("Se cambio bit de " + new String(new char[]{c})+" a "+invertString(new String(new char[]{c})));
                noise.add(invertString(new String(new char[]{c})));
            }else{
                noise.add(new String(new char[]{c}));
            }
        }

        System.out.println(" Binario Noise Layer --> " + concatenateStrings(noise) +"\n");

        return concatenateStrings(noise);
    }

    public static Set<String> generarPalabras() {
        Set<String> palabrasGeneradas = new HashSet<>();
        Random random = new Random();
        char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        while (palabrasGeneradas.size() < 100) { // Puedes ajustar la cantidad de palabras a generar
            StringBuilder palabra = new StringBuilder();
            int longitud = random.nextInt(4) + 1;

            for (int i = 0; i < longitud; i++) {
                char letra = letras[random.nextInt(letras.length)];
                palabra.append(letra);
            }

            palabrasGeneradas.add(palabra.toString());
        }

        return palabrasGeneradas;
    }

    
    public static String textToBinary(String text) {
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (char c : text.toCharArray()) {
            String binaryString = Integer.toBinaryString(c);
            binaryStringBuilder.append(String.format("%8s", binaryString).replace(' ', '0'));
        }
        return binaryStringBuilder.toString();
    }
    public static List<String> xorLists(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("Las listas deben tener la misma longitud");
        }
        
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            String bit1 = list1.get(i);
            String bit2 = list2.get(i);
            
            if (bit1.equals("0") && bit2.equals("0")) {
                result.add("0");
            } else if (bit1.equals("1") && bit2.equals("0")) {
                result.add("1");
            } else if (bit1.equals("0") && bit2.equals("1")) {
                result.add("1");
            } else if (bit1.equals("1") && bit2.equals("1")) {
                result.add("0");
            } else {
                throw new IllegalArgumentException("Las listas deben contener solo caracteres '0' y '1'");
            }
        }
        
        return result;
    }
    public static String concatenateStrings(List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        
        for (String str : strings) {
            stringBuilder.append(str);
        }
        
        return stringBuilder.toString();
    }
    public static String crc_emisor(String trama) {

        // El input se convierte a lista
        List<String> trama_lista = new ArrayList<>();
        for (char c : trama.toCharArray()) {
            trama_lista.add(new String(new char[]{c}));
        }

        // Polinomio que se utilizara para las operaciones
        List<String> crc32 = new ArrayList<>();
        char[] temp_crc32= "100000100110000010001110110110111".toCharArray();
        for (char c : temp_crc32) {
            crc32.add(new String(new char[]{c}));
        }
        // Se agregan la cantidad de 0 a la trama

        for (int i = 0; i < (crc32.size()-1); i++) {
            trama_lista.add("0");
        }

        
        
        // ! Inicia procedimiento para CRC 32
        
        
        // * Se declara A

        List<String> A = new ArrayList<>();
        int limit = Math.min(33, trama_lista.size());
        for (int i = 0; i < limit; i++) {
            A.add(trama_lista.get(i));
        }
        
        // * Se actualiza trama 
        
        if (trama_lista.size() >= 33) {
            trama_lista.subList(0, 33).clear();
        } else {
            System.out.println("La lista no tiene suficientes elementos para eliminar 33.");
        }
        
        // * Se realiza la operacion entre A y crc32

        List<String> resultado = xorLists(A, crc32);
        
        // System.out.println("Trama inicial" + trama_lista);
        // System.out.println("Resultado inicial" + resultado);


        // * Inicia Ciclo

        while (trama_lista.size() > 0) {
            if(resultado.get(0).equals("0")){
                resultado.remove(0);
                String agregar = trama_lista.remove(0);
                resultado.add(agregar);
                // System.out.println("Trama C0 " + trama_lista);
                // System.out.println("Resultado C0" + resultado);
            }
            if(resultado.get(0).equals("1")){
                resultado = xorLists(resultado, crc32);
                // System.out.println("Trama C1" + trama_lista);
                // System.out.println("Resultado C1 " + resultado);
            }

        }
        // System.out.println("Trama 0" + trama_lista);
        // System.out.println("Resultado 0" + resultado);

        List<String> respuesta = new ArrayList<>();
        for (char c : trama.toCharArray()) {
            respuesta.add(new String(new char[]{c}));
        }

        int startIndex = Math.max(0, resultado.size() - 32); // Asegurarse de no exceder el tamaño de la lista
        for (int i = startIndex; i < resultado.size(); i++) {
            respuesta.add(resultado.get(i));
        }
        String resultadoConcatenado = concatenateStrings(respuesta);
        // System.out.println("asdfaf " + trama_lista.size());
        // System.out.println("asdfaf " + A);
        // System.out.println("asdfaf " + A.size());
        // System.out.println("asdfaf " + crc32);
        // System.out.println("asdfaf " + crc32.size());

        return resultadoConcatenado;
    }
    public static String hamming(String trama) {

        // Scanner scanner = new Scanner(System.in);
        // System.out.print("Enter the data bits: ");
        String inputData = trama;

        int len = inputData.length();
        char[] data = new char[len];
        inputData.getChars(0, len, data, 0);
        int r = 0;
        int c = 0;
        int ch = 0;
        int j = 0;
        int[] h = new int[100];

        while ((len + r + 1) > Math.pow(2, r)) {
            r++;
        }

        for (int i = 0; i < (r + len); i++) {
            int p = (int) Math.pow(2, c);
            if (p == (i + 1)) {
                h[i] = 0;
                c++;
            } else {
                h[i] = data[j] - '0';
                j++;
            }
        }

        for (int parity = 0; parity < (r + len); parity++) {
            int ph = (int) Math.pow(2, ch);
            if (ph == (parity + 1)) {
                int startIndex = ph - 1;
                int i = startIndex;
                int[] toXor = new int[100];
                int toXorIdx = 0;

                while (i < (r + len)) {
                    for (int idx = i; idx < i + ph; idx++) {
                        if (idx < (r + len)) {
                            toXor[toXorIdx] = h[idx];
                            toXorIdx++;
                        }
                    }
                    i += 2 * ph;
                }

                for (int z = 1; z < toXorIdx; z++) {
                    h[startIndex] = h[startIndex] ^ toXor[z];
                }
                ch++;
            }
        }
        List<String> A = new ArrayList<>();
        for (int i = 0; i < (r + len); i++) {
            A.add(String.valueOf(h[i]));
        }
        String hString = concatenateStrings(A);
        return hString;
    }
    
}