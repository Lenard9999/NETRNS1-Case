public class IpAdd {
    String[] octets;
    private String ipadd;
    private String pref[];

    public IpAdd(){}

    public IpAdd(String ipadd){
        pref = ipadd.split("\\/");
        octets = pref[0].split("\\.");
        octets[3] = octets[3].replaceFirst("./$", "replacement");
        this.ipadd = ipadd;
    }

    public String getIP(){
        String[] temp;

        temp = ipadd.split("\\/");
        return temp[0];
    }
}
