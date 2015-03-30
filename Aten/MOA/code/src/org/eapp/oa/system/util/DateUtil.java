package org.eapp.oa.system.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 判断前者时间与后者差几个月
     * 
     * @param t1 前者时间 字符串
     * @param t2 后者时间 字符串
     * @return 相差的月数
     * 
     *         <pre>
     * 修改日期 修改人     修改原因
     * 2012-11-28   卢凯宁     修改注释
     * </pre>
     */
    public static int timeCompare(String t1, String t2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return timeCompare(formatter.parse(t1), formatter.parse(t2));
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 判断前者时间与后者差几个月
     * 
     * @param t1 前者时间
     * @param t2 后者时间
     * @return 相差的月数
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-28	卢凯宁		修改注释
     * </pre>
     */
    public static int timeCompare(Date t1, Date t2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(t1);
        c2.setTime(t2);
        ArrayList<String> list = new ArrayList<String>();
        while (!c1.after(c2)) {
            list.add("add");
            c1.add(Calendar.MONTH, 1);
        }
        return list.size() - 1;
    }

    /**
     * 增加 或者 较少 相应 天数
     * 
     * @param oldDate 旧的日期
     * @param plusOrCutFlag true: + false -
     * @param count 相应天数
     * @return 新的日期
     * 
     *         <pre>
     * 修改日期 修改人     修改原因
     * 2012-11-15   卢凯宁     新建
     * </pre>
     */
    public static Date getNewDate(Date oldDate, boolean plusOrCutFlag, int count) {
        if (oldDate == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(oldDate.getTime());
        if (plusOrCutFlag) {
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) + count);
        } else {
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) - count);
        }
        oldDate = new Date(ca.getTimeInMillis());
        return oldDate;
    }

    /**
     * 增加 或者 较少 相应 天数 , 返回新的 timestamp
     * 
     * @param timestamp 旧的日期
     * @param plusOrCutFlag true: + false -
     * @param count 相应天数
     * @return 新的timestamp
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-28	卢凯宁		新建
     * </pre>
     */
    public static Timestamp getNewTimestamp(Timestamp timestamp, boolean plusOrCutFlag, int count) {
        if (timestamp == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(timestamp.getTime());
        if (plusOrCutFlag) {
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) + count);
        } else {
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) - count);
        }
        timestamp = new Timestamp(ca.getTimeInMillis());
        return timestamp;
    }

    /**
     * 比较时间,返回比较结果
     * 
     * @param t1
     * @param t2
     * @return -1 t1<t2 ; 0 t1=t2 ; 1 t1>t2
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-12-7	卢凯宁		新建
     * </pre>
     */
    public static int compareTime(Timestamp t1, Timestamp t2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(t1);
        c2.setTime(t2);
        return c1.compareTo(c2);
    }

    public static void main(String args[]) throws ParseException {
        System.out.println(timeCompare("2010-6-1", "2010-9-2"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(formatter.parse("2010-9-2"));
        c2.setTime(formatter.parse("2010-9-2"));

        System.out.println(c1.compareTo(c2));

    }
}
