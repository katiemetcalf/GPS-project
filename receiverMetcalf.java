import java.net.URL;
import java.util.ArrayList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Scanner;


public class receiverMetcalf {
    static String[] input;
    static double[][] data;
    static double[] sk;
    static double[] x0;
    static double[] xv;
    static double tv;
    static int I = 0;
    static int M;
    static double pi;
    static double c;
    static double R;
    static double s;
    static ArrayList<String> value;
    static ArrayList<String> output;
    static NumberFormat formatDecimals;

    public static void main(String[] args) throws IOException
    {
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

        value = new ArrayList<>();
        formatDecimals = new DecimalFormat("#0.00");
        output = new ArrayList<>();
        BufferedReader In = new BufferedReader(new InputStreamReader(System.in));
        String lines;
        while((lines = In.readLine()) != null)
        {
            Collections.addAll(value, lines.split(" "));
        }
        int cap = value.size();
        input = new String[cap];
        for(int i = 0; i < cap; i++) { input[i] = value.get(i); }
        In.close();
        x0 = new double[]{ 0, 0, 0 };
        while(I<input.length)
        {
            data = getData();
            xv = newton(x0, 0);
            assert xv != null;
            tv = b(xv);
            String[] returned = position(new String[]{""+tv, ""+xv[0], ""+xv[1], ""+xv[2]});
            String ret = "";
            for(int i = 0; i<10; i++)
            {
                ret = ret + formatDecimals.format(Double.parseDouble(returned[i])) + " ";
            }
            System.out.println(ret);
            output.add(ret);
        }

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("receiver.log"), StandardCharsets.UTF_8)))
        {
            for (String s : output) {
                writer.write(s + "\n");
            }
        }
        catch(IOException e) { System.out.println("Error"); }
    }


    static double[][] getData()
    {
        M=1;
        while(I+1+5*M < input.length && Math.abs(Double.parseDouble(input[I+1+5*(M)])-Double.parseDouble(input[I+1+5*(M-1)]))<0.5d) { M++; }
        double[][] returned = new double[M][4];
        // while the current first input is greater than the previous first input
        for(int j = 0; j < M; j++)
        {
            returned[j][0] = Double.parseDouble(input[I + 1]);
            returned[j][1] = Double.parseDouble(input[I + 2]);
            returned[j][2] = Double.parseDouble(input[I + 3]);
            returned[j][3] = Double.parseDouble(input[I + 4]);
            I = I + 5;
        }
        return returned;
    }

    //Calculate position
    private static String[] position(String[] a)
    {
        double t = Double.parseDouble(a[0]);
        double x1 = Double.parseDouble(a[1]);
        double y1 = Double.parseDouble(a[2]);
        double z1 = Double.parseDouble(a[3]);

        double[] xyz = R3(-2*pi*t/s, new double[]{x1,y1,z1});
        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        double psi;
        if(x*x+y*y == 0) {
            if(z>=0) { psi = pi/2;	}
            else { psi = -1*pi/2;	}
        }
        else { psi = Math.atan2(z,Math.sqrt(x*x+y*y));
        }
        double lambda;
        if(x>0 && y>0) { lambda = Math.atan2(y,x); }
        else if(x < 0) { lambda = pi + Math.atan2(y,x); }
        else { lambda = 2*pi + Math.atan2(y,x); }
        lambda-=pi;

        String[] Psi = radians(psi);
        String[] Lambda = radians(lambda);

        double h = Math.sqrt(x*x + y*y + z*z) - R;

        return new String[]{""+t, Psi[0], Psi[1], Psi[2], Psi[3], Lambda[0], Lambda[1], Lambda[2], Lambda[3], ""+h};
    }
    private static double[] R3(double alpha, double[] x)
    {
        return new double[] { Math.cos(alpha)*x[0] - Math.sin(alpha)*x[1], Math.sin(alpha)*x[0] + Math.cos(alpha)*x[1], x[2] };
    }
    private static String[] radians(double a)
    {
        double 	b  		= 	a*180/pi;
        if(a<0) {
            b 		= 	-1*b;
        }
        int 	d 		= 	((int) Math.floor(b));
        int 	m		= 	((int) Math.floor(60*(b-d)));
        double 	s 		= 	60*(60*(b-d)-m);
        if(a<0)	{ return new String[]{""+d,""+m,""+s, ""+(-1)};	}
        return new String[]{""+d,""+m,""+s, ""+1};
    }


    //Uses Newton's Method and solver to solve the jacobian
    static double[] newton(double[] x, int d)
    {
        sk = solver(jacobian(x), gradient(x));
        double[] returned = x.clone();
        for(int i = 0; i<3; i++) { returned[i] -= sk[i]; }
        if(Math.abs(minus(returned,x)[0])<0.0000001d && Math.abs(minus(returned,x)[1])<0.0000001d && Math.abs(minus(returned,x)[2])<0.0000001d) { return returned; }
        else if(d>9) { return null; }
        else
        {
            d++;
            return newton(returned, d);
        }
    }
    static double[] solver(double[][] A, double[] b)
    {
        double x = (A[0][1]*A[1][2]*b[2] - A[0][1]*A[2][2]*b[1] - A[0][2]*A[1][1]*b[2] + A[0][2]*A[2][1]*b[1] + A[1][1]*A[2][2]*b[0] - A[1][2]*A[2][1]*b[0])/(A[0][0]*A[1][1]*A[2][2] - A[0][0]*A[1][2]*A[2][1] - A[0][1]*A[1][0]*A[2][2] + A[0][1]*A[1][2]*A[2][0] + A[0][2]*A[1][0]*A[2][1] - A[0][2]*A[1][1]*A[2][0]);
        double y = -1 * (A[0][0]*A[1][2]*b[2] - A[0][0]*A[2][2]*b[1] - A[0][2]*A[1][0]*b[2] + A[0][2]*A[2][0]*b[1] + A[1][0]*A[2][2]*b[0] - A[1][2]*A[2][0]*b[0])/(A[0][0]*A[1][1]*A[2][2] - A[0][0]*A[1][2]*A[2][1] - A[0][1]*A[1][0]*A[2][2] + A[0][1]*A[1][2]*A[2][0] + A[0][2]*A[1][0]*A[2][1] - A[0][2]*A[1][1]*A[2][0]);
        double z = (A[0][0]*A[1][1]*b[2] - A[0][0]*A[2][1]*b[1] - A[0][1]*A[1][0]*b[2] + A[0][1]*A[2][0]*b[1] + A[1][0]*A[2][1]*b[0] - A[1][1]*A[2][0]*b[0])/(A[0][0]*A[1][1]*A[2][2] - A[0][0]*A[1][2]*A[2][1] - A[0][1]*A[1][0]*A[2][2] + A[0][1]*A[1][2]*A[2][0] + A[0][2]*A[1][0]*A[2][1] - A[0][2]*A[1][1]*A[2][0]);
        return new double[]{x,y,z};
    }
    static double b(double[] Xv)
    {
        double returned = data[0][0] + (1/c) * Math.sqrt(Math.pow(Xv[0]-data[0][1], 2)+Math.pow(Xv[1]-data[0][2], 2)+Math.pow(Xv[2]-data[0][3], 2));
        return returned;
    }
    static double[][] jacobian(double[] x)
    {
        double[][] returned = new double[3][3];
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                returned[i][j] = J_ij(i, j, x);
            }
        }
        return returned;
    }
    static double J_ij(int i, int j, double[] x)
    {
        double sum = 0.0d;
        for(int k = 0; k<M-1; k++)
        {
            sum += gradientAtPoint(k, i, x) * gradientAtPoint(k, j, x);
        }
        return 2.0d * sum;
    }
    static double gradientAtPoint(int i, int j, double[] x)
    {
        double returned = 0.5d * (2.0d * x[j] - 2.0d * data[i][j+1]) / (Math.sqrt(Math.pow((x[0]-data[i][1]),2) + Math.pow((x[1]-data[i][2]),2) + Math.pow((x[2]-data[i][3]),2)));
        returned -= 0.5d * (2.0d * x[j] - 2.0d * data[i+1][j+1]) / (Math.sqrt(Math.pow((x[0]-data[i+1][1]),2) + Math.pow((x[1]-data[i+1][2]),2) + Math.pow((x[2]-data[i+1][3]),2)));
        return returned;
    }
    static double[] gradient(double[] x)
    {
        double[][] minuss = new double[M][3];
        for(int j = 0; j<M; j++)
        {
            minuss[j] = minus(new double[]{data[j][1],data[j][2],data[j][3]},x);
        }

        double[] N = new double[M];
        for(int j = 0; j<M; j++)
        {
            N[j] = norm(minuss[j]);
        }

        double[] A = new double[M-1];
        for(int j = 0; j<M-1; j++)
        {
            A[j] = N[j+1]-N[j]-c*(data[j][0]-data[j+1][0]);
        }
        double[][] XYZ = new double[3][M-1];
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<M-1; j++)
            {
                XYZ[i][j] = minuss[j][i]/N[j] - minuss[j+1][i]/N[j+1];
            }
        }
        double[] returned = new double[3];
        for(int j = 0; j<3; j++)
        {
            returned[j] = 0.0d;
            for(int i = 0; i<M-1; i++) { returned[j] += A[i]*XYZ[j][i]; }
            returned[j] = 2.0d*returned[j];
        }
        return returned;
    }
    static double norm(double[] a)
    {
        double sum = 0.0d;
        for(Double d : a){ sum = sum + d*d; }
        return Math.sqrt(sum);
    }
    static double[] minus(double[] a, double[] b)
    {
        if(a.length==b.length)
        {
            double[] returned = new double[a.length];
            for(int i = 0; i<a.length; i++) { returned[i]=a[i]-b[i]; }
            return returned;
        }
        return null;
    }
}
