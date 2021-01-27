import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class App {
    public static String des;
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        int mode = 0;

        
        while(mode != 4){
            printMenu();
            mode = scanner.nextInt();

            switch(mode){
                case 1: subnetCalc();
                        break;

                case 2: checkClass();
                        break;

                case 3: checkType();
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
            System.out.print("Input the name of network " + (ctr+1) + ": ");
            scanner.nextLine();
            String tempName = scanner.nextLine();
            System.out.print("Input the number of IP Adresses needed: ");
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
        System.out.println("ID\tFirst Usable Addr\tLast Usable Addr\tBroadcast Address\tUsable IPs\t Free IPs");

        networkAdd = ipAddress.getIP();

        while(ctr != networkNum){
            int CIDR = 32 - log2(network.get(ctr).netNum + 2);
            String firstUsable = calcFirst(networkAdd);
            String lastUsable = calcLast(networkAdd, CIDR);
            String broadCast = calcBroad(networkAdd, CIDR);
            int usable = ((int) Math.pow(2, log2(network.get(ctr).netNum + 2))) - 2;
            int freeIPs = usable - network.get(ctr).netNum;


            System.out.println((ctr+1) + "\t" + firstUsable + "\t" + lastUsable + "\t" + broadCast + "\t" + usable + "\t" + freeIPs);

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
        }
        
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

    public static void checkClass(){
        System.out.print("Input IP Address: ");
        String ip_add = scanner.next();
        String[] new_add = ip_add.split("\\."); 

        int first = Integer.parseInt(new_add[0]); 
        int second = Integer.parseInt(new_add[1]); 
        int third = Integer.parseInt(new_add[2]);
        int fourth = Integer.parseInt(new_add[3]);

        des = "";

        if((first < 0 || first > 255) || (second < 0 && second > 255) || (third < 0 && third > 255) || (fourth < 0 && fourth > 255)){
            System.out.println("Error: Invalid IP Address.");
        }
        else if(SpecialIpAddress(first, second, third, fourth)){
            System.out.println("The IP Address " + ip_add + " is a Special Purpose Address. " + des);
        }
        else if(first >= 0 && first <= 127){
            System.out.println("The IP Address " + ip_add + " is a Class A Address, whose network address is " + first + ".0.0.0/8");
        }
        else if(first >= 128 && first <= 191){
            System.out.println("The IP Address " + ip_add + " is a Class B Address, whose network address is " + first + "." + second + ".0.0/16");
        }
        else if(first >= 192 && first <= 223){
            System.out.println("The IP Address " + ip_add + " is a Class C Address, whose network address is " + first + "." + second + "." + third + ".0/24");
        }
    }

    public static boolean SpecialIpAddress(int first, int second, int third, int fourth){
        if(first == 0){
            des = "Current network only valid as source address. Address Block = 0.0.0.0/8";
            return true;
        }
        else if(first == 100 && second >= 64 && second <= 127){
            des = "Shared address space for communications between a service provider and its subscribers when using a carrier-grade NAT. Address Block = 100.64.0.0/10";
            return true;
        }   
        else if(first == 127){
            des = "Used for loopback addresses to the local host. Address Block = 127.0.0.0/8";
            return true;
        }
        else if(first == 169 && second == 254){
            des = "Used for link-local addresses between two hosts on a single link when no IP address is otherwise specified, such as would have normally been retrieved from a DHCP server. Address Block = 169.254.0.0/16";
            return true;
        }
        else if(first == 192 && second == 0 && third == 0){
            des = "IETF Protocol Assignments. Address Block = 192.0.0.0/24";
            return true;
        }
        else if(first == 192 && second == 0 && third == 2){
            des = "Assigned as TEST-NET-1, documentation and examples. Address Block = 192.0.2.0/24";
            return true;
        }
        else if(first == 192 && second == 88 && third == 99){
            des = "Reserved. Formerly used for IPv6 to IPv4 relay. Address Block = 192.88.99.0/24";
            return true;
        }
        else if(first == 198 && (second == 18 || second == 19)){
            des = "Used for benchmark testing of inter-network communications between two separate subnets. Address Block = 198.18.0.0/15";
            return true;
        }
        else if(first == 198 && second == 51 && third == 100){
            des = "Assigned as TEST-NET-2, documentation and examples. Address Block = 198.51.100.0/24";
            return true;
        }
        else if(first == 203 && second == 0 && third == 113){
            des = " Assigned as TEST-NET-3, documentation and examples. Address Block = 203.0.113.0/24";
            return true;
        }
        else if(first >= 224 && first <= 239){
            des = "In use for IP multicast. (Former Class D network). Address Block = 224.0.0.0/4";
            return true;
        }
        else if(first == 255 && first == second && second == third && third == fourth){
            des = "Reserved for future use. (Former Class E network). Address Block = 240.0.0.0/4";
            return true;
        }
        else if(first >= 240 && first <= 255){
            des = "Reserved for the limited broadcast destination address. Address Block = 255.255.255.255/32";
            return true;
        }

        return false;
    }
    
    public static void checkType(){
        System.out.print("Input IP Address: ");
        String ipadd = scanner.next();
        String[] new_add = ipadd.split("/");
        String[] address = new_add[0].split("\\."); 

        int second = Integer.parseInt(address[1]); 
        int third = Integer.parseInt(address[2]);
        int fourth = Integer.parseInt(address[3]);
        int fifth = Integer.parseInt(new_add[1]);

        int newfifth = 0;

        if(fifth >= 8 && fifth <= 15){
            newfifth = (int) Math. pow(2,(16 - fifth));

            if(fourth != 255){
                if(fourth % newfifth == 0){
                    System.out.println("The IP Address " + ipadd + " is a Network Address");
                }
                else if(fourth % (newfifth-1) == 0){
                    System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
                }
                else{
                    System.out.println("The IP Address " + ipadd + " is a Host Address");
                }
            }
            else if(third != 255){
                if(third % newfifth == 0){
                    System.out.println("The IP Address " + ipadd + " is a Network Address");
                }
                else if(third % (newfifth-1) == 0){
                    System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
                }
                else{
                    System.out.println("The IP Address " + ipadd + " is a Host Address");
                }
            }
            else{
                if(second % newfifth == 0){
                    System.out.println("The IP Address " + ipadd + " is a Network Address");
                }
                else if(second % (newfifth-1) == 0){
                    System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
                }
                else{
                    System.out.println("The IP Address " + ipadd + " is a Host Address");
                }
            }
        }
        else if(fifth >= 16 && fifth <= 23){
            newfifth = (int) Math. pow(2,(24 - fifth));

            if(fourth != 255){
                if(fourth % newfifth == 0){
                    System.out.println("The IP Address " + ipadd + " is a Network Address");
                }
                else if(fourth % (newfifth-1) == 0){
                    System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
                }
                else{
                    System.out.println("The IP Address " + ipadd + " is a Host Address");
                }
            }
            else{
                if(third % newfifth == 0){
                    System.out.println("The IP Address " + ipadd + " is a Network Address");
                }
                else if(third % (newfifth-1) == 0){
                    System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
                }
                else{
                    System.out.println("The IP Address " + ipadd + " is a Host Address");
                }
            }
        }
        else{
            newfifth = (int) Math. pow(2,(32 - fifth));

            if(fourth % newfifth == 0){
                System.out.println("The IP Address " + ipadd + " is a Network Address");
            }
            else if(fourth % (newfifth-1) == 0){
                System.out.println("The IP Address " + ipadd + " is a Broadcast Address");
            }
            else{
                System.out.println("The IP Address " + ipadd + " is a Host Address");
            }
        }
    }

}
