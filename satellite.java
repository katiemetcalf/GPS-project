import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class satellite {
    public satellite() {
    }

    public static void main(String[] var0) {
        try {
            String var1 = " ";
            double[][] var10 = new double[24][3];
            double[][] var11 = new double[24][3];
            double[] var12 = new double[24];
            double[] var13 = new double[24];
            double[] var14 = new double[24];
            BufferedReader var15 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data.dat"))));
            PrintStream var16 = new PrintStream(new FileOutputStream("satellite.log"));
            var16.println("Satellite log, Peter Alfeld\n\n data.dat:\n");
            String var17 = var15.readLine().trim() + " ";
            int var18 = var17.indexOf(var1);
            double var2 = Double.parseDouble(var17.substring(0, var18).trim());
            var16.println("pi = " + var2);
            var17 = var15.readLine().trim() + " ";
            var18 = var17.indexOf(var1);
            double var4 = Double.parseDouble(var17.substring(0, var18).trim());
            var16.println("c = " + var4);
            var17 = var15.readLine().trim() + " ";
            var18 = var17.indexOf(var1);
            double var6 = Double.parseDouble(var17.substring(0, var18).trim());
            var16.println("R = " + var6);
            var17 = var15.readLine().trim() + " ";
            var18 = var17.indexOf(var1);
            double var8 = Double.parseDouble(var17.substring(0, var18).trim());
            var16.println("s = " + var8);

            int var20;
            for(int var19 = 0; var19 < 24; ++var19) {
                for(var20 = 0; var20 < 3; ++var20) {
                    var17 = var15.readLine().trim() + " ";
                    var18 = var17.indexOf(var1);
                    var10[var19][var20] = Double.parseDouble(var17.substring(0, var18).trim());
                    var16.println("u[" + var19 + "][" + var20 + "]  = " + var10[var19][var20]);
                }

                for(var20 = 0; var20 < 3; ++var20) {
                    var17 = var15.readLine().trim() + " ";
                    var18 = var17.indexOf(var1);
                    var11[var19][var20] = Double.parseDouble(var17.substring(0, var18).trim());
                    var16.println("v[" + var19 + "][" + var20 + "]  = " + var11[var19][var20]);
                }

                var17 = var15.readLine().trim() + " ";
                var18 = var17.indexOf(var1);
                var12[var19] = Double.parseDouble(var17.substring(0, var18).trim());
                var16.println("period[" + var19 + "] = " + var12[var19]);
                var17 = var15.readLine().trim() + " ";
                var18 = var17.indexOf(var1);
                var14[var19] = Double.parseDouble(var17.substring(0, var18).trim());
                var16.println("altitude[" + var19 + "] = " + var14[var19]);
                var17 = var15.readLine().trim() + " ";
                var18 = var17.indexOf(var1);
                var13[var19] = Double.parseDouble(var17.substring(0, var18).trim());
                var16.println("phase[" + var19 + "] = " + var13[var19]);
            }

            var16.println("\n end data.dat \n");
            BufferedReader var82 = new BufferedReader(new InputStreamReader(System.in));
            var17 = "";
            var20 = -1;

            while(var17 != null) {
                ++var20;

                try {
                    var17 = var82.readLine().trim();
                    var17 = var17.replace(',', ' ');
                    var17 = var17.replace('\t', ' ');
                    var17 = var17.trim() + " ";
                    var16.println("\n --- epoch = " + var20);
                    var16.println("read " + var17);
                    var16.println("wrote:");
                    int var21 = var17.indexOf(var1);
                    double var22 = Double.parseDouble(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var24 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var25 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    double var26 = Double.parseDouble(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var28 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var29 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var30 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    double var31 = Double.parseDouble(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim();
                    var21 = var17.indexOf(var1);
                    int var33 = Integer.parseInt(var17.substring(0, var21).trim());
                    var17 = var17.substring(var21).trim() + var1;
                    var21 = var17.indexOf(var1);
                    double var34 = Double.parseDouble(var17.substring(0, var21).trim());
                    double var36 = var22;
                    double var38 = angles.rad(var29, var30, var31, var33);
                    double var40 = angles.rad(var24, var25, var26, var28);
                    double var42 = (var6 + var34) * cos(var40) * cos(var38);
                    double var44 = (var6 + var34) * cos(var40) * sin(var38);
                    double var46 = (var6 + var34) * sin(var40);
                    double var48 = 2.0D * var2 * var22 / var8;
                    double var50 = cos(var48) * var42 - sin(var48) * var44;
                    double var52 = sin(var48) * var42 + cos(var48) * var44;
                    double var54 = var46;

                    for(int var56 = 0; var56 < 24; ++var56) {
                        double var57 = (var6 + var14[var56]) * (var10[var56][0] * cos(2.0D * var2 * var36 / var12[var56] + var13[var56]) + var11[var56][0] * sin(2.0D * var2 * var36 / var12[var56] + var13[var56]));
                        double var59 = (var6 + var14[var56]) * (var10[var56][1] * cos(2.0D * var2 * var36 / var12[var56] + var13[var56]) + var11[var56][1] * sin(2.0D * var2 * var36 / var12[var56] + var13[var56]));
                        double var61 = (var6 + var14[var56]) * (var10[var56][2] * cos(2.0D * var2 * var36 / var12[var56] + var13[var56]) + var11[var56][2] * sin(2.0D * var2 * var36 / var12[var56] + var13[var56]));
                        double var63 = sqrt((var50 - var57) * (var50 - var57) + (var52 - var59) * (var52 - var59) + (var54 - var61) * (var54 - var61));
                        double var65 = var63 / var4;
                        int var67 = 0;
                        double var68 = var36 - var65;
                        double var70 = 1.0D;
                        double var72 = 0.01D / var4;

                        double var74;
                        double var76;
                        while(var70 > var72) {
                            ++var67;
                            var57 = (var6 + var14[var56]) * (var10[var56][0] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][0] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            var59 = (var6 + var14[var56]) * (var10[var56][1] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][1] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            var61 = (var6 + var14[var56]) * (var10[var56][2] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][2] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            var74 = (var50 - var57) * (var50 - var57) + (var52 - var59) * (var52 - var59) + (var54 - var61) * (var54 - var61);
                            var74 -= var65 * var4 * var65 * var4;
                            var76 = 2.0D * var4 * var4 * var65;
                            var76 += 2.0D * (var57 - var50) * 2.0D * var2 / var12[var56] * (var6 + var14[var56]) * (-var10[var56][0] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][0] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            var76 += 2.0D * (var59 - var52) * 2.0D * var2 / var12[var56] * (var6 + var14[var56]) * (-var10[var56][1] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][1] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            var76 += 2.0D * (var61 - var54) * 2.0D * var2 / var12[var56] * (var6 + var14[var56]) * (-var10[var56][2] * sin(2.0D * var2 * var68 / var12[var56] + var13[var56]) + var11[var56][2] * cos(2.0D * var2 * var68 / var12[var56] + var13[var56]));
                            double var78 = var68;
                            var68 -= var74 / var76;
                            var65 = var36 - var68;
                            var70 = abs(var68 - var78);
                            if (var67 > 10) {
                                var70 = 0.0D;
                                write("no convergence in satellite location");
                            }
                        }

                        var74 = var50 * var57 + var52 * var59 + var54 * var61;
                        var76 = var50 * var50 + var52 * var52 + var54 * var54;
                        if (var76 < var74) {
                            String var83 = var56 + " " + var68 + " " + var57 + " " + var59 + " " + var61;
                            write(var83);
                            var16.println(var83);
                        }
                    }
                } catch (Exception var80) {
                    var17 = null;
                }
            }

            var16.close();
        } catch (Exception var81) {
            write("" + var81);
            var81.printStackTrace();
        }

    }

    static double cos(double var0) {
        return Math.cos(var0);
    }

    static double sin(double var0) {
        return Math.sin(var0);
    }

    static double sqrt(double var0) {
        return Math.sqrt(var0);
    }

    static double abs(double var0) {
        return Math.abs(var0);
    }

    static void write(String var0) {
        System.out.println(var0);
    }

    static void debug(String var0) {
        System.out.println(var0);
    }
}
