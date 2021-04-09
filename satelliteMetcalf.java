import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class satelliteMetcalf {
        public satelliteMetcalf(){

        }
        public static void main(String[] args) {
            Computations.getData();

        }
}

class Computations {
        // Variables needed to get data from vehicle
        public static double tv = 0.0;
        public static double psiD = 0.0;
        public static double psiM = 0.0;
        public static double psiS = 0.0;
        public static double psi = 0.0;
        public static int NS = 0;
        public static double lambdaD = 0.0;
        public static double lambdaM = 0.0;
        public static double lambdaS = 0.0;
        public static double lambda = 0.0;
        public static int EW = 0;
        public static double h = 0.0;
        public static double[][] R3;
        public static double[][] X;
        public static double[][] a, b, e, f;
        public static double x;
        public static double y;
        public static double z;
        public static double alpha;
        public static double C, D, g, k;
        public static double[][] Xvehicle;
        public static double pi, c, R, s;
        public static double[] u1, u2, u3, v1, v2, v3, periodicity, altitude, phase;
        public static double[] xstv;
        public static double[] ystv;
        public static double[] zstv;
        public static double[] xst1;
        public static double[] yst1;
        public static double[] zst1;
        public static double[] xst0;
        public static double[] yst0;
        public static double[] zst0;
        public static double[] t0;
        public static double[] t1;
        public static double[] dft0;
        public static double[] ft0;
        public static double[] dft1;
        public static double[] ft1;
        public static double[] t2;
        public static double[] xsts;
        public static double[] ysts;
        public static double[] zsts;

        static void getData() {
            u1 = new double[24];
            u2 = new double[24];
            u3 = new double[24];
            v1 = new double[24];
            v2 = new double[24];
            v3 = new double[24];
            periodicity = new double[24];
            altitude = new double[24];
            phase = new double[24];

            Scanner dataInput;
            try {
                dataInput = new Scanner(new URL("http://www.math.utah.edu/~pa/5600/tp/data.dat").openStream());
                ArrayList<Double> listOfData = new ArrayList<>();
                while (dataInput.hasNextLine()) {
                    String[] lineData = dataInput.nextLine().trim().split(" ");
                    String data = lineData[0].trim();
                    listOfData.add(Double.parseDouble(data));
                }
                pi = listOfData.get(0);
                c = listOfData.get(1);
                R = listOfData.get(2);
                s = listOfData.get(3);

                int j = 0;
                for(int i = 0; i < 24; i++) {
                    u1[i] = listOfData.get(4+j);
                    u2[i] = listOfData.get(5+j);
                    u3[i] = listOfData.get(6+j);
                    v1[i] = listOfData.get(7+j);
                    v2[i] = listOfData.get(8+j);
                    v3[i] = listOfData.get(9+j);
                    periodicity[i] = listOfData.get(10+j);
                    altitude[i] = listOfData.get(11+j);
                    phase[i] = listOfData.get(12+j);
                    j += 9;
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Use this to get data from vehicle
            BufferedReader vehicleInput;


            try {
                vehicleInput = new BufferedReader(new InputStreamReader(System.in));
                String[] vehicleData = vehicleInput.readLine().split("\\s+");
                tv = Double.parseDouble(vehicleData[0]);
                psiD = Double.parseDouble(vehicleData[1]);
                psiM = Double.parseDouble(vehicleData[2]);
                psiS = Double.parseDouble(vehicleData[3]);
                NS = Integer.parseInt(vehicleData[4]);
                lambdaD = Double.parseDouble(vehicleData[5]);
                lambdaM = Double.parseDouble(vehicleData[6]);
                lambdaS =Double.parseDouble (vehicleData[7]);
                EW = Integer.parseInt(vehicleData[8]);
                h = Double.parseDouble(vehicleData[9]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Find lambda and psi
            psi = 2 * pi * (NS) * ((psiD / 360) + (psiM / (360 * 60)) + (psiS / (360 * 60 * 60)));
            lambda = 2 * pi * (EW) * ((lambdaD / 360) + (lambdaM / (360 * 60)) + (lambdaS / (360 * 60 * 60)));


            //Fill in R3
            alpha = 2 * pi * tv / s;
            R3 = new double[3][3];
            R3[0][0] = Math.cos(alpha);
            R3[1][0] = Math.sin(alpha);
            R3[2][0] = 0.0;
            R3[0][1] = -Math.sin(alpha);
            R3[1][1] = Math.cos(alpha);
            R3[2][1] = 0.0;
            R3[0][2] = 0.0;
            R3[1][2] = 0.0;
            R3[2][2] = 1.0;

            //Fill in matrix X
            X = new double[3][1];
            x = (R + h) * Math.cos(psi) * Math.cos(lambda);
            y = (R + h) * Math.cos(psi) * Math.sin(lambda);
            z = (R + h) * Math.sin(psi);
            X[0][0] = x;
            X[1][0] = y;
            X[2][0] = z;


            //Calculate vehicle position
            Xvehicle = new double [3][1];
            Xvehicle[0][0] = R3[0][0] * X[0][0] + R3[0][1] * X[1][0] + R3[0][2] * X[2][0];
            Xvehicle[1][0] = R3[1][0] * X[0][0] + R3[1][1] * X[1][0] + R3[1][2] * X[2][0];
            Xvehicle[2][0] = R3[2][0] * X[0][0] + R3[2][1] * X[1][0] + R3[2][2] * X[2][0];


            /*
            Newton's Method
             */

            for (int i=0; i < 24; i++) {
                //Position of each satellite at tv

                xstv= new double[24];
                xstv[i]= (R + altitude[i]) * (u1[i] * Math.cos(2 * pi * tv / periodicity[i] + phase[i]) + v1[i] * Math.sin(2 * pi * tv / periodicity[i] + phase[i]));
                ystv= new double[24];
                ystv[i] = (R + altitude[i]) * (u2[i] * Math.cos(2 * pi * tv / periodicity[i] + phase[i]) + v2[i] * Math.sin(2 * pi * tv / periodicity[i] + phase[i]));
                zstv= new double[24];
                zstv[i] = (R + altitude[i]) * (u3[i] * Math.cos(2 * pi * tv / periodicity[i] + phase[i]) + v3[i] * Math.sin(2 * pi * tv / periodicity[i] + phase[i]));


                //t0 for each satellite
                t0= new double[24];
                t0[i] = tv - (Math.sqrt(Math.pow((xstv[i] - Xvehicle[0][0]), 2) + Math.pow((ystv[i] - Xvehicle[1][0]), 2) + Math.pow((zstv[i] - Xvehicle[2][0]), 2)) / c);

                //Position of each satellite at t_0
                xst0= new double[24];
                xst0[i] = (R + altitude[i]) * (u1[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]) + v1[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]));
                yst0= new double[24];
                yst0[i] = (R + altitude[i]) * (u2[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]) + v2[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]));
                zst0= new double[24];
                zst0[i] = (R + altitude[i]) * (u3[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]) + v3[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]));

                //f(t_0), df(t_0) for each satellite
                a=new double[1][3];
                a[0][0] = xst0[i] - Xvehicle[0][0];
                a[0][1] = yst0[i] - Xvehicle[1][0];
                a[0][2] = zst0[i] - Xvehicle[2][0];
                b=new double[3][1];
                b[0][0] = -u1[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]) + v1[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]);
                b[1][0] = -u2[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]) + v2[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]);
                b[2][0] = -u3[i] * Math.sin(2 * pi * t0[i] / periodicity[i] + phase[i]) + v3[i] * Math.cos(2 * pi * t0[i] / periodicity[i] + phase[i]);


                C = a[0][0] * b[0][0] + a[0][1] * b[1][0] + a[0][2] * b[2][0];
                D = a[0][0] * a[0][0] + a[0][1] * a[0][1] + a[0][2] * a[0][2];

                dft0= new double[24];
                dft0[i] = (((4 * pi * (R + h)) / periodicity[i]) * (C) + (2 * C * C * (tv - t0[i])));
                ft0= new double[24];
                ft0[i] = (D) - (C * C * (tv - t0[i]) * (tv - t0[i]));


                //t_1 for each satellite
                t1= new double[24];
                t1[i] = t0[i] - ft0[i] / dft0[i];

                //Position of each satellite at t_1
                xst1= new double[24];
                xst1[i] = (R + altitude[i]) * ((u1[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i])) + (v1[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i])));
                yst1= new double[24];
                yst1[i] = (R + altitude[i]) * ((u2[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i])) + (v2[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i])));
                zst1= new double[24];
                zst1[i] = (R + altitude[i]) * ((u3[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i])) + (v3[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i])));
                //f(t_1) and df(t_1) for each satellite
                e=new double[1][3];
                e[0][0] = xst1[i] - Xvehicle[0][0];
                e[0][1] = yst1[i] - Xvehicle[1][0];
                e[0][2] = zst1[i] - Xvehicle[2][0];
                f=new double[3][1];
                f[0][0] = -u1[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i]) + v1[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i]);
                f[1][0] = -u2[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i]) + v2[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i]);
                f[2][0] = -u3[i] * Math.sin(2 * pi * t1[i] / periodicity[i] + phase[i]) + v3[i] * Math.cos(2 * pi * t1[i] / periodicity[i] + phase[i]);


                g = e[0][0] * f[0][0] + e[0][1] * f[1][0] + e[0][2] * f[2][0];
                k = e[0][0] * e[0][0] + e[0][1] * e[0][1] + e[0][2] * e[0][2];

                dft1= new double[24];
                dft1[i] = ((4 * pi * R + h) / periodicity[i]) * (g) + 2 * c * c * (tv - t1[i]);
                ft1= new double[24];
                ft1[i] = ((k) - c * c * (tv - t1[i]) * (tv - t1[i]));


                //t_2 for each satellite (ts)
                t2= new double[24];
                t2[i] = t1[i] - ft1[i] / dft1[i];

                //Position of each satellite at time t_2 (xs(ts))
                xsts= new double[24];
                xsts[i] = (R + altitude[i]) * (u1[i] * Math.cos(2 * pi * t2[i] / periodicity[i] + phase[i]) + v1[i] * Math.sin(2 * pi * t2[i] / periodicity[i] + phase[i]));
                ysts= new double[24];
                ysts[i] = (R + altitude[i]) * (u2[i] * Math.cos(2 * pi * t2[i] / periodicity[i] + phase[i]) + v2[i] * Math.sin(2 * pi * t2[i] / periodicity[i] + phase[i]));
                zsts= new double[24];
                zsts[i] = (R + altitude[i]) * (u3[i] * Math.cos(2 * pi * t2[i] / periodicity[i] + phase[i]) + v3[i] * Math.sin(2 * pi * t2[i] / periodicity[i] + phase[i]));


                //Determines if the satellite is above the surface of the earth
                double u;
                double l;
                u = Xvehicle[0][0] * xsts[i] + Xvehicle[1][0] * ysts[i] + Xvehicle[2][0] * zsts[i];
                l = Xvehicle[0][0] * Xvehicle[0][0] + Xvehicle[1][0] * Xvehicle[1][0] + Xvehicle[2][0] * Xvehicle[2][0];
                //This prints output going to receiver
                if (u > l) {
                    System.out.println(i + " " + t2[i] + " " + xsts[i] + " " + ysts[i] + " " + zsts[i]);
                }
            } //End of Newton's Method

            File log = new File("satellite.log");
            if (!log.exists()) {
                try {
                    log.createNewFile();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }

            try {
                PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(log, true)));

                for (int i = 0; i < altitude.length; i++) {
                    out.println(i + " " + t2[i] + " " + xsts[i] + " " + ysts[i] + " " + zsts[i]);
                } out.close();
            } catch (FileNotFoundException event) {
                event.printStackTrace();
            }
        }
    }