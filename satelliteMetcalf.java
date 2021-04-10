import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class satelliteMetcalf
{
    static double pi;
    static double c;
    static double R;
    static double s;
    static double[][] readData;
    static double[] xs;
    static double[] xv;
    static double ts;
    static double tv;
    static ArrayList<String> output;

    public static void main(String[] args) throws IOException
    {
        ts = 0.0d;
        xs = null;

        readData = new double[24][9];
        output = new ArrayList<>();
        String temp;
        Scanner scan = new Scanner(new URL("http://www.math.utah.edu/~pa/5600/tp/data.dat").openStream()).useDelimiter("\n");
        scan.useDelimiter("\n");
        String[] read = new String[220];
        int k = 0;
        while(scan.hasNext())
        {
            temp = scan.next();
            read[k] = temp;
            k++;
        }
        scan.close();
        pi = Double.parseDouble(read[0].substring(0, 27));
        c = Double.parseDouble(read[1].substring(0, 27));
        R = Double.parseDouble(read[2].substring(0, 27));
        s = Double.parseDouble(read[3].substring(0, 27));

        k = 4;
        for(int m = 0; m < 24; m++)
        {
            for(int i = 0; i < 9; i++)
            {
                readData[m][i] = Double.parseDouble(read[k].substring(0, 27));
                k++;
            }
        }

        BufferedReader In = new BufferedReader(new InputStreamReader(System.in));
        String vehicleData;
        while((vehicleData = In.readLine()) != null) {
            String[] split = vehicleData.split(" ");
            double[] xyz = cartesian(split);
            tv = Double.parseDouble(split[0]);
            xv = xyz;
            boolean[] Horizon = horizon(xyz);
            for(int i = 0; i < Horizon.length; i++)
            {
                if(Horizon[i])
                {
                    ts = getts(i);
                    xs = getxs(i, ts);
                    String ret = i+" "+ts+" "+xs[0]+" "+xs[1]+" "+xs[2];
                    System.out.println(ret);
                    output.add(ret);
                    if(ts>200 && ts<203)
                    {
                        System.out.println("Error");
                        output.add("Error");
                    }
                }
            }
        }

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("satellite.log"), StandardCharsets.UTF_8)))
        {
            for (String s : output) {
                writer.write(s + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    //Used to calculate via Newton's Method
    private static double newton(int v, double Tk, int depth)
    {
        double Output;
        Output = Tk-(gett(v,Tk)/gett1(v,Tk));
        if(Output-Tk < 0.01/c) { return Output; }
        else if(depth>=9) { return -1; }
        else { return newton(v, Output, depth+1); }
    }

    private static double getts(int v)
    {
        double t0 = tv;
        double norm = 0.0d;
        for(int i = 0; i < 3; i++)
        {
            norm += Math.pow(getxs(v,tv)[i],2);
        }
        t0 = t0 - Math.sqrt(norm)/c;

        return newton(v,t0,0);
    }

    private static double[] getxs(int m, double t)
    {
        double x = (R+readData[m][7])*(readData[m][0]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]) + readData[m][3]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8]));
        double y = (R+readData[m][7])*(readData[m][1]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]) + readData[m][4]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8]));
        double z = (R+readData[m][7])*(readData[m][2]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]) + readData[m][5]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8]));
        return new double[]{x,y,z,t};
    }
    private static double[] getxs1(int m, double t)
    {
        double x = 2*pi*(1/readData[m][6])*(R+readData[m][7])*(readData[m][3]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8])-readData[m][0]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]));
        double y = 2*pi*(1/readData[m][6])*(R+readData[m][7])*(readData[m][4]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8])-readData[m][1]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]));
        double z = 2*pi*(1/readData[m][6])*(R+readData[m][7])*(readData[m][5]*Math.sin((2*pi*t)/readData[m][6]+readData[m][8])-readData[m][2]*Math.cos((2*pi*t)/readData[m][6]+readData[m][8]));
        return new double[]{x,y,z,t};
    }
    private static double gett(int m, double t)
    {
        double Output = -1*c*c*(tv-t)*(tv-t);
        for(int i = 0; i<3; i++)
        {
            Output += (getxs(m,t)[i]-xv[i])*(getxs(m,t)[i]-xv[i]);
        }
        return Output;
    }
    private static double gett1(int v, double t)
    {
        double Output = 2*c*c*(tv-t);
        for(int i = 0; i<3; i++)
        {
            Output += 2*(getxs(v,t)[i]-xv[i])*getxs1(v,t)[i];
        }
        return Output;
    }

    private static boolean[] horizon(double[] a)
    {
        boolean[] Output = new boolean[24];
        double[] satelliteX;
        for(int i = 0; i < 24; i++)
        {
            satelliteX = getxs(i, a[3]);
            Output[i] = 2*a[0]*(satelliteX[0]-a[0]) + 2*a[1]*(satelliteX[1]-a[1]) + 2*a[2]*(satelliteX[2]-a[2]) > 0;
        }
        return Output;
    }

    private static double radians(String[] a)
    {
        return 2*pi*(Integer.parseInt(a[0])/360.0d + Integer.parseInt(a[1])/(360.0d*60) + Double.parseDouble(a[2])/(360*60*60));
    }

    private static double[] cartesian(String[] a)
    {
        double 	t 		= 	Double.parseDouble(a[0]);
        double 	theta 	= 	Integer.parseInt(a[4])*radians(new String[]{a[1],a[2],a[3]});
        double 	psi		= 	Integer.parseInt(a[8])*radians(new String[]{a[5],a[6],a[7]});
        double 	h 		= 	Double.parseDouble(a[9]);
        double 	x 		= 	(R + h) * Math.cos(theta) * Math.cos(psi);
        double 	y 		= 	(R + h) * Math.cos(theta) * Math.sin(psi);
        double 	z 		= 	(R + h) * Math.sin(theta);
        double  alpha   =   (2*pi*t)/s;

        return new double[]{Math.cos(alpha)*x-Math.sin(alpha)*y,Math.sin(alpha)*x+Math.cos(alpha)*y,z,t};
    }

}
