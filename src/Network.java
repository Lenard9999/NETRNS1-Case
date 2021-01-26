public class Network implements Comparable<Network> {
    
    public String Name;
    public int netNum;

    public Network(){
    }

    public Network(String name, int num){
        Name = name;
        netNum = num;
    }

    @Override
    public int compareTo(Network comparenet) {
        int comparenum =((Network)comparenet).netNum;

        return comparenum-this.netNum;
    }


}
