package pl.coderslab.workshopWeek2;

import java.util.Arrays;

public class sad {
    public static void main(String[] args) {
        System.out.println(sortDesc(877654321));
    }
    public static int sortDesc(final int num) {
        String intnum = Integer.toString(num);
        int finint = 0;
        System.out.println(intnum);
        int[] numtab = new int[intnum.length()];
        int[] numtab2 = new int[intnum.length()];
        for (int i = 0; i < numtab.length; i++) {
            numtab[i]=Integer.parseInt(intnum.substring(i,i+1));
        }
        Arrays.sort(numtab);
        intnum = "";
        for (int i = 0; i < numtab.length; i++) {
            intnum = intnum + numtab[i];
        }
        finint = Integer.parseInt(intnum);
        return finint;
    }
}
