import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class App {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        int mode = 0;

        
        while(mode != 4){
            printMenu();
            mode = scanner.nextInt();

            switch(mode){
                case 1: subnetCalc();
                        break;

                case 2: //checkClass();
                        break;

                case 3: //checkType();
                        break;
                
                case 4: break;

                default: System.out.println("WRONG INPUT TRY AGAIN");
                         break;
            }
        }
    }

    public static void subnetCalc(){
        IpAdd ipAddress;
        int networkNum = 0;
        int ctr = 0;
        ArrayList<Network> network = new ArrayList<Network>();

        System.out.println("Input IP Adress: ");
        ipAddress = new IpAdd(scanner.next());
        System.out.print("Input number of networks: ");
        networkNum = scanner.nextInt();

        while(ctr != networkNum){
            System.out.println("Input the name of network " + (ctr+1) + ": ");
            scanner.nextLine();
            String tempName = scanner.nextLine();
            System.out.println("Input the number of IP Adresses needed: ");
            Integer tempNum = scanner.nextInt();
            network.add(new Network(tempName, tempNum));

            ctr++;
        }

        ctr = 0;

        Collections.sort(network);

        System.out.println("Network Information");
        System.out.println("ID\tNetwork Name\tNetwork Address\tSubnet Mask\t Prefix Length");

        String networkAdd = ipAddress.getIP();
        
        while(ctr != networkNum){
            String subnetMask = "";
            String pref = "";
            int CIDR = 32 - log2(network.get(ctr).netNum + 2);

            subnetMask = calcSubnet(CIDR);
            pref = "/" + Integer.toString(CIDR);



            System.out.println((ctr+1) + "\t" + network.get(ctr).Name + "\t" + networkAdd + "\t" + subnetMask + "\t" + pref);

            networkAdd = calcNetwork(networkAdd, CIDR);
            ctr++;
        }

        ctr = 0;

        System.out.println("\n");
        System.out.println("Address Information");
        System.out.println("ID\tFirst Usable Addr\tLast Usable Addr\tUsable IPs\t Free IPs");

        networkAdd = ipAddress.getIP();

        while(ctr != networkNum){
            int CIDR = 32 - log2(network.get(ctr).netNum + 2);
            String firstUsable = calcFirst(networkAdd);
            String lastUsable = calcLast(networkAdd, CIDR);
            String broadCast = calcBroad(networkAdd, CIDR);

            System.out.println((ctr+1) + "\t" + firstUsable + "\t" + lastUsable + "\t" + broadCast);

            networkAdd = calcNetwork(networkAdd, CIDR);
            ctr++;
        }


    }

    public static void printMenu(){
        System.out.println("[1] Subnet Calculator");
        System.out.println("[2] Check Address Class");
        System.out.println("[3] Check Adress Type");
        System.out.println("[4] Exit");
    }

    public static int log2(int N) 
    { 
        int result = (int) Math.ceil(Math.log(N) / Math.log(2)); 
  
        return result; 
    } 

    public static String calcSubnet(int prefix){
        String result = "";
        String[] octets = new String[4];
        int ctr = prefix;

        for(int x = 0; x < 4; x++){
            octets[x] = "";
            for(int y = 0; y < 8; y++){
                if(ctr == 0){
                    octets[x] += '0';
                }else{
                    octets[x] += '1';
                    ctr--;
                }
            }
            int decimal = Integer.parseInt(octets[x], 2);
            octets[x] = Integer.toString(decimal) + ".";
        }

        octets[3] = octets[3].replace(".", "");

        for(int x = 0; x < 4; x++){
            result += octets[x];
        }
        
        return result;
    }

    public static String calcNetwork(String ipadd, int CIDR){
        String[] tempIP;
        int num;
        String numBinary;
        String finalIP;
        String [] tempFinal;
        String tempPlus = "";

        tempIP = ipadd.split("\\.");

        num = (int) Math.pow(2, 32 - CIDR);
        numBinary = Integer.toBinaryString(num);

        for(int x = 0; x < 4; x++){
            tempIP[x] = Integer.toBinaryString(Integer.parseInt(tempIP[x]));
            while(tempIP[x].length() != 8){
                tempIP[x] = 0 + tempIP[x];
            }
            tempPlus += tempIP[x];
        }

        finalIP = addBinary(tempPlus, numBinary);
        


        finalIP = insertString(finalIP, ".", 7);
        finalIP = insertString(finalIP, ".", 16);
        finalIP = insertString(finalIP, ".", 25);

        tempFinal = finalIP.split("\\.");
        finalIP = "";

        for(int x = 0; x < 4; x++){
            tempFinal[x] = Integer.toString(Integer.parseInt(tempFinal[x], 2));
            finalIP += tempFinal[x] + ".";
        }

        
        finalIP = finalIP.replaceFirst(".$","");

        return finalIP;

    }

    static String addBinary(String a, String b) 
    {  
        String result = "";  
          
        int s = 0;          

        int i = a.length() - 1, j = b.length() - 1; 
        while (i >= 0 || j >= 0 || s == 1) 
        { 
            s += ((i >= 0)? a.charAt(i) - '0': 0); 
            s += ((j >= 0)? b.charAt(j) - '0': 0); 

            result = (char)(s % 2 + '0') + result; 
  
            s /= 2; 

            i--; j--; 
        } 
          
    return result; 
    } 

    public static String insertString(String originalString, String stringToBeInserted, int index) 
    { 
        String newString = new String(); 
  
        for (int i = 0; i < originalString.length(); i++) { 
            newString += originalString.charAt(i); 
  
            if (i == index) { 
                newString += stringToBeInserted; 
            } 
        }

        return newString; 
    }
    
    public static String calcFirst(String ipadd){
        String[] octets = ipadd.split("\\.");
        String tempPlus = "";
        String result;
        String[] tempFinal;

        for(int x = 0; x < 4; x++){
            octets[x] = Integer.toBinaryString(Integer.parseInt(octets[x]));    
            while(octets[x].length() != 8){
                octets[x] = 0 + octets[x];
            }
            tempPlus += octets[x];        
            System.out.println(octets[x]);
        }
        System.out.println(tempPlus);
        
        result = addBinary(tempPlus, "1");

        
        result = insertString(result, ".", 7);
        result = insertString(result, ".", 16);
        result = insertString(result, ".", 25);

        tempFinal = result.split("\\.");
        result = "";

        for(int x = 0; x < 4; x++){
            tempFinal[x] = Integer.toString(Integer.parseInt(tempFinal[x], 2));
            result += tempFinal[x] + ".";
        }

        result = result.replaceFirst(".$","");

        return result;
    }

    public static String calcLast(String ipadd, int CIDR){
        String[] tempIP;
        int num;
        String numBinary;
        String finalIP;
        String [] tempFinal;
        String tempPlus = "";

        tempIP = ipadd.split("\\.");

        num = (int) Math.pow(2, 32 - CIDR);
        numBinary = Integer.toBinaryString(num - 2);

        for(int x = 0; x < 4; x++){
            tempIP[x] = Integer.toBinaryString(Integer.parseInt(tempIP[x]));
            while(tempIP[x].length() != 8){
                tempIP[x] = 0 + tempIP[x];
            }
            tempPlus += tempIP[x];
        }

        finalIP = addBinary(tempPlus, numBinary);
        


        finalIP = insertString(finalIP, ".", 7);
        finalIP = insertString(finalIP, ".", 16);
        finalIP = insertString(finalIP, ".", 25);

        tempFinal = finalIP.split("\\.");
        finalIP = "";

        for(int x = 0; x < 4; x++){
            tempFinal[x] = Integer.toString(Integer.parseInt(tempFinal[x], 2));
            finalIP += tempFinal[x] + ".";
        }

        
        finalIP = finalIP.replaceFirst(".$","");

        return finalIP;

    }

    public static String calcBroad(String ipadd, int CIDR){
        String[] tempIP;
        int num;
        String numBinary;
        String finalIP;
        String [] tempFinal;
        String tempPlus = "";

        tempIP = ipadd.split("\\.");

        num = (int) Math.pow(2, 32 - CIDR);
        numBinary = Integer.toBinaryString(num - 1);

        for(int x = 0; x < 4; x++){
            tempIP[x] = Integer.toBinaryString(Integer.parseInt(tempIP[x]));
            while(tempIP[x].length() != 8){
                tempIP[x] = 0 + tempIP[x];
            }
            tempPlus += tempIP[x];
        }

        finalIP = addBinary(tempPlus, numBinary);
        
        finalIP = insertString(finalIP, ".", 7);
        finalIP = insertString(finalIP, ".", 16);
        finalIP = insertString(finalIP, ".", 25);

        tempFinal = finalIP.split("\\.");
        finalIP = "";

        for(int x = 0; x < 4; x++){
            tempFinal[x] = Integer.toString(Integer.parseInt(tempFinal[x], 2));
            finalIP += tempFinal[x] + ".";
        }

        
        finalIP = finalIP.replaceFirst(".$","");

        return finalIP;

    }

}
