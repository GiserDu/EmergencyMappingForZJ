package com.zz.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/1.
 */
public class ClassModles {
    /**
     * 界限等分模型
     * @param minValue
     * @param maxValue
     * @param breakNum
     * @return
     */
    public static double[] modelA1(double minValue, double maxValue, int breakNum) {
//        boolean flag = JUtil.IsIntegerOnly(a);//判断是否需要为整数
        double interval = (maxValue-minValue)/breakNum;
        int intervalNum = breakNum-1;
        double[] classInterval = new double[intervalNum];
        for (int i=0;i<intervalNum;i++){
//            classInterval[i] = minValue + interval*(i+1);
            BigDecimal b = new BigDecimal(minValue + interval*(i+1));
            double df = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            classInterval[i] = df;
        }
        return classInterval;
    }

    /**
     * 间隔等分模型
     * @param minValue
     * @param maxValue
     * @param breakNum
     * @return
     */
    public static double[] modelB1(double minValue, double maxValue, int breakNum) {
//        boolean flag = JUtil.IsIntegerOnly(a);
//        double[] b = new double[num + 1];
//        b[0] = JStatistics.MinNoneNegative(a);
//        b[num] = JStatistics.Max(a);
        int intervalNum = breakNum-1;
        double[] classInterval = new double[intervalNum];
        double dif = (maxValue - minValue) / (breakNum * (breakNum + 1));
        for (int i = 0; i <intervalNum; i++){
//            classInterval[i] = minValue + i * (i + 1) * dif;
            BigDecimal b = new BigDecimal(minValue + (i+1) * (i + 2) * dif);
            double df = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            classInterval[i] = df;
        }

        return classInterval;
    }

    /**
     * 界限等比模型
     * @param minValue
     * @param maxValue
     * @param breakNum
     * @return
     */
    public static double[] modelC1(double minValue, double maxValue, int breakNum) {
//        boolean flag = JUtil.IsIntegerOnly(a);
//        double[] b = new double[num + 1];
//        b[0] = JStatistics.MinPositive(a);
//        b[num] = JStatistics.Max(a);
        int intervalNum = breakNum-1;
        double[] classInterval = new double[intervalNum];
        double ratn = maxValue / minValue;
        double rat = Math.pow(ratn, 1d / intervalNum);
        for (int i = 0; i <intervalNum; i++){
//            b[i] = flag ? (int)(b[0] * Math.pow(rat, i) + 0.5) : JUtil.GetDecimalTipValue(b[0] * Math.pow(rat, i));
            BigDecimal b = new BigDecimal(minValue* Math.pow(rat, (i+1)));
            double df = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            classInterval[i] = df;
        }

        return classInterval;
    }

    /**
     * 间隔等比模型
     *
     * @param minValue
     * @param maxValue
     * @param breakNum
     * @return
     */
    public static double[] modelD1(double minValue, double maxValue, int breakNum) {
//        boolean flag = JUtil.IsIntegerOnly(a);
//        double[] b = new double[num + 1];
//        b[0] = JStatistics.MinPositive(a);
//        b[num] = JStatistics.Max(a);
//        double ratn = b[num] / b[0];
//        double rat = JFormula.EquationR(num, ratn);
//        for (int i = 1; i < b.length - 1; i++)
//            b[i] = flag ? (int)(b[0] + JFormula.PolynomialsR(i, rat) + 0.5) : JUtil.GetDecimalTipValue(b[0] + JFormula.PolynomialsR(i, rat));
//        return b;

        int intervalNum = breakNum-1;
        double[] classInterval = new double[intervalNum];
        double ratn = maxValue / minValue /20;
        double rat = Formula.EquationR(breakNum, ratn);
        for (int i = 0; i <intervalNum; i++){
//            b[i] = flag ? (int)(b[0] + JFormula.PolynomialsR(i, rat) + 0.5) : JUtil.GetDecimalTipValue(b[0] + JFormula.PolynomialsR(i, rat));
            BigDecimal b = new BigDecimal(minValue+ Formula.PolynomialsR((i+1), rat));
            double df = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            classInterval[i] = df;
        }
        return classInterval;
    }

}
